package service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import model.PackageServices;
import model.ServiceReport;
import model.SubscriberAlaCarteServices;
import repository.PackageServiceRepository;
import repository.SubscriberAlaCarteServicesRepository;
import repository.SubscriberRepository;
/*
@Service
public class ServiceCompletionService {

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

    // Method to complete a service by marking it in the report
    public void completeService(Integer subscriberId, Integer serviceId) {
        Optional<PackageServices> optionalService = packageServiceRepository.findById(serviceId);

        if (optionalService.isPresent()) {
            PackageServices service = optionalService.get();

            // Create a ServiceReport and mark the service as completed
            ServiceReport report = new ServiceReport(
                service.getService().getServiceID(),
                service.getService().getServiceName(),
                service.getSubscriptionPackage().getPackageName(),
                service.getFrequency(),
                service.getFrequencyUnit(),
                1,  // Mark 1 completion initially
                "Completed",  // Mark the completion status
                LocalDateTime.now(),true ,service.getPackageServicesID() // Set the completion date
            );

            // Add the report to the in-memory storage
            subscriberServiceMap.computeIfAbsent(subscriberId, k -> new ArrayList<>()).add(report);
        } else {
            System.out.println("Service not found with ID: " + serviceId);
        }
    }

    @Cacheable(value = "subscriberServicesCache", key = "#subscriberId")
    public Map<String, List<ServiceReport>> getSubscriberServices(Integer subscriberId) {
        System.out.println("Retrieving services for subscriber ID: " + subscriberId);

        // Retrieve the service reports from the in-memory map
        List<ServiceReport> serviceReports = subscriberServiceMap.get(subscriberId);

        if (serviceReports == null || serviceReports.isEmpty()) {
            throw new RuntimeException("No services found for subscriber with ID: " + subscriberId);
        }

        // Prepare the response map
        Map<String, List<ServiceReport>> services = new HashMap<>();
        services.put("packageServices", serviceReports);

        return services;
    }

    // Method to track a package purchase, update both the in-memory map and cache
/*    @CachePut(value = "subscriberServicesCache", key = "#subscriberId")
    public Map<String, List<ServiceReport>> trackSubscriberPackage(Long subscriberId, int packageId) {
        System.out.println("Tracking package for subscriber ID: " + subscriberId);

        List<PackageServices> packageServices = packageServiceRepository.findServicesByPackageId(packageId);

        if (packageServices == null || packageServices.isEmpty()) {
            System.out.println("No package services found for packageId: " + packageId);
            return null;
        }

        List<ServiceReport> serviceReports = new ArrayList<>();
        for (PackageServices packageService : packageServices) {
            ServiceReport report = new ServiceReport(
                packageService.getService().getServiceID(),
                packageService.getService().getServiceName(),
                packageService.getSubscriptionPackage().getPackageName(),
                packageService.getFrequency(),
                packageService.getFrequencyUnit(),
                0,
                "Not Completed",
                null
            );
            serviceReports.add(report);
        }

        // Store the serviceReports in cache
        subscriberServiceMap.put(subscriberId, serviceReports);
        inMemoryServiceTracker.startTracking(subscriberId, serviceReports);

        Map<String, List<ServiceReport>> services = new HashMap<>();
        services.put("packageServices", serviceReports);
        return services;
    }
*/
/*
    // Update the service completion for a subscriber
    @CachePut(value = "subscriberServicesCache", key = "#subscriberId")
    public Map<String, List<ServiceReport>> updateServiceCompletion(Integer subscriberId, Integer serviceId) {
        List<ServiceReport> services = subscriberServiceMap.get(subscriberId);
        System.out.println("Retrieved services from in-memory map: " + services);
        if (services != null) {
            for (ServiceReport service : services) {
                if (service.getServiceID() == serviceId) {
                    // Check if the service has already reached the frequency limit
                    if (service.getCompletions() >= service.getFrequency()) {
                        System.out.println("Service: " + service.getServiceName() + " is already completed.");
                        return null; // Prevent further updates
                    }

                    // Increment completions
                    int newCompletions = service.getCompletions() + 1;
                    service.setCompletions(newCompletions);
                    // Check if the service is fully completed
                    if (newCompletions >= service.getFrequency()) {
                        service.setCompletionStatus("Completed");
                        service.setCompletionDate(LocalDateTime.now()); // Set the completion date
                        System.out.println("Service: " + service.getServiceName() + " marked as completed on " + service.getCompletionDate());
                    } else {
                        service.setCompletionStatus("In Progress");
                        System.out.println("Service: " + service.getServiceName() + " updated with completions: " + newCompletions);
                    }

                    // Return the updated service map for caching
                    Map<String, List<ServiceReport>> updatedServices = new HashMap<>();
                    updatedServices.put("packageServices", services);
                    return updatedServices;
                }
            }
        }

        return null;  // Return null if the service is not found
    }


    @CachePut(value = "subscriberServicesCache", key = "#subscriberId")
    public Map<String, List<ServiceReport>> trackSubscriberServices(Integer subscriberId, int packageId, int alaCarteServiceId) {
        System.out.println("Tracking services for subscriber ID: " + subscriberId);

        List<ServiceReport> serviceReports = new ArrayList<>();

        // Handle package services if packageId is provided
        if (packageId != 0) {
            List<PackageServices> packageServices = packageServiceRepository.findServicesByPackageId(packageId);

            if (packageServices == null || packageServices.isEmpty()) {
                System.out.println("No package services found for packageId: " + packageId);
            } else {
                for (PackageServices packageService : packageServices) {
                    ServiceReport report = new ServiceReport(
                        packageService.getService().getServiceID(),
                        packageService.getService().getServiceName(),
                        packageService.getSubscriptionPackage().getPackageName(),
                        packageService.getFrequency(),
                        packageService.getFrequencyUnit(),
                        0,
                        "Not Completed",
                        null,false,packageService.getPackageServicesID()
                    );
                    serviceReports.add(report);
                }
            }
        }

        // Handle ala-carte service if alaCarteServiceId is provided
        if (alaCarteServiceId != 0) {
            SubscriberAlaCarteServices alaCarteService = alaCarteServicesRepository.findById(alaCarteServiceId)
                    .orElseThrow(() -> new RuntimeException("Ala-carte service not found"));

            ServiceReport alaCarteServiceReport = new ServiceReport(
                alaCarteService.getService().getServiceID(),
                alaCarteService.getService().getServiceName(),  // Fetch the service name from the associated AlaCarteService
                "Ala-carte",
                1, // Assuming ala-carte services are one-time or based on specific frequency
                "Single", // Can change based on how you track frequency for ala-carte
                0,
                "Not Completed",
                null,true,0
            );

            serviceReports.add(alaCarteServiceReport);
        }

        // Store the serviceReports in cache and in-memory tracking
        subscriberServiceMap.put(subscriberId, serviceReports);
        inMemoryServiceTracker.startTracking(subscriberId, serviceReports);

        Map<String, List<ServiceReport>> services = new HashMap<>();
        services.put("allServices", serviceReports);
        return services;
    }

}
*/