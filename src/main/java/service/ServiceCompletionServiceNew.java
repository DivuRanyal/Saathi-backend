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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import model.Interaction;
import model.PackageServices;
import model.ServiceReport;
import model.Subscriber;
import model.SubscriberAlaCarteServices;
import repository.InteractionRepository;
import repository.PackageServiceRepository;
import repository.SubscriberAlaCarteServicesRepository;
import repository.SubscriberRepository;

@Service
public class ServiceCompletionServiceNew {

    // In-memory map to store services for each subscriber
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

    // Complete a service by marking it in the report
    public void completeService(Integer subscriberID, Integer serviceID) {
        List<ServiceReport> services = subscriberServiceMap.get(subscriberID);

        if (services != null) {
            for (ServiceReport service : services) {
                if (service.getServiceID() == serviceID) {
                    service.setCompletions(1); // Assuming one-time completion for manual completion
                    service.setCompletionStatus("Completed");
                    service.setCompletionDate(LocalDateTime.now());
                    inMemoryServiceTracker.updateTracking(subscriberID, services); // Update in-memory tracking
                    return;
                }
            }
            System.out.println("Service with ID: " + serviceID + " not found for subscriber ID: " + subscriberID);
        } else {
            System.out.println("No services found for subscriber ID: " + subscriberID);
        }
    }

