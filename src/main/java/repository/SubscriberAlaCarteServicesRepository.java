package repository;

import model.SubscriberAlaCarteServices;

import java.util.List;

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

}

