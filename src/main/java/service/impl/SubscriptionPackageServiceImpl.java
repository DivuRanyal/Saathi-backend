package service.impl;

import model.SubscriptionPackage;
import repository.SubscriptionPackageRepository;
import service.SubscriptionPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionPackageServiceImpl implements SubscriptionPackageService {

    private final SubscriptionPackageRepository subscriptionPackageRepository;

    @Autowired
    public SubscriptionPackageServiceImpl(SubscriptionPackageRepository subscriptionPackageRepository) {
        this.subscriptionPackageRepository = subscriptionPackageRepository;
    }

    @Override
    public List<SubscriptionPackage> findAll() {
        return subscriptionPackageRepository.findAll();
    }

    @Override
    public Optional<SubscriptionPackage> findById(int packageID) {
        return subscriptionPackageRepository.findById(packageID);
    }

    @Override
    public SubscriptionPackage save(SubscriptionPackage subscriptionPackage) {
        return subscriptionPackageRepository.save(subscriptionPackage);
    }

    @Override
    public SubscriptionPackage update(int packageID, SubscriptionPackage subscriptionPackage) {
        Optional<SubscriptionPackage> existingPackageOptional = subscriptionPackageRepository.findById(packageID);

        if (existingPackageOptional.isPresent()) {
            SubscriptionPackage existingPackage = existingPackageOptional.get();

            // Update only the fields that are not null or have been modified
            if (subscriptionPackage.getPackageName() != null) {
                existingPackage.setPackageName(subscriptionPackage.getPackageName());
            }
            if (subscriptionPackage.getPackageDescription() != null) {
                existingPackage.setPackageDescription(subscriptionPackage.getPackageDescription());
            }
            if (subscriptionPackage.getPrice() != null && subscriptionPackage.getPrice().compareTo(BigDecimal.ZERO) != 0) {
                existingPackage.setPrice(subscriptionPackage.getPrice());
            }
            if (subscriptionPackage.getStatus() != 0) {
                existingPackage.setStatus(subscriptionPackage.getStatus());
            }
         // Set the updatedBy field
            existingPackage.setUpdatedBy(subscriptionPackage.getUpdatedBy());

            return subscriptionPackageRepository.save(existingPackage);
        } else {
            throw new RuntimeException("Package with ID " + packageID + " not found.");
        }
    }

    @Override
    public void deleteById(int packageID) {
        subscriptionPackageRepository.deleteById(packageID);
    }
    
    @Override
    public SubscriptionPackage getSubscriptionPackageById(int packageID) {
        Optional<SubscriptionPackage> optionalPackage = subscriptionPackageRepository.findById(packageID);

        if (optionalPackage.isPresent()) {
        	
            return optionalPackage.get(); // Return the actual SubscriptionPackage object
        } else {
            throw new RuntimeException("Package with ID " + packageID + " not found.");
        }
    }
}