    // Retrieve subscriber's service data from cache or in-memory
/*    @Cacheable(value = "subscriberServicesCache", key = "#subscriberId")
    public Map<String, List<ServiceReport>> getSubscriberServices(Integer subscriberId) {
        System.out.println("Retrieving services for subscriber ID: " + subscriberId);
        // Try to retrieve services from in-memory map
        List<ServiceReport> serviceReports = subscriberServiceMap.get(subscriberId);
        // If services are not found in memory, throw an exception or return an appropriate response
        if (serviceReports == null || serviceReports.isEmpty()) {
            // Handle cache miss or in-memory map miss gracefully
        	
            System.out.println("No services found for subscriber with ID: " + subscriberId + ". Attempting to repopulate.");
            
            
            // You can either throw an exception or attempt to re-fetch the services here.
            throw new RuntimeException("No services found for subscriber with ID: " + subscriberId);
        }

        // Prepare the response map
        Map<String, List<ServiceReport>> services = new HashMap<>();
        services.put("allServices", serviceReports);
        return services;
    }
*/
    // Update the service completion for a subscriber
/*    @CachePut(value = "subscriberServicesCache", key = "#subscriberId")
    public Map<String, List<ServiceReport>> updateServiceCompletion(Long subscriberId, int serviceId) {
        // Retrieve the services for the subscriber from the in-memory map
        List<ServiceReport> services = subscriberServiceMap.get(subscriberId);
        System.out.println("Retrieved services from in-memory map: " + services);
        // If services are not found in memory, try to repopulate from the cache
        if (services == null || services.isEmpty()) {
            System.out.println("No services found in memory for subscriber ID: " + subscriberId);

            // Attempt to repopulate from the cache
            try {
                Map<String, List<ServiceReport>> cachedServices = getSubscriberServices(subscriberId);
                if (cachedServices != null && cachedServices.containsKey("allServices")) {
                    services = cachedServices.get("allServices");

                    // Repopulate the in-memory map with services from the cache
                    subscriberServiceMap.put(subscriberId, services);  
                    System.out.println("Repopulated in-memory services for subscriber ID: " + subscriberId);
                }
            } catch (Exception e) {
                System.out.println("Failed to repopulate services for subscriber ID: " + subscriberId + ". Exception: " + e.getMessage());
                return null;
            }

            // If still no services are found, return null
            if (services == null || services.isEmpty()) {
                System.out.println("No services found for subscriber ID: " + subscriberId + " after repopulation.");
                return null;
            }
        }

        boolean serviceUpdated = false;

        // Iterate over the services and find the one that needs to be updated
        for (ServiceReport service : services) {
            System.out.println("Checking service ID: " + service.getServiceID());

            if (service.getServiceID() == serviceId) {
                // Check if the service has already reached the completion limit
                if (service.getCompletions() >= service.getFrequency()) {
                    System.out.println("Service: " + service.getServiceName() + " is already completed.");
                    return null; // Prevent further updates
                }

                // Increment completions
                int newCompletions = service.getCompletions() + 1;
                service.setCompletions(newCompletions);

                // Mark as completed or in-progress
                if (newCompletions >= service.getFrequency()) {
                    service.setCompletionStatus("Completed");
                    service.setCompletionDate(LocalDateTime.now());
                    System.out.println("Service: " + service.getServiceName() + " marked as completed.");
                } else {
                    service.setCompletionStatus("In Progress");
                    System.out.println("Service: " + service.getServiceName() + " updated with completions: " + newCompletions);
                }

                serviceUpdated = true;
                break;
            }
        }

        // If the service was updated, update the in-memory map and cache
        if (serviceUpdated) {
            System.out.println("Updating in-memory map and cache for subscriber ID: " + subscriberId);
            subscriberServiceMap.put(subscriberId, services);  // Update in-memory map
            inMemoryServiceTracker.updateTracking(subscriberId, services);  // Update in-memory tracking

            // Return the updated services for caching
            Map<String, List<ServiceReport>> updatedServices = new HashMap<>();
            updatedServices.put("allServices", services);

            return updatedServices;  // This will update the cache
        }

        System.out.println("Service with ID: " + serviceId + " not found for subscriber ID: " + subscriberId);
        return null;  // Return null if the service is not found
    }

*/
    // Track package and ala-carte services for a subscriber and store them in cache
    @CachePut(value = "subscriberServicesCache", key = "#subscriberID")
    public Map<String, List<ServiceReport>> trackSubscriberServices(Integer subscriberID, int packageID, int alaCarteServiceID) {
        System.out.println("Tracking services for subscriber ID: " + subscriberID); 
 //       List<ServiceReport> serviceReports = new ArrayList<>();
        // Fetch existing services from in-memory map (if any)
        List<ServiceReport> serviceReports = subscriberServiceMap.getOrDefault(subscriberID, new ArrayList<>());
       // Handle package services
        if (packageID != 0) {
            List<PackageServices> packageServices = packageServiceRepository.findServicesByPackageId(packageID);
            if (packageServices == null || packageServices.isEmpty()) {
                System.out.println("No package services found for packageId: " + packageID);
            } else {
                for (PackageServices packageService : packageServices) {
   //             	int calculatedFrequency = packageService.getService().getFrequency() * packageService.getFrequency();
                	int calculatedFrequency =  packageService.getFrequency();
                	System.out.println("calculatedFrequency"+calculatedFrequency);
                	LocalDate dummyRequestedDate = LocalDate.of(2024, 9, 20); // Example date: September 20, 2024
                    LocalTime dummyRequestedTime = LocalTime.of(13, 53, 52);  // Example time: 13:53:52
                    System.out.println(dummyRequestedDate);
                    System.out.println(dummyRequestedTime);
                    ServiceReport report = new ServiceReport(
                        packageService.getService().getServiceID(),
                        packageService.getService().getServiceName(),
                        packageService.getSubscriptionPackage().getPackageName(),
                        calculatedFrequency,
                        packageService.getService().getFrequencyUnit(),
                        0,
                        "Not Completed",
                        null,false,packageService.getPackageServicesID(),dummyRequestedDate,dummyRequestedTime,null
                    );
                 // Avoid adding duplicate services to the list
                    if (!serviceReports.contains(report)) {
                        serviceReports.add(report);
                    }
                }
            }
        } 
        // Handle ala-carte services
        if (alaCarteServiceID != 0) {
            SubscriberAlaCarteServices alaCarteService = alaCarteServicesRepository.findById(alaCarteServiceID)
                    .orElseThrow(() -> new RuntimeException("Ala-carte service not found"));
            LocalDate dummyRequestedDate = LocalDate.of(2024, 9, 20); // Example date: September 20, 2024
            LocalTime dummyRequestedTime = LocalTime.of(13, 53, 52);  // Example time: 13:53:52
  //          System.out.println(dummyRequestedDate);
 //           System.out.println(dummyRequestedTime);
            LocalDate date=convertToLocalDate(alaCarteService.getServiceDate());
            LocalTime time=convertToLocalTime(alaCarteService.getServiceTime());
            System.out.println(alaCarteService.getServiceDate());
            System.out.println(alaCarteService.getServiceTime());
 //           System.out.println(alaCarteService.getService().getServiceID() + alaCarteService.getService().getServiceName());
            ServiceReport alaCarteServiceReport = new ServiceReport(
                alaCarteService.getService().getServiceID(),
                alaCarteService.getService().getServiceName(),
                null,
                1,
                "Single",
                0,
                "Not Completed",
                null,true,null,date,time,alaCarteService.getSubscriberAlaCarteServicesID()
            );
            
            System.out.println();
            // Avoid adding duplicate ala-carte service
            if (!serviceReports.contains(alaCarteServiceReport)) {
                serviceReports.add(alaCarteServiceReport);
            }
        }
        // Store the service reports in cache and in-memory
        subscriberServiceMap.put(subscriberID, serviceReports);
        inMemoryServiceTracker.startTracking(subscriberID, serviceReports);
        Map<String, List<ServiceReport>> services = new HashMap<>();
        services.put("allServices", serviceReports);
        return services;
    }
    
