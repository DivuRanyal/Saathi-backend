package service.impl;

import model.PackageServices;
import model.Subscriber;
import repository.PackageServiceRepository;
import repository.SubscriberRepository;
import service.PackageServicesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PackageServicesServiceImpl implements PackageServicesService {

    @Autowired
    private PackageServiceRepository packageServicesRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    // Fetch all package services
    @Override
    public List<PackageServices> getAllPackageServices() {
        return packageServicesRepository.findAll();
    }

    // Fetch a specific package service by its ID
    @Override
    public PackageServices getPackageServicesById(Integer id) {
        Optional<PackageServices> packageService = packageServicesRepository.findById(id);
        return packageService.orElse(null); // Return the entity if found, or null if not
    }

    // Create a new PackageService
    @Override
    public PackageServices createPackageService(PackageServices packageServices) {
        return packageServicesRepository.save(packageServices); // Save and return the newly created entity
    }

    // Update an existing PackageService
    @Override
    public PackageServices updatePackageService(Integer id, PackageServices packageServices) {
        Optional<PackageServices> existingPackageService = packageServicesRepository.findById(id);

        if (existingPackageService.isPresent()) {
            PackageServices updatedService = existingPackageService.get();
            updatedService.setSubscriptionPackage(packageServices.getSubscriptionPackage());
            updatedService.setService(packageServices.getService());
            updatedService.setFrequency(packageServices.getFrequency());
           
            updatedService.setStatus(packageServices.getStatus());
            updatedService.setCreatedBy(packageServices.getCreatedBy());
            updatedService.setUpdatedBy(packageServices.getUpdatedBy());
            updatedService.setCreatedDate(packageServices.getCreatedDate());
            updatedService.setLastUpdatedDate(packageServices.getLastUpdatedDate());
            return packageServicesRepository.save(updatedService); // Save the updated entity
        }
        return null; // Return null if the package service is not found
    }

    // Delete a PackageService by its ID
    @Override
    public void deletePackageService(Integer id) {
        packageServicesRepository.deleteById(id); // Delete the entity by its ID
    }
/*    
    @Override
    public Integer getPackageServiceID(Integer subscriberID, Integer serviceID) {
        // First, fetch the Subscriber
        Subscriber subscriber = subscriberRepository.findById(subscriberID).orElse(null);

        if (subscriber == null || subscriber.getSubscriptionPackage() == null) {
            // Handle cases where the subscriber or subscription package is null
            return null;
        }

        // Now fetch the packageID from the subscriber's subscription package
        Integer packageID = subscriber.getSubscriptionPackage().getPackageID();

        // Use the packageID to find the packageServiceID
        return packageServicesRepository.findPackageServicesIDByPackageIDAndServiceID(packageID, serviceID);
    }
  */  
}
