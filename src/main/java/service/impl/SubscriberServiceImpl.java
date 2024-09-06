package service.impl;

import model.dto.SubscriberDTO;
import model.AdminUser;
import model.Subscriber;
import model.SubscriptionPackage;
import repository.AdminUsersRepository;
import repository.SubscriberRepository;
import repository.SubscriptionPackageRepository;
import service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import exception.EmailAlreadyRegisteredException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriberServiceImpl implements SubscriberService {

	@Autowired
	private AdminUsersRepository adminUserRepository;
	@Autowired
	private  SubscriptionPackageRepository subscriptionPackageRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public SubscriberDTO createSubscriber(SubscriberDTO subscriberDTO) {
    	// Check if the email is already registered
        Optional<Subscriber> existingSubscriber = subscriberRepository.findByEmail(subscriberDTO.getEmail());
        
        if (existingSubscriber.isPresent()) {
        	throw new EmailAlreadyRegisteredException("The email address is already registered.");
        }

        // If the email is not registered, proceed to create a new subscriber
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
            // Check if the incoming password is different from the existing password
            if (!passwordEncoder.matches(subscriberDTO.getPassword(), existingSubscriber.getPassword())) {
                existingSubscriber.setPassword(passwordEncoder.encode(subscriberDTO.getPassword()));
            }
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
        if (subscriberDTO.getSaathiID() != null) {
            AdminUser saathi = adminUserRepository.findById(subscriberDTO.getSaathiID())
                    .orElseThrow(() -> new RuntimeException("AdminUser not found with ID: " + subscriberDTO.getSaathiID()));
            existingSubscriber.setSaathi(saathi);
        }
		if (subscriberDTO.getPackageID() != null) {
			SubscriptionPackage subscriptionPackage = subscriptionPackageRepository
					.findById(subscriberDTO.getPackageID()).orElseThrow(() -> new RuntimeException(
							"Subscription Package not found with ID: " + subscriberDTO.getPackageID()));
			existingSubscriber.setSubscriptionPackage(subscriptionPackage);
		}
        
        Subscriber updatedSubscriber = subscriberRepository.save(existingSubscriber);
        return convertToDTO(updatedSubscriber);
    }

 //   @Override
    public SubscriberDTO getSubscriberById(int subscriberId) {
    	// Fetch the Subscriber from the database
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberId));

        SubscriberDTO dto = new SubscriberDTO();
        dto.setSubscriberID(subscriber.getSubscriberId());
        dto.setFirstName(subscriber.getFirstName());
        dto.setLastName(subscriber.getLastName());
        dto.setEmail(subscriber.getEmail());
        dto.setContactNo(subscriber.getContactNo());
        dto.setCountryCode(subscriber.getCountryCode());
        dto.setStartDate(subscriber.getStartDate());
        dto.setEndDate(subscriber.getEndDate());
        dto.setBillingStatus(subscriber.getBillingStatus());
        dto.setStatus(subscriber.getStatus());

        // Set subscription package details
        SubscriptionPackage subscriptionPackage = subscriber.getSubscriptionPackage();
        if (subscriptionPackage != null) {
            dto.setPackageName(subscriptionPackage.getPackageName());
          
        }

        return dto;
    }


    @Override
    public List<SubscriberDTO> getAllSubscribers() {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        return subscribers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubscriberDTO> getActiveSubscribers() {
        List<Subscriber> subscribers = subscriberRepository.findByStatus(1);
        return subscribers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
    }
    @Override
    public void deleteSubscriber(int subscriberId) {
        subscriberRepository.deleteById(subscriberId);
    }

    @Override
    public Subscriber findByEmailAndPassword(String email, String rawPassword) {
        // Use Optional to safely handle the possibility that the subscriber does not exist
        Optional<Subscriber> optionalSubscriber = subscriberRepository.findByEmail(email);

        if (optionalSubscriber.isPresent()) {
            Subscriber subscriber = optionalSubscriber.get();

            // Check if the raw password matches the encoded password
            if (passwordEncoder.matches(rawPassword, subscriber.getPassword())) {
                // Update lastLoginTime with the current date and time
                subscriber.setLastLoginTime(new Date());
                return subscriber;
            }
        }

        return null; // Return null if credentials are invalid or subscriber does not exist
    }
    // Utility methods to convert between entity and DTO
    private SubscriberDTO convertToDTO(Subscriber subscriber) {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setSubscriberID(subscriber.getSubscriberId());
        subscriberDTO.setFirstName(subscriber.getFirstName());
        subscriberDTO.setLastName(subscriber.getLastName());
        subscriberDTO.setEmail(subscriber.getEmail());
        subscriberDTO.setContactNo(subscriber.getContactNo());
        subscriberDTO.setCountryCode(subscriber.getCountryCode());
        subscriberDTO.setPassword(subscriber.getPassword());
        subscriberDTO.setLastLoginTime(subscriber.getLastLoginTime());
        subscriberDTO.setPackageID(subscriber.getSubscriptionPackage() != null ? subscriber.getSubscriptionPackage().getPackageID() : null);
        subscriberDTO.setStartDate(subscriber.getStartDate());
        subscriberDTO.setEndDate(subscriber.getEndDate());
        // Safely handling BillingStatus in case it's null
      
        Integer billingStatus = subscriber.getBillingStatus();
        subscriberDTO.setBillingStatus(billingStatus != null ? billingStatus : null);
     // Correctly mapping SaathiID from AdminUser's UserID
        subscriberDTO.setSaathiID(subscriber.getSaathi() != null ? subscriber.getSaathi().getAdminUserID() : null);

        subscriberDTO.setCreatedDate(subscriber.getCreatedDate());
        subscriberDTO.setLastUpdatedDate(subscriber.getLastUpdatedDate());
        subscriberDTO.setStatus(subscriber.getStatus());
        
        return subscriberDTO;
    }

    private Subscriber convertToEntity(SubscriberDTO subscriberDTO) throws RuntimeException {
        Subscriber subscriber = new Subscriber();
        subscriber.setFirstName(subscriberDTO.getFirstName());
        subscriber.setLastName(subscriberDTO.getLastName());
        subscriber.setEmail(subscriberDTO.getEmail());
        subscriber.setContactNo(subscriberDTO.getContactNo());
        subscriber.setCountryCode(subscriberDTO.getCountryCode());
        subscriber.setPassword(passwordEncoder.encode(subscriberDTO.getPassword()));
        
        subscriber.setLastLoginTime(subscriberDTO.getLastLoginTime());
        subscriber.setStartDate(subscriberDTO.getStartDate());
        subscriber.setEndDate(subscriberDTO.getEndDate());
        subscriber.setBillingStatus(subscriberDTO.getBillingStatus());
        // Fetch the SubscriptionPackage entity by packageId and set it
        if (subscriberDTO.getPackageID() != null) {
            SubscriptionPackage subscriptionPackage = subscriptionPackageRepository.findById(subscriberDTO.getPackageID())
                .orElseThrow(() -> new RuntimeException("Subscription Package not found"));
            subscriber.setSubscriptionPackage(subscriptionPackage);
        }

        // Fetch the AdminUser entity by saathiId and set it
        if (subscriberDTO.getSaathiID() != null) {
            AdminUser saathi = adminUserRepository.findById(subscriberDTO.getSaathiID())
                .orElseThrow(() -> new RuntimeException("Admin User (Saathi) not found"));
            subscriber.setSaathi(saathi);
        }
        subscriber.setStatus(subscriberDTO.getStatus());
        // Additional fields can be set here
        return subscriber;
    }
    @Override
    public List<SubscriberDTO> getSubscribersBySaathi(int saathiId) {
        AdminUser saathi = adminUserRepository.findById(saathiId)
            .orElseThrow(() -> new RuntimeException("Saathi not found with ID: " + saathiId));
        
        List<Subscriber> subscribers = subscriberRepository.findBySaathi(saathi);
        return subscribers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public AdminUser getSubscriberDetails(int subscriberId) {
        // Fetch the subscriber from the repository or throw an exception if not found
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
            .orElseThrow(() -> new RuntimeException("Subscriber not found"));

        // Get the Saathi (AdminUser) associated with the subscriber
        AdminUser saathi = subscriber.getSaathi();

        // Return only Saathi details, or null if Saathi is not assigned
        return saathi;
    }
    
    @Override
    public SubscriberDTO assignSaathiToSubscriber(int subscriberId, int saathiId) {

        // Fetch the Subscriber from the database
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberId));

        // Fetch the Saathi from the database
        AdminUser saathi = adminUserRepository.findById(saathiId)
                .orElseThrow(() -> new RuntimeException("Saathi not found with ID: " + saathiId));

        // Assign the Saathi to the Subscriber
        subscriber.setSaathi(saathi);

        // Save the updated subscriber to the database
        Subscriber updatedSubscriber = subscriberRepository.save(subscriber);

        // Convert the updated Subscriber entity to DTO
        SubscriberDTO updatedSubscriberDTO = convertToSubscriberDTO(updatedSubscriber);
        return updatedSubscriberDTO;
    }

    @Override
    public SubscriberDTO convertToSubscriberDTO(Subscriber subscriber) {
        SubscriberDTO subscriberDTO = new SubscriberDTO();

        // Basic fields mapping
        subscriberDTO.setSubscriberID(subscriber.getSubscriberId()); // Mapping the ID
        subscriberDTO.setFirstName(subscriber.getFirstName()); // First name
        subscriberDTO.setLastName(subscriber.getLastName()); // Last name
        subscriberDTO.setEmail(subscriber.getEmail()); // Email
        subscriberDTO.setContactNo(subscriber.getContactNo()); // Phone number
        subscriberDTO.setCountryCode(subscriber.getCountryCode()); // Country code
        subscriberDTO.setPassword(subscriber.getPassword()); // Password

        // Handling package ID
        subscriberDTO.setPackageID(subscriber.getSubscriptionPackage() != null ? subscriber.getSubscriptionPackage().getPackageID() : null);

        // Handling dates (assuming they are stored as Date objects in the Subscriber entity)
        subscriberDTO.setStartDate(subscriber.getStartDate() != null ? subscriber.getStartDate().toString() : null);
        subscriberDTO.setEndDate(subscriber.getEndDate() != null ? subscriber.getEndDate().toString() : null);
        subscriberDTO.setCreatedDate(subscriber.getCreatedDate());
        subscriberDTO.setLastUpdatedDate(subscriber.getLastUpdatedDate());

        // Billing status
        subscriberDTO.setBillingStatus(subscriber.getBillingStatus());

        // Saathi (AdminUser) Mapping
        if (subscriber.getSaathi() != null) {
            subscriberDTO.setSaathiID(subscriber.getSaathi().getAdminUserID()); // Saathi ID
            subscriberDTO.setSaathi(subscriber.getSaathi()); // Saathi object
        }

        // Handling status
        subscriberDTO.setStatus(subscriber.getStatus());

        return subscriberDTO;
    }

    @Override
    public List<Subscriber> getSubscribersWithoutSaathi() {
        return subscriberRepository.findSubscribersWithoutSaathi();
    }
}