    @CachePut(value = "subscriberServicesCache", key = "#subscriberID")
    @Transactional
    public ServiceReport updateServiceRequestedDateTime(int subscriberID, int serviceID, boolean isAlaCarte, LocalDate newRequestedDate, LocalTime newRequestedTime) throws Exception {
        ServiceReport serviceReport = null;

        if (isAlaCarte) {
            // Fetch the ala-carte service
            SubscriberAlaCarteServices alaCarteService = alaCarteServicesRepository.findBySubscriberIDAndServiceID(subscriberID, serviceID);

            if (alaCarteService != null) {
                // Update the ala-carte service with the new requested date and time
    //            alaCarteService.setRequestedDate(newRequestedDate);
    //            alaCarteService.setRequestedTime(newRequestedTime);
                alaCarteServicesRepository.save(alaCarteService); // Save the updated ala-carte service

                // Build the updated ServiceReport
                serviceReport = new ServiceReport();
                serviceReport.setServiceID(alaCarteService.getService().getServiceID());
                serviceReport.setServiceName(alaCarteService.getService().getServiceName());
                serviceReport.setRequestedDate(newRequestedDate);
                serviceReport.setRequestedTime(newRequestedTime);
                serviceReport.setCompletionStatus("In Progress");
                serviceReport.setAlaCarte(true);
            }
        } else {
            // Fetch the subscriber's packageID first
            Subscriber subscriber = subscriberRepository.findById(subscriberID).orElseThrow(() -> new Exception("Subscriber not found"));

            // Use the packageID to find the corresponding PackageServices
            PackageServices packageService = packageServiceRepository.findByPackageIDAndServiceID(subscriber.getSubscriptionPackage().getPackageID(), serviceID);

            if (packageService != null) {
                // Build the updated ServiceReport for this package service
                serviceReport = new ServiceReport();
                serviceReport.setServiceID(packageService.getService().getServiceID());
                serviceReport.setServiceName(packageService.getService().getServiceName());
                serviceReport.setRequestedDate(newRequestedDate);
                serviceReport.setRequestedTime(newRequestedTime);
                serviceReport.setCompletionStatus("In Progress");
                serviceReport.setAlaCarte(false);  // Mark it as a package service
            } else {
                throw new Exception("Package service not found for package ID: " + subscriber.getSubscriptionPackage().getPackageID() + " and service ID: " + serviceID);
            }
        }

        // If no service was found, throw an exception
        if (serviceReport == null) {
            throw new Exception("Service not found for subscriber with ID: " + subscriberID + " and service ID: " + serviceID);
        }

        // Return the updated service report
        return serviceReport;
    }

   
    @CachePut(value = "subscriberServicesCache", key = "#subscriberID")
    public Map<String, List<ServiceReport>> updateServiceCompletion(Integer subscriberID, Integer serviceID, Integer subscriberAlaCarteServicesID, boolean isAlaCarteService) {
        // Retrieve the services for the subscriber from the in-memory map
        System.out.println("subscriberID: " + subscriberID);

        List<ServiceReport> services = subscriberServiceMap.get(subscriberID);
        System.out.println("Retrieved services from in-memory map: " + services);

        // If services are not found, return null or throw an appropriate exception
        if (services == null || services.isEmpty()) {
            System.out.println("No services found for subscriber ID: " + subscriberID);
            return null;
        }

        boolean serviceUpdated = false;

        // Iterate over the services (both package and ala-carte)
        for (ServiceReport service : services) {
            // Check if the serviceID matches first
            if (service.getServiceID() == serviceID) {
                System.out.println("Found matching serviceID: " + serviceID);

                // Handle the case for ala-carte service first
                if (isAlaCarteService && service.isAlaCarte() && service.getSubscriberAlaCarteServicesID().equals(subscriberAlaCarteServicesID)) {
                    System.out.println("Found matching ala-carte service type for serviceID: " + serviceID);
                    serviceUpdated = updateAlaCarteService(service);
                    // Don't return early here, keep looping to update other services (like package service)
                }

                // Handle the case for package service
                else if (!isAlaCarteService && service.getPackageServiceID() != null && service.getPackageServiceID() != 0) {
                    System.out.println("Handling package service update for serviceID: " + serviceID);
                    serviceUpdated = updatePackageService(service);
                    // Continue to update other services if necessary
                }
            }
        }

        // If the service was updated, return the updated map for caching
        if (serviceUpdated) {
            // Put the updated services in the cache and return all services
            Map<String, List<ServiceReport>> updatedServices = new HashMap<>();
            updatedServices.put("allServices", services);  // Return all services, including both package and ala-carte
            return updatedServices;
        }

        // If no service was found with the given serviceID and type
        System.out.println("Service not found for the subscriber.");
        return null;
    }

