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

	// Corrected Query to get services with no corresponding interaction
	@Query("SELECT s, i.completionStatus, sv.serviceName FROM SubscriberAlaCarteServices s " +
	       "LEFT JOIN Interaction i ON s.SubscriberAlaCarteServicesID = i.subscriberAlaCarteServices.SubscriberAlaCarteServicesID " +
	       "JOIN AlaCarteService sv ON s.serviceID = sv.serviceID " +
	       "WHERE s.subscriber.subscriberID = :subscriberID")
	List<Object[]> findAlaCarteServicesWithCompletionStatusAndServiceDetails(@Param("subscriberID") Integer subscriberID);

	// Find services by the `Subscriber` entity
	List<SubscriberAlaCarteServices> findBySubscriber(Subscriber subscriber);

	// Corrected method to find by `subscriberID` and `serviceID`
	Optional<SubscriberAlaCarteServices> findBySubscriber_SubscriberIDAndServiceID(Integer subscriberId, Integer serviceId);

	// Find all ala-carte services for a specific subscriber, using `SubscriberID`
	List<SubscriberAlaCarteServices> findBySubscriber_SubscriberID(int subscriberID);

	// Query to find subscribers with untracked ala-carte services
	@Query("SELECT s.subscriber.subscriberID FROM SubscriberAlaCarteServices s " +
	       "WHERE s.SubscriberAlaCarteServicesID NOT IN (SELECT i.subscriberAlaCarteServices.SubscriberAlaCarteServicesID FROM Interaction i)")
	List<Integer> findSubscribersWithUntrackedAlaCarteServices();

	// Count the total number of ala-carte services for a given subscriber
	@Query("SELECT COUNT(s) FROM SubscriberAlaCarteServices s WHERE s.subscriber.subscriberID = :subscriberID")
	int countAlaCarteServicesBySubscriber(@Param("subscriberID") Integer subscriberID);

	// Find ala-carte services by `subscriberID` and `serviceID` (optional if still needed)
	SubscriberAlaCarteServices findBySubscriber_SubscriberIDAndServiceID(int subscriberID, int serviceID);
}
