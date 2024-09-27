package repository;

import model.AlaCarteService;
import model.Subscriber;
import model.SubscriberAlaCarteServices;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberAlaCarteServicesRepository extends JpaRepository<SubscriberAlaCarteServices, Integer> {

	// Query to get services with no corresponding interaction
/*	@Query("SELECT s, i.completionStatus, sv FROM SubscriberAlaCarteServices s " +
	           "LEFT JOIN Interaction i ON s.SubscriberAlaCarteServicesID = i.subscriberAlaCarteServices.SubscriberAlaCarteServicesID " +
	           "JOIN Services sv ON s.serviceID = sv.serviceID " +
	           "WHERE s.subscriberID = :subscriberID")
	    List<Object[]> findAlaCarteServicesWithCompletionStatusAndServiceDetails(Integer subscriberID); */
	
	
	@Query("SELECT s, i.completionStatus, sv FROM SubscriberAlaCarteServices s " +
	           "LEFT JOIN Interaction i ON s.SubscriberAlaCarteServicesID = i.subscriberAlaCarteServices.SubscriberAlaCarteServicesID " +
	           "JOIN AlaCarteService sv ON s.serviceID = sv.serviceID " +
	           "WHERE s.subscriberID = :subscriberID")
	    List<Object[]> findAlaCarteServicesWithCompletionStatusAndServiceDetails(@Param("subscriberID")  Integer subscriberID);
	   
	 
	 // Query using the 'subscriber' field
	    List<SubscriberAlaCarteServices> findBySubscriber(Subscriber subscriber);
	    Optional<SubscriberAlaCarteServices> findBySubscriberIDAndServiceID(Integer subscriberId,  Integer serviceId);
	    
	    // Method to fetch all ala-carte services for a specific subscriber
	    List<SubscriberAlaCarteServices> findBySubscriberID(int subscriberID);
	    
	    @Query("SELECT s.subscriber.subscriberID FROM SubscriberAlaCarteServices s WHERE s.SubscriberAlaCarteServicesID NOT IN (SELECT i.subscriberAlaCarteServices.SubscriberAlaCarteServicesID FROM Interaction i)")
	    List<Integer> findSubscribersWithUntrackedAlaCarteServices();

	    // Method to count the total number of ala-carte services for a given subscriber
	    @Query("SELECT COUNT(s) FROM SubscriberAlaCarteServices s WHERE s.subscriberID = :subscriberID")
	    int countAlaCarteServicesBySubscriber(@Param("subscriberID") Integer subscriberID);

	    SubscriberAlaCarteServices findBySubscriberIDAndServiceID(int subscriberID, int serviceID);
	    
	    }