    private boolean updateAlaCarteService(ServiceReport service) {
        // Check if the ala-carte service has already reached the frequency limit
        if (service.getCompletions() >= service.getFrequencyCount()) {
            System.out.println("Ala-carte Service: " + service.getServiceName() + " is already completed.");
            return false; // Prevent further updates if the service is already completed
        }

        // Increment completions
        int newCompletions = service.getCompletions() + 1;
        service.setCompletions(newCompletions);

        // Recalculate the pending count and frequency count
        service.setPending(service.getFrequencyCount() - newCompletions);

        // Check if the ala-carte service is fully completed
        if (newCompletions >= service.getFrequencyCount()) {
            service.setCompletionStatus("Completed");
            service.setCompletionDate(LocalDateTime.now()); // Set the completion date
            System.out.println("Ala-carte Service: " + service.getServiceName() + " marked as completed on " + service.getCompletionDate());
        } else {
            service.setCompletionStatus("In Progress");
            System.out.println("Ala-carte Service: " + service.getServiceName() + " updated with completions: " + newCompletions);
        }

        return true;  // Service was updated successfully
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
    	
    	System.out.println(subscriberID);
        System.out.println("Retrieving services for subscriber ID: " + subscriberID);

        // Try to retrieve services from in-memory map
        List<ServiceReport> serviceReports = subscriberServiceMap.get(subscriberID);
        System.out.println("serviceReports"+serviceReports);
        // If services are not found in memory, fall back to database
        if (serviceReports == null || serviceReports.isEmpty()) {
            // This is where a cache miss would occur, so log it
            System.out.println("Cache miss for subscriber ID: " + subscriberID);

            // Fetch all interactions from the database for the subscriber (handling multiple services)
            List<Interaction> interactions = interactionRepository.findBySubscriberID(subscriberID);

            if (interactions == null || interactions.isEmpty()) {
  //              throw new RuntimeException("No services found for subscriber with ID: " + subscriberID);
            }

            // Convert each interaction to a ServiceReport (handle multiple services)
            serviceReports = convertInteractionsToServiceReports(interactions);

            // Repopulate the in-memory map and cache with the fetched data
            subscriberServiceMap.put(subscriberID, serviceReports);

            // Cache the result (repopulate cache after cache miss)
            Map<String, List<ServiceReport>> services = new HashMap<>();
            services.put("allServices", serviceReports);

            return services;
        }

        // If the services are already cached in-memory, return them
        Map<String, List<ServiceReport>> cachedServices = new HashMap<>();
        cachedServices.put("allServices", serviceReports);

        return cachedServices;
    }

