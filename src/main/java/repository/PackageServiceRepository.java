package repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import model.AlaCarteService;
import model.PackageServices;
import model.SubscriptionPackage;
import model.Subscriber;

@Repository
public interface PackageServiceRepository extends JpaRepository<PackageServices, Integer> {

    // Find PackageServices by SubscriptionPackage
    List<PackageServices> findBySubscriptionPackage(SubscriptionPackage subscriptionPackage);

    // Remove findServicesBySubscriber(int subscriberId)
    
    // Query by the Subscriber entity instead of subscriberId
    List<PackageServices> findServicesBySubscriber(Subscriber subscriber);

    // Derived query based on the relationship field in the PackageServices entity
    List<PackageServices> findBySubscriptionPackage_PackageID(int packageId);

    // JPQL Query to find services by package ID
    @Query("SELECT ps FROM PackageServices ps WHERE ps.subscriptionPackage.packageID = :packageId")
    List<PackageServices> findServicesByPackageId(@Param("packageId") int packageId);
    
    @Query("SELECT ps FROM PackageServices ps WHERE ps.status = 1")
    List<PackageServices> findActivePackageServices();
    
    @Modifying
    @Transactional
    @Query("DELETE FROM PackageServices ps WHERE ps.subscriptionPackage.id = :subscriptionPackageId")
    void deleteAllBySubscriptionPackage(@Param("subscriptionPackageId") Integer subscriptionPackageId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PackageServices ps WHERE ps.id = :id")
    void deleteById( Integer id);

 // Method to count the total services associated with a package for a given subscriber
    @Query("SELECT COUNT(ps) FROM PackageServices ps WHERE ps.subscriptionPackage = (SELECT s.subscriptionPackage FROM Subscriber s WHERE s.subscriberID = :subscriberID)")
    int countServicesBySubscriber(@Param("subscriberID") Integer subscriberID);


}
