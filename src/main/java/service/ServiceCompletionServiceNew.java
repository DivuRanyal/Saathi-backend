package service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import exception.RequestedDateTimesExceedsFrequencyException;
import model.Interaction;
import model.PackageServices;
import model.PreferredDateTime;
import model.ServiceReport;
import model.Subscriber;
import model.SubscriberAlaCarteServices;
import repository.InteractionRepository;
import repository.PackageServiceRepository;
import repository.SubscriberAlaCarteServicesRepository;
import repository.SubscriberRepository;

@Service
public class ServiceCompletionServiceNew {

    @Autowired
    private CacheManager cacheManager;

    private final Map<Integer, List<ServiceReport>> subscriberServiceMap = new HashMap<>();

    @Autowired
    private PackageServiceRepository packageServiceRepository;

    @Autowired
    private InMemoryServiceTracker inMemoryServiceTracker;

    @Autowired
    private SubscriberAlaCarteServicesRepository alaCarteServicesRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;
    
    @Autowired
    private InteractionRepository interactionRepository;

    @Autowired
    private PackageServicesService packageServiceService; // Injecting the service

    // Complete a service by marking it in the report
    public void completeService(Integer subscriberID, Integer serviceID) {
        List<ServiceReport> services = subscriberServiceMap.get(subscriberID);

        if (services != null) {
            for (ServiceReport service : services) {
                if (service.getServiceID() == serviceID) {
                    service.setCompletions(1);
                    service.setCompletionStatus("Completed");
                    service.setCompletionDate(LocalDateTime.now());
                    inMemoryServiceTracker.updateTracking(subscriberID, services);
                    return;
                }
            }
            System.out.println("Service with ID: " + serviceID + " not found for subscriber ID: " + subscriberID);
        } else {
            System.out.println("No services found for subscriber ID: " + subscriberID);
        }
    }

    @CachePut(value = "subscriberServicesCache", key = "#subscriberID")
    public Map<String, List<ServiceReport>> trackSubscriberServices(Integer subscriberID, int packageID, int alaCarteServiceID, LocalDate serviceDate, LocalTime serviceTime) {
        List<ServiceReport> serviceReports = subscriberServiceMap.getOrDefault(subscriberID, new ArrayList<>());

        if (packageID != 0) {
            List<PackageServices> packageServices = packageServiceRepository.findServicesByPackageId(packageID);
            if (packageServices != null && !packageServices.isEmpty()) {
                for (PackageServices packageService : packageServices) {
                	List<PreferredDateTime> requestedDates=null;
                    int calculatedFrequency = packageService.getFrequency();
                    if(serviceDate!=null) {
                    	 requestedDates = generateRequestedDateTimes(calculatedFrequency, serviceDate, serviceTime);                         
                    }
                    ServiceReport report = new ServiceReport(
                        packageService.getService().getServiceID(),
                        packageService.getService().getServiceName(),
                        packageService.getSubscriptionPackage().getPackageName(),
                        calculatedFrequency,
                        packageService.getService().getFrequencyUnit(),
                        0,
                        "Not Completed",
                        null, 
                        false,
                        packageService.getPackageServicesID(),
                        requestedDates,
                        null
                    );

                    if (!serviceReports.contains(report)) {
                        serviceReports.add(report);
                    }
                }
            }
        }

        if (alaCarteServiceID != 0) {
            SubscriberAlaCarteServices alaCarteService = alaCarteServicesRepository.findById(alaCarteServiceID)
                    .orElseThrow(() -> new RuntimeException("Ala-carte service not found"));
            
            List<PreferredDateTime> requestedDates = generateRequestedDateTimes(1, serviceDate, serviceTime);
             ServiceReport alaCarteServiceReport = new ServiceReport(
                alaCarteService.getService().getServiceID(),
                alaCarteService.getService().getServiceName(),
                "Ala-carte",
                1,
                "Single",
                0,
                "Not Completed",
                null,
                true,
                null,
                requestedDates,
                alaCarteService.getSubscriberAlaCarteServicesID()
            );

            if (!serviceReports.contains(alaCarteServiceReport)) {
                serviceReports.add(alaCarteServiceReport);
            }
        }

        subscriberServiceMap.put(subscriberID, serviceReports);
        inMemoryServiceTracker.startTracking(subscriberID, serviceReports);

        Map<String, List<ServiceReport>> services = new HashMap<>();
        services.put("allServices", serviceReports);
        return services;
    }

