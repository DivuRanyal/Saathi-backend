package repository;

import model.AdminUser;
import model.Subscriber;
import model.dto.SubscriberServiceDetailsDTO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {
    // Additional custom queries can be defined here if needed
	
	Subscriber findByEmailAndPassword(String email, String password);
	// You can define custom query methods here if needed, e.g.,
	
	List<Subscriber> findByStatus(Integer status);
	List<Subscriber> findByBillingStatus(Integer billingStatus);
	 Optional<Subscriber> findByEmail(String email);
	 List<Subscriber> findBySaathi(AdminUser saathi);
	 @Query(value = "SELECT * FROM Subscribers WHERE SaathiID IS NULL", nativeQuery = true)
	 List<Subscriber> findSubscribersWithoutSaathiNative();
	 List<Subscriber> findBySaathiIsNull();
	 List<Subscriber> findBySaathiIsNotNull();
	 @Query("SELECT s FROM Subscriber s WHERE s.saathi.adminUserID = :saathiId")
	 List<Subscriber> findSubscribersBySaathiID(@Param("saathiId") int saathiId);

//	 @Query("SELECT s.subscriberID FROM Subscriber s WHERE s.billingStatus = 1 AND s.subscriptionPackage IS NOT NULL")
	 @Query("SELECT s.subscriberID FROM Subscriber s WHERE s.subscriptionPackage IS NOT NULL")
	 List<Integer> findSubscribersWithPurchasedPackages();
	 
	 @Query("SELECT COUNT(s) FROM Subscriber s WHERE s.status = 1 AND s.billingStatus = 0")
	    long countActiveSubscribersWithBillingStatusZero();

	    @Query("SELECT COUNT(s) FROM Subscriber s WHERE s.status = 1 AND s.billingStatus = 1")
	    long countActiveSubscribersWithBillingStatusOne();

	    @Query("SELECT COUNT(s) FROM Subscriber s WHERE s.status = 0")
	    long countInactiveSubscribers();

	    @Query("SELECT s.billingStatus FROM Subscriber s WHERE s.subscriberID = :subscriberID")
	    Integer findBillingStatusBySubscriberID(@Param("subscriberID") int subscriberID);
}