 // Utility method to convert a list of Interactions to ServiceReport objects (handles multiple services)
    private List<ServiceReport> convertInteractionsToServiceReports(List<Interaction> interactions) {
        List<ServiceReport> serviceReports = new ArrayList<>();

        for (Interaction interaction : interactions) {
            ServiceReport serviceReport = new ServiceReport();
            
            // Handle Ala-carte services
            if (interaction.getSubscriberAlaCarteServices() != null) {
                serviceReport.setServiceID(interaction.getSubscriberAlaCarteServices().getSubscriberAlaCarteServicesID());
                serviceReport.setServiceName(interaction.getSubscriberAlaCarteServices().getService().getServiceName()); // Assuming there's a service name field in SubscriberAlaCarteServices
            }
            // Handle Package-based services (if packageID or packageServiceID is involved)
            else if (interaction.getPackageServices() != null) {
     //           serviceReport.setServiceID(interaction.getPackageServices().getService().getServiceID()); // Assuming Interaction contains packageServiceID
     //           serviceReport.setServiceName("Package Service " + interaction.getPackageServices().getService().getServiceName()); // Use a meaningful name or fetch from the service table
            } else {
                // If neither ala-carte nor package is present, skip this interaction or handle appropriately
                continue;
            }
            
            // Set the completion status based on interaction
  //          serviceReport.setCompletionStatus(interaction.getCompletionStatus() == 1 ? "Completed" : "In Progress");
  //          serviceReport.setCompletionDate(interaction.getLastUpdatedDate());

            // Add the constructed ServiceReport to the list
            serviceReports.add(serviceReport);
        }
        return serviceReports; // Returns list of service reports representing all services availed by the subscriber
    }

