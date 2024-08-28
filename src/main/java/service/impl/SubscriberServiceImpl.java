package service.impl;

import model.dto.SubscriberDTO;
import model.AdminUser;
import model.Subscriber;
import repository.SubscriberRepository;
import service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Override
    public SubscriberDTO createSubscriber(SubscriberDTO subscriberDTO) {
        Subscriber subscriber = convertToEntity(subscriberDTO);
        Subscriber savedSubscriber = subscriberRepository.save(subscriber);
        return convertToDTO(savedSubscriber);
    }

    @Override
    public SubscriberDTO updateSubscriber(int subscriberId, SubscriberDTO subscriberDTO) {
        Subscriber existingSubscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberId));

        if (subscriberDTO.getFirstName() != null) {
            existingSubscriber.setFirstName(subscriberDTO.getFirstName());
        }
        if (subscriberDTO.getLastName() != null) {
            existingSubscriber.setLastName(subscriberDTO.getLastName());
        }
        if (subscriberDTO.getEmail() != null) {
            existingSubscriber.setEmail(subscriberDTO.getEmail());
        }
        if (subscriberDTO.getContactNo() != null) {
            existingSubscriber.setContactNo(subscriberDTO.getContactNo());
        }
        if (subscriberDTO.getCountryCode() != null) {
            existingSubscriber.setCountryCode(subscriberDTO.getCountryCode());
        }
        if (subscriberDTO.getPassword() != null) {
            existingSubscriber.setPassword(subscriberDTO.getPassword());
        }
        if (subscriberDTO.getLastLoginTime() != null) {
            existingSubscriber.setLastLoginTime(subscriberDTO.getLastLoginTime());
        }
        if (subscriberDTO.getStartDate() != null) {
            existingSubscriber.setStartDate(subscriberDTO.getStartDate());
        }
        if (subscriberDTO.getEndDate() != null) {
            existingSubscriber.setEndDate(subscriberDTO.getEndDate());
        }
        if (subscriberDTO.getBillingStatus() != null) {
            existingSubscriber.setBillingStatus(subscriberDTO.getBillingStatus());
        }
        if (subscriberDTO.getStatus() != null) {
            existingSubscriber.setStatus(subscriberDTO.getStatus());
        }

        Subscriber updatedSubscriber = subscriberRepository.save(existingSubscriber);
        return convertToDTO(updatedSubscriber);
    }

    @Override
    public SubscriberDTO getSubscriberById(int subscriberId) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberId));
        return convertToDTO(subscriber);
    }

    @Override
    public List<SubscriberDTO> getAllSubscribers() {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        return subscribers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSubscriber(int subscriberId) {
        subscriberRepository.deleteById(subscriberId);
    }

    @Override
    public Subscriber findByEmailAndPassword(String email, String password) {
        return subscriberRepository.findByEmailAndPassword(email, password);
    }
    // Utility methods to convert between entity and DTO
    private SubscriberDTO convertToDTO(Subscriber subscriber) {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setSubscriberId(subscriber.getSubscriberId());
        subscriberDTO.setFirstName(subscriber.getFirstName());
        subscriberDTO.setLastName(subscriber.getLastName());
        subscriberDTO.setEmail(subscriber.getEmail());
        subscriberDTO.setContactNo(subscriber.getContactNo());
        subscriberDTO.setCountryCode(subscriber.getCountryCode());
        subscriberDTO.setPassword(subscriber.getPassword());
        subscriberDTO.setLastLoginTime(subscriber.getLastLoginTime());
        subscriberDTO.setPackageId(subscriber.getSubscriptionPackage() != null ? subscriber.getSubscriptionPackage().getPackageID() : null);
        subscriberDTO.setStartDate(subscriber.getStartDate());
        subscriberDTO.setEndDate(subscriber.getEndDate());
        subscriberDTO.setBillingStatus(subscriber.getBillingStatus());
     // Correctly mapping SaathiID from AdminUser's UserID
        subscriberDTO.setSaathiId(subscriber.getSaathi() != null ? subscriber.getSaathi().getAdminUserID() : null);

        subscriberDTO.setCreatedDate(subscriber.getCreatedDate());
        subscriberDTO.setLastUpdatedDate(subscriber.getLastUpdatedDate());
        subscriberDTO.setStatus(subscriber.getStatus());
        return subscriberDTO;
    }

    private Subscriber convertToEntity(SubscriberDTO subscriberDTO) {
        Subscriber subscriber = new Subscriber();
        subscriber.setFirstName(subscriberDTO.getFirstName());
        subscriber.setLastName(subscriberDTO.getLastName());
        subscriber.setEmail(subscriberDTO.getEmail());
        subscriber.setContactNo(subscriberDTO.getContactNo());
        subscriber.setCountryCode(subscriberDTO.getCountryCode());
        subscriber.setPassword(subscriberDTO.getPassword());
        subscriber.setLastLoginTime(subscriberDTO.getLastLoginTime());
        subscriber.setStartDate(subscriberDTO.getStartDate());
        subscriber.setEndDate(subscriberDTO.getEndDate());
        subscriber.setBillingStatus(subscriberDTO.getBillingStatus());
        subscriber.setStatus(subscriberDTO.getStatus());
        // Additional fields can be set here
        return subscriber;
    }
}
