package service;

import model.dto.SubscriberDTO;
import model.dto.SubscriberSaathiDTO;
import model.AdminUser;
import model.Subscriber;

import java.util.List;
import java.util.Optional;

public interface SubscriberService {

    // Create a new subscriber
    SubscriberDTO createSubscriber(SubscriberDTO subscriberDTO);

    // Update an existing subscriber
    SubscriberDTO updateSubscriber(int subscriberId, SubscriberDTO subscriberDTO);

    // Fetch a subscriber by ID
    SubscriberDTO getSubscriberById(int subscriberId);

    // Get all subscribers
    List<SubscriberDTO> getAllSubscribers();

    // Delete a subscriber by ID
    void deleteSubscriber(int subscriberId);

    // Get active subscribers
    List<SubscriberDTO> getActiveSubscribers();

    // Find a subscriber by email and password
    Subscriber findByEmailAndPassword(String email, String password);

    // Get subscribers assigned to a specific Saathi (AdminUser)
    List<SubscriberDTO> getSubscribersBySaathi(int saathiId);

    List<SubscriberDTO> getSubscribersBySaathiID(int saathiId) ;
    // Fetch Saathi (AdminUser) details of a subscriber by subscriber ID
    AdminUser getSubscriberDetails(int subscriberId);

    // Convert a subscriber entity to a DTO
    SubscriberDTO convertToSubscriberDTO(Subscriber subscriber);

    // Assign a Saathi (AdminUser) to a subscriber
    SubscriberDTO assignSaathiToSubscriber(int subscriberId, int saathiId, String reasonForChange);

    // Get subscribers who do not have a Saathi assigned
    List<Subscriber> getSubscribersWithoutSaathi();

    // Get subscribers who  have a Saathi assigned
    List<SubscriberSaathiDTO> getSubscribersWithSaathi();

    // Convert a DTO to a subscriber entity
    Subscriber convertToEntity(SubscriberDTO subscriberDTO, boolean isPasswordRequired);

    // Get the associated package service ID for a subscriber
    Integer getPackageIDBySubscriber(Integer subscriberId);

    // Check if a subscriber exists by their ID
    boolean subscriberExists(Integer subscriberID);
    
    Integer getAdminUserIDBySubscriber(Integer subscriberID);
    int verifyOtp(String email, String otp); 
    SubscriberDTO completeRegistration(String email, SubscriberDTO additionalDetails); 
}