    @CachePut(value = "subscriberServicesCache", key = "#subscriberID")
    @Transactional
    public Map<String, List<ServiceReport>> rebuildAllServices(int subscriberID) {
        // Fetch all relevant interactions for the subscriber
        List<Interaction> interactions = interactionRepository.findBySubscriberID(subscriberID);

     // Fetch all ala-carte services the subscriber has purchased
        List<SubscriberAlaCarteServices> alaCarteServices = alaCarteServicesRepository.findBySubscriberID(subscriberID);

        // Initialize a list to hold ServiceReports
        List<ServiceReport> serviceReports = new ArrayList<>();

        // Track completed services by their service ID
        Set<Integer> completedServiceIds = new HashSet<>();

        // Loop through interactions and build ServiceReports
        for (Interaction interaction : interactions) {
            ServiceReport serviceReport = new ServiceReport();

            if (interaction.getSubscriberAlaCarteServices() != null) {
                // Handle Ala-carte services
                SubscriberAlaCarteServices alaCarteService = interaction.getSubscriberAlaCarteServices();
                serviceReport.setServiceID(alaCarteService.getService().getServiceID());
                serviceReport.setServiceName(alaCarteService.getService().getServiceName());
                serviceReport.setPackageName("Ala-carte");
                serviceReport.setAlaCarte(true);
                serviceReport.setSubscriberAlaCarteServicesID(alaCarteService.getSubscriberAlaCarteServicesID());

                // Set requested date and time
                serviceReport.setRequestedDate(convertToLocalDate(alaCarteService.getServiceDate()));
                serviceReport.setRequestedTime(convertToLocalTime(alaCarteService.getServiceTime()));

                // For ala-carte services, completions are typically one-time
                serviceReport.setCompletions(1); // Since each completion is a separate row
                serviceReport.setCompletionStatus("Completed");
                serviceReport.setCompletionDate(LocalDateTime.now()); // Set the completion date for Ala-carte services
                serviceReport.setFrequencyCount(1); // Ala-carte services typically have a frequency of 1
                serviceReport.setPending(0); // Completed means no pending

                completedServiceIds.add(alaCarteService.getService().getServiceID());

            } else if (interaction.getPackageServices() != null) {
                // Handle package services
                PackageServices packageService = interaction.getPackageServices();
                serviceReport.setServiceID(packageService.getService().getServiceID());
                serviceReport.setServiceName(packageService.getService().getServiceName());
                serviceReport.setPackageName(packageService.getSubscriptionPackage().getPackageName());
                serviceReport.setAlaCarte(false);
                serviceReport.setPackageServiceID(packageService.getPackageServicesID());
                serviceReport.setFrequency(packageService.getFrequency());
                System.out.println(packageService.getService().getFrequencyUnit());
                serviceReport.setFrequencyUnit(packageService.getService().getFrequencyUnit());
                // Set requested date and time (if available)
                serviceReport.setRequestedDate(LocalDate.now());  // Use real data if available
                serviceReport.setRequestedTime(LocalTime.now());

                // Set completions and status
                int completions = countCompletionsForService(packageService.getPackageServicesID(), false);
                serviceReport.setCompletions(completions);

                // Set the frequency count from the package service
                int frequencyCount = packageService.getFrequency();
                System.out.println("--"+frequencyCount);
                serviceReport.setFrequencyCount(frequencyCount);

                // Calculate pending completions
                int pending = frequencyCount - completions;
                serviceReport.setPending(Math.max(pending, 0)); // Ensure no negative pending value

                // Set completion status and completion date
                if (completions >= frequencyCount) {
                    serviceReport.setCompletionStatus("Completed");
                    serviceReport.setCompletionDate(LocalDateTime.now()); // Set completion date as today if status is "Completed"
                } else {
                    serviceReport.setCompletionStatus("In Progress");
                    serviceReport.setCompletionDate(null); // No completion date if still in progress
                }

                completedServiceIds.add(packageService.getService().getServiceID());
            }

            // Add the constructed ServiceReport to the list
            serviceReports.add(serviceReport);
        }

        // Handle Subscriber Ala-carte Services (if they haven't been completed)
        for (SubscriberAlaCarteServices alaCarteService : alaCarteServices) {
            if (!completedServiceIds.contains(alaCarteService.getService().getServiceID())) {
                // Service has not been completed, so we need to add it to the service report
                ServiceReport serviceReport = new ServiceReport();
                serviceReport.setServiceID(alaCarteService.getService().getServiceID());
                serviceReport.setServiceName(alaCarteService.getService().getServiceName());
                serviceReport.setPackageName("Ala-carte");
                serviceReport.setAlaCarte(true);
                serviceReport.setSubscriberAlaCarteServicesID(alaCarteService.getSubscriberAlaCarteServicesID());
                serviceReport.setFrequencyUnit(alaCarteService.getService().getFrequencyUnit());
                // Set requested date and time
                serviceReport.setRequestedDate(convertToLocalDate(alaCarteService.getServiceDate()));
                serviceReport.setRequestedTime(convertToLocalTime(alaCarteService.getServiceTime()));

                // For ala-carte services that haven't been completed yet
                serviceReport.setCompletions(0); // No completions yet
                serviceReport.setCompletionStatus("Not Started");
                serviceReport.setCompletionDate(null); // No completion date since it's not started
                serviceReport.setFrequencyCount(1); // Frequency for ala-carte services is typically 1
                serviceReport.setPending(1); // One pending completion

                // Add this service to the serviceReports list
                serviceReports.add(serviceReport);
            }
        }

        // Add remaining services from the package that haven't been completed
        addRemainingServices(subscriberID, completedServiceIds, serviceReports);

     // Store the service reports in cache and in-memory
        subscriberServiceMap.put(subscriberID, serviceReports);
        inMemoryServiceTracker.startTracking(subscriberID, serviceReports);
 //       Map<String, List<ServiceReport>> services = new HashMap<>();
        // Create a map structure similar to what is stored in Memcached
        Map<String, List<ServiceReport>> allServicesMap = new HashMap<>();
        allServicesMap.put("allServices", serviceReports);

        // Return the rebuilt data
        return allServicesMap;
    }

