package service;

import model.SubscriptionPackage;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPackageService {

    List<SubscriptionPackage> findAll();

    Optional<SubscriptionPackage> findById(int packageID);

    SubscriptionPackage save(SubscriptionPackage subscriptionPackage);

    SubscriptionPackage update(int packageID, SubscriptionPackage subscriptionPackage);

    void deleteById(int packageID);
    
    SubscriptionPackage getSubscriptionPackageById(int packageID);
}
