package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import model.Interaction;
import model.dto.SubscriberServiceDetailsDTO;

import java.util.List;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Integer> {

//    @Query("SELECT i.subscriberAlaCarteServices, i.completionStatus FROM Interaction i WHERE i.subscriber.subscriberID = :subscriberID")
//    List<Object[]> findAlaCarteServicesWithCompletionStatusAndServiceDetails(Integer subscriberID);
    
	@Query("SELECT new model.dto.SubscriberServiceDetailsDTO(s.subscriberID, s.serviceID, s.serviceDate, s.serviceTime, s.billingStatus, s.createdDate, s.lastUpdatedDate, s.isAccepted, s.SubscriberAlaCarteServicesID, i.completionStatus, sv.serviceName, s.isPackageService) " +
		       "FROM SubscriberAlaCarteServices s " +
		       "LEFT JOIN Interaction i ON s.SubscriberAlaCarteServicesID = i.subscriberAlaCarteServices.SubscriberAlaCarteServicesID " +
		       "JOIN AlaCarteService sv ON s.serviceID = sv.serviceID " +
		       "WHERE s.subscriberID = :subscriberID")
		List<SubscriberServiceDetailsDTO> findAlaCarteServicesWithCompletionStatusAndServiceDetails(Integer subscriberID);

	// Define a method to fetch all interactions by the subscriber ID
    List<Interaction> findBySubscriberID(Integer subscriberId);
    
    // Custom query to count completions for Ala-carte services
    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.subscriberAlaCarteServices.service.serviceID = :serviceID")
    int countAlaCarteCompletions(@Param("serviceID") int serviceID);

    // Custom query to count completions for Package services
    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.packageServices.service.serviceID = :serviceID")
    int countPackageCompletions(@Param("serviceID") int serviceID);

}
