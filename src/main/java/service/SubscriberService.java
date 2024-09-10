package service;

import model.dto.SubscriberDTO;
import model.AdminUser;
import model.Subscriber;

import java.util.List;

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
    
    // Fetch Saathi (AdminUser) details of a subscriber by subscriber ID
    AdminUser getSubscriberDetails(int subscriberId);
    
    // Convert a subscriber entity to a DTO
    SubscriberDTO convertToSubscriberDTO(Subscriber subscriber);
    
    // Assign a Saathi (AdminUser) to a subscriber
    SubscriberDTO assignSaathiToSubscriber(int subscriberId, int saathiId);
    
    // Get subscribers who do not have a Saathi assigned
    List<Subscriber> getSubscribersWithoutSaathi();
    
    // Convert a DTO to a subscriber entity
    Subscriber convertToEntity(SubscriberDTO subscriberDTO, boolean isPasswordRequired);
    
    Integer getPackageServiceIDBySubscriber(Long subscriberId);
}