    private List<PreferredDateTime> generateRequestedDateTimes(int frequency, LocalDate startDate, LocalTime startTime) {
        List<PreferredDateTime> requestedDates = new ArrayList<>();
        for (int i = 0; i < frequency; i++) {
            requestedDates.add(new PreferredDateTime(startDate.plusWeeks(i), startTime));
        }
        return requestedDates;
    }
    @CachePut(value = "subscriberServicesCache", key = "#subscriberID")
    public Map<String, List<ServiceReport>> updateServiceRequestedDateTime(
            int subscriberID, int serviceID, boolean isAlaCarte, 
            LocalDate parsedPreferredDate, LocalTime parsedPreferredTime, LocalDateTime createdDate) throws Exception {

        // Fetch the cached service reports from Memcached or in-memory
        Map<String, List<ServiceReport>> allServicesMap = safeGetCachedServices(subscriberID);

        // Validate if services exist
        if (allServicesMap == null || allServicesMap.get("allServices") == null) {
            throw new Exception("No services found for subscriber ID: " + subscriberID);
        }

        // Fetch all service reports for the subscriber
        List<ServiceReport> allServiceReports = allServicesMap.get("allServices");
        ServiceReport updatedServiceReport = null;

        // Iterate through service reports to find the matching service
        for (ServiceReport serviceReport : allServiceReports) {
            if (serviceReport.getServiceID() == serviceID && serviceReport.isAlaCarte() == isAlaCarte) {
                // Add the new requested date and time to the service report
                if (serviceReport.getPreferredDateTimes() != null) {
                    if (serviceReport.getPreferredDateTimes().size() >= serviceReport.getFrequencyCount()) {
                        throw new RequestedDateTimesExceedsFrequencyException("RequestedDateTimes exceeds the allowed frequency count for service ID: " + serviceID);
                    }
                }
                
                // Create a new RequestedDateTime object with preferred date, preferred time, and created date
                PreferredDateTime newRequestedDateTime = new PreferredDateTime(parsedPreferredDate, parsedPreferredTime);
                newRequestedDateTime.setRequestedDate(createdDate); // Set createdDate as the requestedDate
                
                // Add the new requested date and time to the service report
                serviceReport.addPreferredDateTime(newRequestedDateTime);

                updatedServiceReport = serviceReport;
                break; // Exit after updating the requested date and time
            }
        }

        // Throw an error if no matching service is found
        if (updatedServiceReport == null) {
            throw new Exception("Service not found for subscriber ID: " + subscriberID + " and service ID: " + serviceID);
        }

        // Update the cache and in-memory storage
        cacheManager.getCache("subscriberServicesCache").put(subscriberID, allServicesMap);
        subscriberServiceMap.put(subscriberID, allServiceReports);
        inMemoryServiceTracker.startTracking(subscriberID, allServiceReports);

        // Return the updated services map
        return allServicesMap;
    }

    private Map<String, List<ServiceReport>> safeGetCachedServices(int subscriberID) throws Exception {
        Object cachedData = cacheManager.getCache("subscriberServicesCache").get(subscriberID, Object.class);

        if (cachedData instanceof Map) {
            try {
                return (Map<String, List<ServiceReport>>) cachedData;
            } catch (ClassCastException e) {
                throw new Exception("Cached data is not in the expected format for subscriber ID: " + subscriberID);
            }
        } else {
            throw new Exception("Cached data is missing or invalid for subscriber ID: " + subscriberID);
        }
    }

