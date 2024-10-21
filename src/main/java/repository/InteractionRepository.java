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
    
	@Query("SELECT new model.dto.SubscriberServiceDetailsDTO(s.subscriber.subscriberID, s.serviceID, s.serviceDate, s.serviceTime, s.billingStatus, s.createdDate, s.lastUpdatedDate, s.isAccepted, s.SubscriberAlaCarteServicesID, i.completionStatus, sv.serviceName, s.isPackageService) " +
		       "FROM SubscriberAlaCarteServices s " +
		       "LEFT JOIN Interaction i ON s.SubscriberAlaCarteServicesID = i.subscriberAlaCarteServices.SubscriberAlaCarteServicesID " +
		       "JOIN AlaCarteService sv ON s.serviceID = sv.serviceID " +
		       "WHERE s.subscriber.subscriberID = :subscriberID")
		List<SubscriberServiceDetailsDTO> findAlaCarteServicesWithCompletionStatusAndServiceDetails(@Param("subscriberID") Integer subscriberID);

	// Define a method to fetch all interactions by the subscriber ID
    List<Interaction> findBySubscriberID(Integer subscriberId);
    
    // Custom query to count completions for Ala-carte services
    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.subscriberAlaCarteServices.service.serviceID = :serviceID")
    int countAlaCarteCompletions(@Param("serviceID") int serviceID);

    // Custom query to count completions for Package services
    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.packageServices.service.serviceID = :serviceID")
    int countPackageCompletions(@Param("serviceID") int serviceID);

 // Method to count the completed ala-carte services for a given subscriber
    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.subscriberID = :subscriberID AND i.subscriberAlaCarteServices IS NOT NULL AND i.completionStatus = 1")
    int countCompletedAlaCarteServicesBySubscriber(@Param("subscriberID") Integer subscriberID);

    // Method to count the completed package services for a given subscriber
    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.subscriberID = :subscriberID AND i.packageServices IS NOT NULL AND i.completionStatus = 1")
    int countCompletedPackageServicesBySubscriber(@Param("subscriberID") Integer subscriberID);
    
    @Query("SELECT COUNT(i) FROM Interaction i WHERE i.subscriberID = :subscriberID AND i.packageServices.packageServicesID = :packageServicesID AND FUNCTION('MONTH', i.createdDate) = FUNCTION('MONTH', CURRENT_DATE) AND FUNCTION('YEAR', i.createdDate) = FUNCTION('YEAR', CURRENT_DATE)")
    int countBySubscriberIDAndPackageServices_PackageServicesID_CurrentMonth(
      Integer subscriberID, 
        Integer packageServicesID);

 // Query to fetch interactions for a list of subscriber IDs
    @Query("SELECT i FROM Interaction i WHERE i.subscriberID IN :subscriberIDs AND i.serviceRating IS NOT NULL")
    List<Interaction> findInteractionsBySubscriberIDs(List<Integer> subscriberIDs);
    
    // Alternatively, calculate the average rating directly in the query
    @Query("SELECT AVG(i.serviceRating) FROM Interaction i WHERE i.subscriberID IN :subscriberIDs AND i.serviceRating IS NOT NULL")
    Double findAverageRatingBySubscriberIDs(List<Integer> subscriberIDs);
}

