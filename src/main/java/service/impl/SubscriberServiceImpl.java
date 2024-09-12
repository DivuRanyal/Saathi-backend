package service.impl;

import model.dto.CreditCardDTO;
import model.dto.PatronDTO;
import model.dto.SubscriberDTO;
import model.AdminUser;
import model.CreditCard;
import model.Subscriber;
import model.PackageServices;
import model.Patron;
import repository.AdminUsersRepository;
import repository.CreditCardRepository;
import repository.PackageServiceRepository;
import repository.PatronRepository;
import repository.SubscriberRepository;
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
    private CreditCardRepository creditCardRepository;

    @Autowired
    private PackageServiceRepository packageServiceRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private PatronRepository patronRepository;
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
        Subscriber subscriber = convertToEntity(subscriberDTO, true);
        Subscriber savedSubscriber = subscriberRepository.save(subscriber);
        return convertToDTO(savedSubscriber);
    }

    @Override
    public SubscriberDTO updateSubscriber(int subscriberId, SubscriberDTO subscriberDTO) {
        Subscriber existingSubscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberId));

        // Update the fields of the subscriber
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
        if (subscriberDTO.getComments() != null) {
            existingSubscriber.setComments(subscriberDTO.getComments());
        }
        if (subscriberDTO.getSaathiID() != null) {
            AdminUser saathi = adminUserRepository.findById(subscriberDTO.getSaathiID())
                    .orElseThrow(() -> new RuntimeException("AdminUser not found with ID: " + subscriberDTO.getSaathiID()));
            existingSubscriber.setSaathi(saathi);
        }

        // Handle PackageService (instead of SubscriptionPackage)
        if (subscriberDTO.getPackageServiceID() != null) {
            PackageServices packageServices = packageServiceRepository.findById(subscriberDTO.getPackageServiceID())
                    .orElseThrow(() -> new RuntimeException("PackageServices not found with ID: " + subscriberDTO.getPackageServiceID()));
            existingSubscriber.setPackageServices(packageServices);
        }

     // Handle CreditCard
        if (subscriberDTO.getCreditCard() != null) {
            CreditCardDTO creditCardDTO = subscriberDTO.getCreditCard();

            // Assuming a subscriber can have one credit card; you can also modify for multiple credit cards if needed.
            CreditCard creditCard = existingSubscriber.getCreditCard() != null ? existingSubscriber.getCreditCard() : new CreditCard();
            
            creditCard.setNameOnCard(creditCardDTO.getNameOnCard());
            creditCard.setCreditCardNumber(creditCardDTO.getCreditCardNumber());
            creditCard.setExpiryDate(creditCardDTO.getExpiryDate());
            creditCard.setCvv(creditCardDTO.getCvv());
            creditCard.setSubscriber(existingSubscriber);

            creditCardRepository.save(creditCard);  // Save or update credit card details
        }
        Subscriber updatedSubscriber = subscriberRepository.save(existingSubscriber);
        return convertToDTO(updatedSubscriber);
    }

    @Override
    public SubscriberDTO getSubscriberById(int subscriberId) {
        // Fetch the Subscriber from the database
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberId));

        // Convert the entity to DTO
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
        Optional<Subscriber> optionalSubscriber = subscriberRepository.findByEmail(email);

        if (optionalSubscriber.isPresent()) {
            Subscriber subscriber = optionalSubscriber.get();

            if (passwordEncoder.matches(rawPassword, subscriber.getPassword())) {
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
        subscriberDTO.setComments(subscriber.getComments());
        // Set PackageService ID
        if (subscriber.getPackageServices() != null) {
        	subscriberDTO.setPackageServiceID(subscriber.getPackageServices().getPackageServicesID());
        	
            // Fetch and set the packageName from SubscriptionPackage
            if (subscriber.getPackageServices().getSubscriptionPackage() != null) {
            	subscriberDTO.setPackageName(subscriber.getPackageServices().getSubscriptionPackage().getPackageName());
            	subscriberDTO.setPriceINR(subscriber.getPackageServices().getSubscriptionPackage().getPriceINR());
            	subscriberDTO.setPriceUSD(subscriber.getPackageServices().getSubscriptionPackage().getPriceUSD());
            }
        } 
        // Credit Card Mapping
        if (subscriber.getCreditCard() != null) {
            CreditCardDTO creditCardDTO = new CreditCardDTO();
            creditCardDTO.setNameOnCard(subscriber.getCreditCard().getNameOnCard());
            creditCardDTO.setCreditCardNumber(subscriber.getCreditCard().getCreditCardNumber());
            creditCardDTO.setExpiryDate(subscriber.getCreditCard().getExpiryDate());
            creditCardDTO.setCvv(subscriber.getCreditCard().getCvv());
            subscriberDTO.setCreditCard(creditCardDTO);
        }
        subscriberDTO.setStartDate(subscriber.getStartDate());
        subscriberDTO.setEndDate(subscriber.getEndDate());
        subscriberDTO.setBillingStatus(subscriber.getBillingStatus());
        subscriberDTO.setCreatedDate(subscriber.getCreatedDate());
        subscriberDTO.setLastUpdatedDate(subscriber.getLastUpdatedDate());
        subscriberDTO.setStatus(subscriber.getStatus());
        // Handle Saathi (AdminUser)
        if (subscriber.getSaathi() != null) {
            subscriberDTO.setSaathiID(subscriber.getSaathi().getAdminUserID());
            subscriberDTO.setSaathi(subscriber.getSaathi());
        }

         return subscriberDTO;
    }

    @Override
    public Subscriber convertToEntity(SubscriberDTO subscriberDTO, boolean isPasswordRequired) {
        Subscriber subscriber = new Subscriber();
        // Set the basic details from the DTO
        subscriber.setFirstName(subscriberDTO.getFirstName());
        subscriber.setLastName(subscriberDTO.getLastName());
        subscriber.setEmail(subscriberDTO.getEmail());
        subscriber.setContactNo(subscriberDTO.getContactNo());
        subscriber.setCountryCode(subscriberDTO.getCountryCode());
        // Handle password setting
        if (isPasswordRequired) {
            if (subscriberDTO.getPassword() == null || subscriberDTO.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            subscriber.setPassword(passwordEncoder.encode(subscriberDTO.getPassword()));
        } else {
            subscriber.setPassword(subscriberDTO.getPassword());
        }
        subscriber.setLastLoginTime(subscriberDTO.getLastLoginTime());
        subscriber.setStartDate(subscriberDTO.getStartDate());
        subscriber.setEndDate(subscriberDTO.getEndDate());
        subscriber.setBillingStatus(subscriberDTO.getBillingStatus());
        subscriber.setComments(subscriberDTO.getComments());
        // Handle PackageServices relationship
        if (subscriberDTO.getPackageServiceID() != null) {
            PackageServices packageServices = packageServiceRepository.findById(subscriberDTO.getPackageServiceID())
                    .orElseThrow(() -> new RuntimeException("PackageServices not found with ID: " + subscriberDTO.getPackageServiceID())); 
            // Set the package services for the subscriber
            subscriber.setPackageServices(packageServices);
            // Fetch the associated SubscriptionPackage and set the packageName in the DTO
            if (packageServices.getSubscriptionPackage() != null) {
            	System.out.println(packageServices.getSubscriptionPackage().getPackageName());
                subscriberDTO.setPackageName(packageServices.getSubscriptionPackage().getPackageName());
                subscriberDTO.setPriceINR(packageServices.getSubscriptionPackage().getPriceINR());
                subscriberDTO.setPriceUSD(packageServices.getSubscriptionPackage().getPriceUSD());
            }
        }
        
        // Handle Saathi (AdminUser) relationship
        if (subscriberDTO.getSaathiID() != null) {
            AdminUser saathi = adminUserRepository.findById(subscriberDTO.getSaathiID())
                    .orElseThrow(() -> new RuntimeException("Admin User (Saathi) not found with ID: " + subscriberDTO.getSaathiID()));
            subscriber.setSaathi(saathi);
        }
        // Set the status from the DTO
        subscriber.setStatus(subscriberDTO.getStatus());
     // Mapping the CreditCard
        if (subscriberDTO.getCreditCard() != null) {
            CreditCardDTO creditCardDTO = subscriberDTO.getCreditCard();
            // Create a new CreditCard entity or update the existing one
            CreditCard creditCard = subscriber.getCreditCard() != null ? subscriber.getCreditCard() : new CreditCard();
            creditCard.setNameOnCard(creditCardDTO.getNameOnCard());
            creditCard.setCreditCardNumber(creditCardDTO.getCreditCardNumber());
            creditCard.setExpiryDate(creditCardDTO.getExpiryDate());
            creditCard.setCvv(creditCardDTO.getCvv());
            // Set the relationship with the subscriber
            creditCard.setSubscriber(subscriber);

            // Persist the credit card details
            creditCardRepository.save(creditCard);

            subscriber.setCreditCard(creditCard);  // Set the credit card to the subscriber entity
        }
        return subscriber;
    }

    @Override
    public List<SubscriberDTO> getSubscribersBySaathi(int saathiId) {
        // Fetch the Saathi (AdminUser)
        AdminUser saathi = adminUserRepository.findById(saathiId)
                .orElseThrow(() -> new RuntimeException("Saathi not found with ID: " + saathiId));

        // Fetch the subscribers for the given Saathi
        List<Subscriber> subscribers = subscriberRepository.findBySaathi(saathi);

        // Map each subscriber to SubscriberDTO, including PatronDTO
        return subscribers.stream().map(subscriber -> {
            SubscriberDTO subscriberDTO = mapToSubscriberDTO(subscriber);

            // Fetch patron details for the subscriber using the correct repository method
            Optional<Patron> optionalPatron = patronRepository.findFirstBySubscriber_SubscriberID(subscriber.getSubscriberId());
            if (optionalPatron.isPresent()) {
                PatronDTO patronDTO = mapToPatronDTO(optionalPatron.get());
                subscriberDTO.setPatron(patronDTO);
            }
            
            return subscriberDTO;
        }).collect(Collectors.toList()); // This collects the DTOs into a list
    }

    private PatronDTO mapToPatronDTO(Patron patron) {
        PatronDTO dto = new PatronDTO();
        dto.setPatronID(patron.getPatronID());
        dto.setFirstName(patron.getFirstName());
        dto.setLastName(patron.getLastName());
        dto.setEmail(patron.getEmail());
        dto.setContactNo(patron.getContactNo());
        dto.setAddress1(patron.getAddress1());
        dto.setAddress2(patron.getAddress2());
        dto.setCity(patron.getCity());
        dto.setState(patron.getState());
        dto.setCountry(patron.getCountry());
        dto.setRelation(patron.getRelation());
        dto.setCreatedDate(patron.getCreatedDate());
        return dto;
    }

    @Override
    public AdminUser getSubscriberDetails(int subscriberId) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found"));
        return subscriber.getSaathi(); // Return Saathi details or null if not assigned
    }

    @Override
    public SubscriberDTO assignSaathiToSubscriber(int subscriberId, int saathiId) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberId));
        AdminUser saathi = adminUserRepository.findById(saathiId)
                .orElseThrow(() -> new RuntimeException("Saathi not found with ID: " + saathiId));
        subscriber.setSaathi(saathi);
        Subscriber updatedSubscriber = subscriberRepository.save(subscriber);
        System.out.println(updatedSubscriber.getSaathi().getAdminUserID());
        return convertToDTO(updatedSubscriber);
    }

    @Override
    public List<Subscriber> getSubscribersWithoutSaathi() {
        return subscriberRepository.findSubscribersWithoutSaathi();
    }

	@Override
	public SubscriberDTO convertToSubscriberDTO(Subscriber subscriber) {
		// TODO Auto-generated method stub
		return convertToDTO(subscriber);
	}
	
	public SubscriberDTO mapToSubscriberDTO(Subscriber subscriber) {
	    SubscriberDTO dto = new SubscriberDTO();
	    dto.setSubscriberID(subscriber.getSubscriberId());
	    dto.setFirstName(subscriber.getFirstName());
	    dto.setLastName(subscriber.getLastName());
	    dto.setEmail(subscriber.getEmail());
	    dto.setContactNo(subscriber.getContactNo());
	    dto.setStatus(subscriber.getStatus());

	    // You can selectively set fields for AdminUser (Saathi) instead of returning the whole object
	    AdminUser saathi = subscriber.getSaathi(); // Assuming `saathi` is a field in Subscriber
	    if (saathi != null) {
	        dto.setLastName(saathi.getFirstName() + " " + saathi.getLastName());
	        dto.setEmail(saathi.getEmail());
	    }

	    return dto;
	}

	public Integer getPackageServiceIDBySubscriber(Integer subscriberId) {
	    // Convert Long to Integer safely, assuming the value is within Integer range
/*	    if (subscriberId > Integer.MAX_VALUE || subscriberId < Integer.MIN_VALUE) {
	        throw new IllegalArgumentException("Subscriber ID is out of range for Integer.");
	    }

	    // Cast subscriberId from Long to Integer
	    Integer subscriberIdAsInt = subscriberId.intValue();
*/
	    // Fetch the subscriber using the Integer subscriberId
	    Subscriber subscriber = subscriberRepository.findById(subscriberId)
	        .orElse(null);

	    // Check if the subscriber and their package services exist
	    if (subscriber != null && subscriber.getPackageServices() != null) {
	        return subscriber.getPackageServices().getPackageServicesID();
	    }

	    // Return null if no package service is found
	    return null;
	}

	@Override
	public boolean subscriberExists(Integer subscriberID) {
        return subscriberRepository.existsById(subscriberID);
    }
	
	
}