    public int countCompletionsForService(int serviceID, boolean isAlaCarte) {
        if (isAlaCarte) {
            return interactionRepository.countAlaCarteCompletions(serviceID);
        } else {
            return interactionRepository.countPackageCompletions(serviceID);
        }
    }

    public void addRemainingServices(int subscriberID, Set<Integer> completedServiceIds, List<ServiceReport> serviceReports) {
        // Fetch the subscriber and retrieve the packageID
        Subscriber subscriber = subscriberRepository.findById(subscriberID)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberID));
        
        Integer packageID = subscriber.getSubscriptionPackage().getPackageID();  // Assuming subscriber has a field for packageID
        
        // Fetch all services associated with the packageID
        List<PackageServices> packageServices = packageServiceRepository.findServicesByPackageId(packageID);

        for (PackageServices packageService : packageServices) {
            // Check if this service has already been completed
        	
        	
            if (!completedServiceIds.contains(packageService.getService().getServiceID())) {
                // Service has not been completed, so we need to add it to the service report
                ServiceReport serviceReport = new ServiceReport();
                serviceReport.setServiceID(packageService.getService().getServiceID());
                serviceReport.setServiceName(packageService.getService().getServiceName());
                serviceReport.setPackageName(packageService.getSubscriptionPackage().getPackageName());
                serviceReport.setAlaCarte(false);
                serviceReport.setPackageServiceID(packageService.getPackageServicesID());

                // Set default completion status and values for remaining services
                serviceReport.setCompletions(0);
                serviceReport.setCompletionStatus("Not Started");

                // Set frequency and pending values for services that haven't been started yet
                int frequencyCount = packageService.getService().getFrequency();
                System.out.println("frequencyCount"+frequencyCount);
                serviceReport.setFrequencyCount(frequencyCount);
                serviceReport.setFrequency(frequencyCount);
                serviceReport.setPending(frequencyCount); // Since none of the completions have started, pending = frequencyCount
                serviceReport.setFrequencyUnit(packageService.getService().getFrequencyUnit());
                serviceReport.setRequestedDate(LocalDate.now());  // You can use real data if available
                serviceReport.setRequestedTime(LocalTime.now());

                // Add this service to the serviceReports list
                serviceReports.add(serviceReport);
            }
        }
    }

    public static LocalDate convertToLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            date = new java.util.Date(date.getTime());  // Convert java.sql.Date to java.util.Date
        }
        return date.toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toLocalDate();
    }
    
    public static LocalTime convertToLocalTime(Date date) {
        return Instant.ofEpochMilli(date.getTime())  // Convert to Instant from Date
                      .atZone(ZoneId.systemDefault()) // Adjust to the system's default time zone
                      .toLocalTime();                 // Extract the time part as LocalTime
    }
        
}