    @CachePut(value = "subscriberServicesCache", key = "#subscriberID")
    public Map<String, List<ServiceReport>> updateServiceCompletion(
            Integer subscriberID, 
            Integer serviceID, 
            Integer subscriberAlaCarteServicesID, 
            boolean isAlaCarteService, 
            LocalDate preferredDate, 
            LocalTime preferredTime) {

        // Retrieve the list of services for the subscriber
        List<ServiceReport> services = subscriberServiceMap.get(subscriberID);
        System.out.println("Number of services: " + (services != null ? services.size() : 0));

        // Return null if no services found
        if (services == null || services.isEmpty()) {
            return null;
        }

        boolean serviceUpdated = false;

        // Loop through the services and update the correct one
        for (ServiceReport service : services) {
            if (service.getServiceID() == serviceID) {
                System.out.println("PackageserviceID: " + service.getPackageServiceID());
                System.out.println("isAlaCarteService: " + isAlaCarteService);

                // Handle ala-carte services
                if (isAlaCarteService && service.getSubscriberAlaCarteServicesID() != null
                        && service.getSubscriberAlaCarteServicesID().equals(subscriberAlaCarteServicesID)) {
                    serviceUpdated = updateServiceFrequencyCompletion(service, null, null);
                }                

                // Handle package services
                else if (!isAlaCarteService && service.getPackageServiceID() != null) { 
                    // Case where preferredDate and preferredTime are provided
                    if (preferredDate != null && preferredTime != null) {
                        serviceUpdated = updateServiceFrequencyCompletion(service, preferredDate, preferredTime);
                    }
                    
                    // Case where preferredDate and preferredTime are null
                    else {   
                        System.out.println("Handling package service update for serviceID: " + serviceID);
                        serviceUpdated = updatePackageService(service);
                    }
                }

                // If a service was successfully updated, break the loop
                if (serviceUpdated) {
                    System.out.println("Service has been updated. Breaking the loop.");
                    break; // Stop further processing once updated
                }
            }
        }

        // If a service was successfully updated, return the updated services
        if (serviceUpdated) {
            Map<String, List<ServiceReport>> updatedServices = new HashMap<>();
            updatedServices.put("allServices", services);
            return updatedServices;
        }

        return null;
    }

    private boolean updateServiceFrequencyCompletion(ServiceReport service, LocalDate preferredDate, LocalTime preferredTime) {
        List<PreferredDateTime> requestedDates = service.getPreferredDateTimes();
        System.out.println(requestedDates.size());
        // Log basic information
        System.out.println("Service Package Name: " + service.getPackageName());
        System.out.println("Is Ala-carte: " + "Ala-carte".equals(service.getPackageName()));
        System.out.println("Preferred Date: " + preferredDate);
        System.out.println("Preferred Time: " + preferredTime);

        boolean updated = false;

        // Loop over requested dates to update Ala-carte services or match based on date and time
        for (PreferredDateTime preferredDateTime : requestedDates) {
            // If Ala-carte, force update without checking dates
            if ("Ala-carte".equals(service.getPackageName())) {
                System.out.println("Updating Ala-carte service.");
                LocalDate date=LocalDate.now();
                preferredDateTime.setCompletionDate(date);
                preferredDateTime.setCompletionStatus("Completed");

                service.setCompletions(service.getCompletions() + 1);
                service.setPending(service.getFrequencyCount() - service.getCompletions());

                // Update overall service completion status
                if (service.getCompletions() >= service.getFrequencyCount()) {
                    service.setCompletionStatus("Completed");
                    service.setCompletionDate(LocalDateTime.now());
                } else {
                    service.setCompletionStatus("In Progress");
                }

                updated = true;
                break;  // Exit loop after first match/update
            } else if (preferredDate != null && preferredDateTime.getPreferredDate() != null &&
                       preferredTime != null && preferredDateTime.getPreferredTime() != null &&
                       preferredDate.equals(preferredDateTime.getPreferredDate()) && 
                       preferredTime.equals(preferredDateTime.getPreferredTime())) {
                // If not Ala-carte, update based on date and time
                System.out.println("Updating based on date and time match.");
                
                preferredDateTime.setCompletionDate(LocalDate.now());
                preferredDateTime.setCompletionStatus("Completed");

                service.setCompletions(service.getCompletions() + 1);
                service.setPending(service.getFrequencyCount() - service.getCompletions());

                if (service.getCompletions() >= service.getFrequencyCount()) {
                    service.setCompletionStatus("Completed");
                    service.setCompletionDate(LocalDateTime.now());
                } else {
                    service.setCompletionStatus("In Progress");
                }

                updated = true;
                break;  // Exit loop after first match/update
            }
        }

        if (!updated) {
            System.out.println("No match found for update.");
        }

        return updated;
    }

