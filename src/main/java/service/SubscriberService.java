package service;

import model.AdminUser;
import model.Subscriber;
import model.dto.SubscriberDTO;
import java.util.List;

public interface SubscriberService {
    SubscriberDTO createSubscriber(SubscriberDTO subscriberDTO);
    SubscriberDTO updateSubscriber(int subscriberId, SubscriberDTO subscriberDTO);
    SubscriberDTO getSubscriberById(int subscriberId);
    List<SubscriberDTO> getAllSubscribers();
    void deleteSubscriber(int subscriberId);
     List<SubscriberDTO> getActiveSubscribers();
    Subscriber findByEmailAndPassword(String email, String password);
    List<SubscriberDTO> getSubscribersBySaathi(int saathiId);
    AdminUser getSubscriberDetails(int subscriberId);
    SubscriberDTO convertToSubscriberDTO(Subscriber subscriber);
    SubscriberDTO assignSaathiToSubscriber(int subscriberId, int saathiId);
    List<Subscriber> getSubscribersWithoutSaathi();
}