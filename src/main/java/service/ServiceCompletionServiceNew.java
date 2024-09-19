package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import model.Interaction;
import model.PackageServices;
import model.ServiceReport;
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
                	int calculatedFrequency = packageService.getService().getFrequency() * packageService.getFrequency();

                    ServiceReport report = new ServiceReport(
                        packageService.getService().getServiceID(),
                        packageService.getService().getServiceName(),
                        packageService.getSubscriptionPackage().getPackageName(),
                        calculatedFrequency,
                        packageService.getService().getFrequencyUnit(),
                        0,
                        "Not Completed",
                        null,false,packageService.getPackageServicesID(),null,null
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
            System.out.println(alaCarteService.getService().getServiceID() + alaCarteService.getService().getServiceName());
            ServiceReport alaCarteServiceReport = new ServiceReport(
                alaCarteService.getService().getServiceID(),
                alaCarteService.getService().getServiceName(),
                "Ala-carte",
                1,
                "Single",
                0,
                "Not Completed",
                null,true,0,null,null
            );
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
    public ServiceReport updateServiceRequest(Integer subscriberID, int serviceID, LocalDate requestedDate, LocalTime requestedTime) {
        // Fetch the cached list of services for the subscriber
        List<ServiceReport> allServices = subscriberServiceMap.get(subscriberID);
        ServiceReport updatedReport = null;

        if (allServices != null) {
            // Iterate over the list of all services to find the one that matches the serviceID
            for (ServiceReport report : allServices) {
                if (report.getServiceID() == serviceID) {
                    // Check if the service is ala-carte or package
                    if (report.isAlaCarte()) {
                        System.out.println("Updating requested date/time for an ala-carte service.");
                        // Optionally update in the database as well
      //                  updateAlaCarteServiceInDatabase(subscriberID, serviceID, requestedDate, requestedTime);
                    } else {
                        System.out.println("Updating requested date/time for a package service.");
                        // Optionally update in the database as well
      //                  updatePackageServiceInDatabase(subscriberID, serviceID, requestedDate, requestedTime);
                    }

                    // Update the requested date and time for the cached service report
                    report.setRequestedDate(requestedDate);
                    report.setRequestedTime(requestedTime);
                    updatedReport = report;  // Keep track of the updated report
                    break;  // Break the loop since we found and updated the correct report
                }
            }

            // Update the cache with the modified list of services
            if (updatedReport != null) {
                subscriberServiceMap.put(subscriberID, allServices);  // Update cache with modified list
            } else {
                throw new RuntimeException("Service report not found for subscriber: " + subscriberID + " and serviceID: " + serviceID);
            }
        } else {
            throw new RuntimeException("No cached data found for subscriber: " + subscriberID);
        }

        return updatedReport;
    }

    @CachePut(value = "subscriberServicesCache", key = "#subscriberID")
    public Map<String, List<ServiceReport>> updateServiceCompletion(Integer subscriberID, Integer serviceID, boolean isAlaCarteService) {
        // Retrieve the services for the subscriber from the in-memory map
    	System.out.println("isAlaCarteService"+isAlaCarteService);
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

                // Now, handle the case based on whether it's ala-carte or package service
                if (isAlaCarteService && service.isAlaCarte()) {
                    System.out.println("Found matching service type for serviceID: " + serviceID);
                    // Check if the service has already reached the frequency limit
                    if (service.getCompletions() >= service.getFrequencyCount()) {
                        System.out.println("Service: " + service.getServiceName() + " is already completed.");
                        return null; // Prevent further updates if the service is already completed
                    }

                    // Increment completions
                    int newCompletions = service.getCompletions() + 1;
                    service.setCompletions(newCompletions);

                    // Recalculate the pending count and frequency count
                    service.setFrequencyCount(service.getFrequency());  // Set the correct frequency count based on logic
                    service.setPending(service.getFrequencyCount() - newCompletions);  // Recalculate pending based on completions

                    // Check if the service is fully completed
                    if (newCompletions >= service.getFrequencyCount()) {
                        service.setCompletionStatus("Completed");
                        service.setCompletionDate(LocalDateTime.now()); // Set the completion date
                        System.out.println("Service: " + service.getServiceName() + " marked as completed on " + service.getCompletionDate());
                    } else {
                        service.setCompletionStatus("In Progress");
                        System.out.println("Service: " + service.getServiceName() + " updated with completions: " + newCompletions);
                    }
                    serviceUpdated = true;
                    break;  // Exit the loop as the relevant service has been updated
                } else {
                    // Else block: If it's not ala-carte, handle package services here
                    System.out.println("Service type mismatch for serviceID: " + serviceID + ". Handling it as a package service.");
                    
                    // Handle package services here as the `else` condition
                    if (!isAlaCarteService) {
                        // Update package service completion
                        if (service.getCompletions() >= service.getFrequencyCount()) {
                            System.out.println("Package Service: " + service.getServiceName() + " is already completed.");
                            return null; // Prevent further updates if the service is already completed
                        }

                        // Increment completions for package service
                        int newCompletions = service.getCompletions() + 1;
                        service.setCompletions(newCompletions);

                        // Recalculate the pending count and frequency count for package service
                        service.setFrequencyCount(service.getFrequencyCount());
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

                        serviceUpdated = true;
                        break;
                    } else {
                        // If a service mismatch occurs where both types don't match, log it
                        System.out.println("Service type mismatch: Expected ala-carte or package, but types didn't match.");
                    }
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

    @Cacheable(value = "subscriberServicesCache", key = "#subscriberID")
    public Map<String, List<ServiceReport>> getSubscriberServices(Integer subscriberID) {
        System.out.println("Retrieving services for subscriber ID: " + subscriberID);

        // Try to retrieve services from in-memory map
        List<ServiceReport> serviceReports = subscriberServiceMap.get(subscriberID);

        // If services are not found in memory, fall back to database
        if (serviceReports == null || serviceReports.isEmpty()) {
            // This is where a cache miss would occur, so log it
            System.out.println("Cache miss for subscriber ID: " + subscriberID);
            
            // Fetch all interactions from the database for the subscriber (handling multiple services)
            List<Interaction> interactions = interactionRepository.findBySubscriberID(subscriberID);
            
            if (interactions == null || interactions.isEmpty()) {
                throw new RuntimeException("No services found for subscriber with ID: " + subscriberID);
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

        // Prepare the response map
        Map<String, List<ServiceReport>> services = new HashMap<>();
        services.put("allServices", serviceReports);
        return services;
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
                serviceReport.setServiceID(interaction.getPackageServices().getService().getServiceID()); // Assuming Interaction contains packageServiceID
                serviceReport.setServiceName("Package Service " + interaction.getPackageServices().getService().getServiceName()); // Use a meaningful name or fetch from the service table
            } else {
                // If neither ala-carte nor package is present, skip this interaction or handle appropriately
                continue;
            }
            
            // Set the completion status based on interaction
            serviceReport.setCompletionStatus(interaction.getCompletionStatus() == 1 ? "Completed" : "In Progress");
            serviceReport.setCompletionDate(interaction.getLastUpdatedDate());

            // Add the constructed ServiceReport to the list
            serviceReports.add(serviceReport);
        }

        return serviceReports; // Returns list of service reports representing all services availed by the subscriber
    }

    public Map<String, List<ServiceReport>> rebuildAllServices(int subscriberID) {
        // Fetch all relevant interactions for the subscriber
        List<Interaction> interactions = interactionRepository.findBySubscriberID(subscriberID);

        // Initialize a list to hold ServiceReports
        List<ServiceReport> serviceReports = new ArrayList<>();

        // Loop through interactions and build ServiceReports
        for (Interaction interaction : interactions) {
            ServiceReport serviceReport = new ServiceReport();

            // Check if it's an Ala-carte service or a package service
            if (interaction.getSubscriberAlaCarteServices() != null) {
                // Handle Ala-carte services
                SubscriberAlaCarteServices alaCarteService = interaction.getSubscriberAlaCarteServices();
                serviceReport.setServiceID(alaCarteService.getService().getServiceID());
                serviceReport.setServiceName(alaCarteService.getService().getServiceName());
                serviceReport.setPackageName("Ala-carte");
                serviceReport.setAlaCarte(true); // It's an Ala-carte service
            } else if (interaction.getPackageServices() != null) {
                // Handle package services
                PackageServices packageService = interaction.getPackageServices();
                serviceReport.setServiceID(packageService.getService().getServiceID());
                serviceReport.setServiceName(packageService.getService().getServiceName());
                serviceReport.setPackageName(packageService.getSubscriptionPackage().getPackageName());
                serviceReport.setAlaCarte(false); // It's a package service
            } else {
                // If neither ala-carte nor package is present, skip this interaction or handle appropriately
                continue;
            }

            // Set completion status and date from Interaction data
            serviceReport.setCompletionStatus(interaction.getCompletionStatus() == 1 ? "Completed" : "In Progress");
            serviceReport.setCompletionDate(interaction.getLastUpdatedDate());
            
            // Set the requested date and time from Interaction
            serviceReport.setRequestedDate(interaction.getCreatedDate().toLocalDate());
            serviceReport.setRequestedTime(interaction.getCreatedDate().toLocalTime());

            // Add the constructed ServiceReport to the list
            serviceReports.add(serviceReport);
        }

        // Create a map structure similar to what is stored in Memcached
        Map<String, List<ServiceReport>> allServicesMap = new HashMap<>();
        allServicesMap.put("allServices", serviceReports);

        // Store the rebuilt data in Memcached (if applicable)
        // memcachedClient.set("allServices_" + subscriberID, allServicesMap);

        // Return the rebuilt data
        return allServicesMap;
    }

}