    private boolean updatePackageService(ServiceReport service) {
        // Check if the package service has already reached the frequency limit
        if (service.getCompletions() >= service.getFrequencyCount()) {
            System.out.println("Package Service: " + service.getServiceName() + " is already completed.");
            return false; // Prevent further updates if the service is already completed
        }

        // Increment completions for package service
        int newCompletions = service.getCompletions() + 1;
        service.setCompletions(newCompletions);

        // Recalculate the pending count and frequency count for package service
        service.setPending(service.getFrequencyCount() - newCompletions);

        // Check if the package service is fully completed
        if (newCompletions >= service.getFrequencyCount()) {
            service.setCompletionStatus("Completed");
            service.setCompletionDate(LocalDateTime.now());
            System.out.println("Package Service: " + service.getServiceName() + " marked as completed on " + service.getCompletionDate());
        } else {
            service.setCompletionStatus("In Progress");
            System.out.println("Package Service: " + service.getServiceName() + " updated with completions: " + newCompletions);
        }

        return true;  // Service was updated successfully
    }



    @Cacheable(value = "subscriberServicesCache", key = "#subscriberID")
    public Map<String, List<ServiceReport>> getSubscriberServices(Integer subscriberID) {
        List<ServiceReport> serviceReports = subscriberServiceMap.get(subscriberID);
        System.out.println("--"+serviceReports.size());
        if (serviceReports == null || serviceReports.isEmpty()) {
            List<Interaction> interactions = interactionRepository.findBySubscriberID(subscriberID);

            if (interactions == null || interactions.isEmpty()) {
                return null;
            }

            serviceReports = convertInteractionsToServiceReports(interactions);
            subscriberServiceMap.put(subscriberID, serviceReports);
        }

        Map<String, List<ServiceReport>> cachedServices = new HashMap<>();
        cachedServices.put("allServices", serviceReports);
        return cachedServices;
    }

    private List<ServiceReport> convertInteractionsToServiceReports(List<Interaction> interactions) {
        List<ServiceReport> serviceReports = new ArrayList<>();
        for (Interaction interaction : interactions) {
            ServiceReport serviceReport = new ServiceReport();
            if (interaction.getSubscriberAlaCarteServices() != null) {
                serviceReport.setServiceID(interaction.getSubscriberAlaCarteServices().getSubscriberAlaCarteServicesID());
                serviceReport.setServiceName(interaction.getSubscriberAlaCarteServices().getService().getServiceName());
            }

            serviceReports.add(serviceReport);
        }

        return serviceReports;
    }

