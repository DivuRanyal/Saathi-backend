package service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import model.AggregatedServiceReport;
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
                    Integer subscriberAlaCarteServicesID = null;

                    if(serviceDate!=null) {
                    	 requestedDates = generateRequestedDateTimes(calculatedFrequency, serviceDate, serviceTime);  
                    	 subscriberAlaCarteServicesID = alaCarteServicesRepository.getSubscriberAlaCarteServicesIDForPackageService(
                    		        subscriberID, serviceDate, serviceTime, alaCarteServiceID);
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
                        subscriberAlaCarteServicesID,null,null
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
                alaCarteService.getSubscriberAlaCarteServicesID(),1,null
            );
             System.out.println(alaCarteServiceReport);
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
            LocalDate parsedPreferredDate, LocalTime parsedPreferredTime, LocalDateTime createdDate, Integer frequencyInstance) throws Exception {

        // Fetch the cached service reports from Memcached or in-memory
        Map<String, List<ServiceReport>> allServicesMap = safeGetCachedServices(subscriberID);

        if (allServicesMap == null || allServicesMap.get("allServices") == null) {
            throw new Exception("No services found for subscriber ID: " + subscriberID);
        }

        // Fetch all service reports for the subscriber
        List<ServiceReport> allServiceReports = allServicesMap.get("allServices");
        ServiceReport updatedServiceReport = null;

        // Iterate through service reports to find the matching service
        for (ServiceReport serviceReport : allServiceReports) {
            
            // Correct comparison with equals() for object types and continue with checks
            if (serviceReport.getServiceID() == serviceID && 
                    serviceReport.isAlaCarte() == isAlaCarte && 
                    serviceReport.getFrequencyInstance().equals(frequencyInstance) && 
                    serviceReport.getPending() != 0) {
                System.out.println("Entered update");
                // Check if the number of preferred date-times exceeds the allowed frequency
                if (serviceReport.getPreferredDateTimes() != null && 
                    serviceReport.getPreferredDateTimes().size() >= serviceReport.getFrequencyCount()) {
                    throw new RequestedDateTimesExceedsFrequencyException(
                        "RequestedDateTimes exceeds the allowed frequency count for service ID: " + serviceID);
                }

                // Add the new requested date and time
                PreferredDateTime newRequestedDateTime = new PreferredDateTime(parsedPreferredDate, parsedPreferredTime);
                newRequestedDateTime.setRequestedDate(createdDate);
                serviceReport.addPreferredDateTime(newRequestedDateTime);

                // Create a new SubscriberAlaCarteServices entity for this request
                SubscriberAlaCarteServices newAlaCarteService = new SubscriberAlaCarteServices();
                newAlaCarteService.setServiceID(serviceID);
                newAlaCarteService.setServiceDate(parsedPreferredDate);
                newAlaCarteService.setServiceTime(parsedPreferredTime);
                newAlaCarteService.setIsPackageService(!isAlaCarte); // This sets true if it's a package service, false if ala-carte
                newAlaCarteService.setSubscriber(
                    subscriberRepository.findById(subscriberID).orElseThrow(() -> new Exception("Subscriber not found")));

                // Optionally, set other fields like billing status, created date, etc.
                newAlaCarteService.setBillingStatus(1); // Example value for billing status
                newAlaCarteService.setIsAccepted(true); // Example value for isAccepted
                newAlaCarteService.setCreatedDate(new Date()); // Automatically set the creation date

                // Save the new entry in the database
                SubscriberAlaCarteServices savedAlaCarteService = alaCarteServicesRepository.save(newAlaCarteService);

                // Update the service report to store the new SubscriberAlaCarteServicesID
                serviceReport.setSubscriberAlaCarteServicesID(savedAlaCarteService.getSubscriberAlaCarteServicesID());

                updatedServiceReport = serviceReport;
                break;
            }
        }

        // Handle case when no matching service was found
        if (updatedServiceReport == null) {
            throw new Exception("Service not found for subscriber ID: " + subscriberID + " and service ID: " + serviceID);
        }

        // Update the cache and in-memory storage
        cacheManager.getCache("subscriberServicesCache").put(subscriberID, allServicesMap);
        subscriberServiceMap.put(subscriberID, allServiceReports);
        inMemoryServiceTracker.startTracking(subscriberID, allServiceReports);

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
            LocalTime preferredTime,
            Integer frequencyInstance) {

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
            System.out.println(service.getServiceID() + " " + serviceID + " " + service.getFrequencyInstance() + " " + frequencyInstance);

            // Use '==' for primitive int comparison instead of .equals()
            if (service.getServiceID() == serviceID && service.getFrequencyInstance().equals(frequencyInstance)) {
                System.out.println("PackageServiceID: " + service.getPackageServiceID());
                System.out.println("isAlaCarteService: " + isAlaCarteService);

                // Handle ala-carte services
                if (isAlaCarteService && service.getSubscriberAlaCarteServicesID() != null
                        && service.getSubscriberAlaCarteServicesID().equals(subscriberAlaCarteServicesID)) {
                    serviceUpdated = updateServiceFrequencyCompletion(service, null, null, frequencyInstance);  // Pass frequencyCount
                }                

                // Handle package services
                else if (!isAlaCarteService && service.getPackageServiceID() != null) { 
                    // Case where preferredDate and preferredTime are provided
                    if (preferredDate != null && preferredTime != null) {
                        // Ensure we are updating the correct frequency instance
                        serviceUpdated = updateServiceFrequencyCompletion(service, preferredDate, preferredTime, frequencyInstance);
                        
                        // Ensure subscriberAlaCarteServicesID is only updated for the correct frequency instance
                        if (service.getFrequencyInstance() == frequencyInstance) {
                            service.setSubscriberAlaCarteServicesID(subscriberAlaCarteServicesID);
                        }
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


    private boolean updateServiceFrequencyCompletion(ServiceReport service, LocalDate preferredDate, LocalTime preferredTime, int frequencyInstance) {
        List<PreferredDateTime> requestedDates = service.getPreferredDateTimes();
        System.out.println("Service Package Name: " + service.getPackageName());
        System.out.println("Is Ala-carte: " + "Ala-carte".equals(service.getPackageName()));
        System.out.println("Preferred Date: " + preferredDate);
        System.out.println("Preferred Time: " + preferredTime);
        System.out.println("Frequency Instance: " + frequencyInstance);

        boolean updated = false;

        // Check if the frequency count matches and update the corresponding service frequency
        if (service.getFrequencyInstance() == frequencyInstance) {
            System.out.println("Updating frequency instance: " + frequencyInstance);
            
            // Update completion based on date and time for package services, or directly for ala-carte
            for (PreferredDateTime preferredDateTime : requestedDates) {
                if ("Ala-carte".equals(service.getPackageName()) || 
                    (preferredDate != null && preferredDateTime.getPreferredDate() != null &&
                    preferredTime != null && preferredDateTime.getPreferredTime() != null &&
                    preferredDate.equals(preferredDateTime.getPreferredDate()) && 
                    preferredTime.equals(preferredDateTime.getPreferredTime()))) {

                    // Update based on frequency count and preferred date/time
                    preferredDateTime.setCompletionDate(LocalDate.now());
                    preferredDateTime.setCompletionStatus("Completed");

                    service.setCompletions(service.getCompletions() + 1);
                    service.setPending(0);

                    if (service.getCompletions()==1) {
                        service.setCompletionStatus("Completed");
                        service.setCompletionDate(LocalDateTime.now());
                    } else {
                        service.setCompletionStatus("In Progress");
                    }

                    updated = true;
                    break;  // Exit loop after first match/update
                }
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
        service.setPending(0);

        // Check if the package service is fully completed
        if (newCompletions ==1) {
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
                serviceReport.setServiceRating(interaction.getServiceRating());
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
        System.out.println("size" + alaCarteServices.size());
        List<ServiceReport> serviceReports = new ArrayList<>();
        Set<Integer> completedServiceIds = new HashSet<>();

        // Handle interactions (completed services)
        for (Interaction interaction : interactions) {
            ServiceReport serviceReport = new ServiceReport();

            // Case 1: Interaction is of ala-carte
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
                serviceReport.setFrequency(1);
                serviceReport.setFrequencyInstance(interaction.getFrequencyInstance());
                completedServiceIds.add(alaCarteService.getService().getServiceID());
            } 
            // Case 2: Package service with null preferredDate and preferredTime (no subscriberAlaCarteServicesID)
            else if (interaction.getPackageServices() != null && interaction.getSubscriberAlaCarteServices() == null) {
                serviceReport.setServiceID(interaction.getPackageServices().getService().getServiceID());
                serviceReport.setServiceName(interaction.getPackageServices().getService().getServiceName());
                serviceReport.setPackageName(interaction.getPackageServices().getSubscriptionPackage().getPackageName());
                serviceReport.setAlaCarte(false);
                serviceReport.setPackageServiceID(interaction.getPackageServices().getPackageServicesID());
                serviceReport.setCompletions(1);
                serviceReport.setCompletionStatus("Completed");
                serviceReport.setCompletionDate(LocalDateTime.now());
                serviceReport.setFrequency(interaction.getPackageServices().getFrequency());
                serviceReport.setFrequencyInstance(interaction.getFrequencyInstance()); // Frequency count
                serviceReport.setPending(0);
            }
            // Case 3: Package service with preferredDate and preferredTime (with subscriberAlaCarteServicesID)
            else if (interaction.getPackageServices() != null && interaction.getSubscriberAlaCarteServices() != null) {
                SubscriberAlaCarteServices alaCarteService = interaction.getSubscriberAlaCarteServices();
                serviceReport.setServiceID(interaction.getPackageServices().getService().getServiceID());
                serviceReport.setServiceName(interaction.getPackageServices().getService().getServiceName());
                serviceReport.setPackageName(interaction.getPackageServices().getSubscriptionPackage().getPackageName());
                serviceReport.setAlaCarte(false);
                serviceReport.setPackageServiceID(interaction.getPackageServices().getPackageServicesID());
                serviceReport.setSubscriberAlaCarteServicesID(alaCarteService.getSubscriberAlaCarteServicesID()); // Populated
                serviceReport.setCompletions(1);
                serviceReport.setCompletionStatus("Completed");
                serviceReport.setCompletionDate(LocalDateTime.now());
                serviceReport.setFrequencyInstance(interaction.getFrequencyInstance()); // Frequency count
                serviceReport.setFrequency(interaction.getPackageServices().getFrequency());
                serviceReport.setPending(0);
            }

            serviceReports.add(serviceReport);
        }

        // Handle ala-carte services that are not completed
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
                serviceReport.setFrequency(1);
                serviceReport.setPending(1);
                serviceReports.add(serviceReport);
            }
        }

        // Add remaining package services
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
                int frequencyCount = packageService.getFrequency();
                
                // Create a separate ServiceReport for each frequency
                for (int i = 1; i <= frequencyCount; i++) {
                    ServiceReport serviceReport = new ServiceReport();
                    serviceReport.setServiceID(packageService.getService().getServiceID());
                    serviceReport.setServiceName(packageService.getService().getServiceName());
                    serviceReport.setPackageName(packageService.getSubscriptionPackage().getPackageName());
                    serviceReport.setAlaCarte(false);
                    serviceReport.setPackageServiceID(packageService.getPackageServicesID());

                    serviceReport.setCompletions(0);
                    serviceReport.setCompletionStatus("Pending");

                    // Set the frequency count and use an integer for frequencyInstance
                    serviceReport.setFrequencyCount(i);  // The current instance number (1st, 2nd, etc.)
                    serviceReport.setPending(1); // Total pending occurrences
                    serviceReport.setFrequency(frequencyCount);  // Total number of occurrences for this service
                    serviceReport.setFrequencyUnit(packageService.getService().getFrequencyUnit());
                    
                    // Use an integer to represent the frequency instance
                    serviceReport.setFrequencyInstance(i);  // Now an integer representing each instance
                    
                    serviceReport.setPreferredDateTimes(null); // Placeholder for preferred times if required
                    
                    serviceReports.add(serviceReport);
                }
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
    
    
    public List<AggregatedServiceReport> aggregateServiceDataByPackageServiceIDFromCache(int subscriberID) throws Exception {
        // Fetch the cached service reports from Memcached or in-memory
        Map<String, List<ServiceReport>> allServicesMap = safeGetCachedServices(subscriberID);

        // Check if the services are available in cache
        if (allServicesMap == null || allServicesMap.get("allServices") == null) {
            throw new Exception("No services found for subscriber ID: " + subscriberID);
        }

        // Fetch all service reports for the subscriber
        List<ServiceReport> allServiceReports = allServicesMap.get("allServices");
        Map<Integer, AggregatedServiceReport> aggregatedReportsMap = new HashMap<>();

        // Iterate through the service reports and aggregate them by packageServiceID
        for (ServiceReport serviceReport : allServiceReports) {
            Integer packageServiceID = serviceReport.getPackageServiceID();

        	
            // If the packageServiceID already exists in the map, update its pending, completions, and frequencyCount
            if (aggregatedReportsMap.containsKey(packageServiceID)) {
                AggregatedServiceReport aggregatedReport = aggregatedReportsMap.get(packageServiceID);

             	System.out.println(serviceReport.getPending());
             	System.out.println(serviceReport.getCompletions());
                // Update the aggregated report's pending, completions, and frequencyCount
                aggregatedReport.setPending(aggregatedReport.getPending() + serviceReport.getPending());
                aggregatedReport.setCompletions(aggregatedReport.getCompletions() + serviceReport.getCompletions());
                
            } else {
            	System.out.println("hh"+serviceReport);
            	// Assuming the date string follows the format "yyyy-MM-dd'T'HH:mm:ss"
            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            	LocalDateTime completionDate = serviceReport.getCompletionDate() != null 
            	                                ? LocalDateTime.parse(serviceReport.getCompletionDate(), formatter) 
            	                                : null;
            	System.out.println(serviceReport.getPending());
            	System.out.println(serviceReport.getCompletions());
            	// Create a new AggregatedServiceReport using the constructor
            	AggregatedServiceReport newReport = new AggregatedServiceReport(
            	    serviceReport.getFrequencyUnit(),                // frequencyUnit
            	    serviceReport.getPackageServiceID(),             // packageServiceID
            	    serviceReport.getPending(),                      // pending
            	    serviceReport.getCompletions(),                  // completions
            	    serviceReport.getServiceName(),                  // serviceName
            	    serviceReport.getFrequency(),                    // frequency
            	    null,                                            // interactions (if you have it, replace null)
            	    serviceReport.isAlaCarte(),                      // alaCarte
     //       	    serviceReport.getPreferredDateTimes(),           // preferredDatesTimes
            	    null,
            	    completionDate,              // completionDate
            	    serviceReport.getPackageName(),                  // packageName
            	    serviceReport.getCompletionStatus(),             // completionStatus
            	    serviceReport.getServiceID(),                    // serviceID
     //       	    serviceReport.getSubscriberAlaCarteServicesID()  // subscriberAlaCarteServicesID
            	    null
            	);

            	System.out.println(newReport.toString());  // Print the AggregatedServiceReport object

                // Add to the map
                aggregatedReportsMap.put(packageServiceID, newReport);
            }
        }

        // Convert the map values to a list and return the aggregated service reports
        return new ArrayList<>(aggregatedReportsMap.values());
    }

}
