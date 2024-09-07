package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import model.PackageServices;
import model.SubscriptionPackage;

@Repository
public interface PackageServiceRepository extends JpaRepository<PackageServices, Integer> {
	// Define the method to find PackageServices by SubscriptionPackage
    List<PackageServices> findBySubscriptionPackage(SubscriptionPackage subscriptionPackage);
    
}