package service;

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
}