    @CachePut(value = "subscriberServicesCache", key = "#subscriberID")
    @Transactional
    public Map<String, List<ServiceReport>> rebuildAllServices(int subscriberID) {
        List<Interaction> interactions = interactionRepository.findBySubscriberID(subscriberID);
        List<SubscriberAlaCarteServices> alaCarteServices = alaCarteServicesRepository.findBySubscriber_SubscriberID(subscriberID);
        System.out.println("size"+alaCarteServices.size());
        List<ServiceReport> serviceReports = new ArrayList<>();
        Set<Integer> completedServiceIds = new HashSet<>();
        for (Interaction interaction : interactions) {
            ServiceReport serviceReport = new ServiceReport();
            if (interaction.getSubscriberAlaCarteServices() != null) {
                SubscriberAlaCarteServices alaCarteService = interaction.getSubscriberAlaCarteServices();
                serviceReport.setServiceID(alaCarteService.getService().getServiceID());
                serviceReport.setServiceName(alaCarteService.getService().getServiceName());
                serviceReport.setPackageName("Ala-carte");
                serviceReport.setAlaCarte(true);
                serviceReport.setSubscriberAlaCarteServicesID(alaCarteService.getSubscriberAlaCarteServicesID());
                serviceReport.setCompletions(1);
                serviceReport.setCompletionStatus("Completed");
                serviceReport.setCompletionDate(LocalDateTime.now());
                serviceReport.setFrequencyCount(1);
                serviceReport.setPending(0);
                
                completedServiceIds.add(alaCarteService.getService().getServiceID());
            }

            serviceReports.add(serviceReport);
        }

        for (SubscriberAlaCarteServices alaCarteService : alaCarteServices) {
            if (!completedServiceIds.contains(alaCarteService.getService().getServiceID())) {
                ServiceReport serviceReport = new ServiceReport();
                serviceReport.setServiceID(alaCarteService.getService().getServiceID());
                serviceReport.setServiceName(alaCarteService.getService().getServiceName());
                serviceReport.setPackageName("Ala-carte");
                serviceReport.setAlaCarte(true);
                serviceReport.setSubscriberAlaCarteServicesID(alaCarteService.getSubscriberAlaCarteServicesID());
                serviceReport.setCompletions(0);
                serviceReport.setCompletionStatus("Pending");
                serviceReport.setFrequencyCount(1);
                serviceReport.setPending(1);
                
                serviceReports.add(serviceReport);
            }
        }

        addRemainingServices(subscriberID, completedServiceIds, serviceReports);
        subscriberServiceMap.put(subscriberID, serviceReports);
        inMemoryServiceTracker.startTracking(subscriberID, serviceReports);

        Map<String, List<ServiceReport>> allServicesMap = new HashMap<>();
        allServicesMap.put("allServices", serviceReports);
        return allServicesMap;
    }

    public void addRemainingServices(int subscriberID, Set<Integer> completedServiceIds, List<ServiceReport> serviceReports) {
        Subscriber subscriber = subscriberRepository.findById(subscriberID)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberID));
        
        Integer packageID = subscriber.getSubscriptionPackage().getPackageID();
        List<PackageServices> packageServices = packageServiceRepository.findServicesByPackageId(packageID);

        for (PackageServices packageService : packageServices) {
            if (!completedServiceIds.contains(packageService.getService().getServiceID())) {
                ServiceReport serviceReport = new ServiceReport();
                serviceReport.setServiceID(packageService.getService().getServiceID());
                serviceReport.setServiceName(packageService.getService().getServiceName());
                serviceReport.setPackageName(packageService.getSubscriptionPackage().getPackageName());
                serviceReport.setAlaCarte(false);
                serviceReport.setPackageServiceID(packageService.getPackageServicesID());

                serviceReport.setCompletions(0);
                serviceReport.setCompletionStatus("Pending");

                int frequencyCount = packageService.getFrequency();
                serviceReport.setFrequencyCount(frequencyCount);
                serviceReport.setFrequency(frequencyCount);
                serviceReport.setPending(frequencyCount);
                serviceReport.setFrequencyUnit(packageService.getService().getFrequencyUnit());
                serviceReport.setPreferredDateTimes(null);
                
                serviceReports.add(serviceReport);
            }
        }
    }

    public static LocalDate convertToLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            date = new java.util.Date(date.getTime());
        }
        return date.toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toLocalDate();
    }

    public static LocalTime convertToLocalTime(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                      .atZone(ZoneId.systemDefault())
                      .toLocalTime();
    }
}
