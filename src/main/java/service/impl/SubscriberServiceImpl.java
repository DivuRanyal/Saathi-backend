package service.impl;

import model.dto.CreditCardDTO;
import model.dto.PatronDTO;
import model.dto.SubscriberDTO;
import model.AdminUser;
import model.CreditCard;
import model.Subscriber;
import model.SubscriptionPackage;
import model.Patron;
import repository.AdminUsersRepository;
import repository.CreditCardRepository;
import repository.PatronRepository;
import repository.SubscriberRepository;
import repository.SubscriptionPackageRepository;
import service.EmailService;
import service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import exception.EmailAlreadyRegisteredException;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    @Autowired
    private AdminUsersRepository adminUserRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private SubscriptionPackageRepository subscriptionPackageRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private PatronRepository patronRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;


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

            if (subscriberDTO.getStatus() == 1) { // Assuming '1' is the status for active
                AdminUser assignedSaathi = existingSubscriber.getSaathi();
                if (assignedSaathi != null) {
                    String saathiName = assignedSaathi.getFirstName() + " " + assignedSaathi.getLastName();
                    String saathiPhone = assignedSaathi.getContactNo();
                    String saathiEmail = assignedSaathi.getEmail();
                    
                    try {
                        emailService.sendSaathiDetailsEmail(
                            existingSubscriber.getEmail(),
                            existingSubscriber.getFirstName(),
                            saathiName,
                            saathiPhone,
                            saathiEmail
                        );
                    } catch (MessagingException | IOException | TemplateException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (subscriberDTO.getComments() != null) {
            existingSubscriber.setComments(subscriberDTO.getComments());
        }
        
        if (subscriberDTO.getSaathiID() != null) {
            AdminUser saathi = adminUserRepository.findById(subscriberDTO.getSaathiID())
                    .orElseThrow(() -> new RuntimeException("AdminUser not found with ID: " + subscriberDTO.getSaathiID()));
            existingSubscriber.setSaathi(saathi);
        }

        // Corrected part: Use PackageID instead of PackageServiceID
        if (subscriberDTO.getPackageID() != null) {
            SubscriptionPackage subscriptionPackage = subscriptionPackageRepository.findById(subscriberDTO.getPackageID())
                    .orElseThrow(() -> new RuntimeException("SubscriptionPackage not found with ID: " + subscriberDTO.getPackageID()));
            existingSubscriber.setSubscriptionPackage(subscriptionPackage);
        }

        // Handle CreditCard
        if (subscriberDTO.getCreditCard() != null) {
            CreditCardDTO creditCardDTO = subscriberDTO.getCreditCard();
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
        
        if (subscriber.getSubscriptionPackage() != null) {
            subscriberDTO.setPackageID(subscriber.getSubscriptionPackage().getPackageID());
            subscriberDTO.setPackageName(subscriber.getSubscriptionPackage().getPackageName());
            subscriberDTO.setPriceINR(subscriber.getSubscriptionPackage().getPriceINR());
            subscriberDTO.setPriceUSD(subscriber.getSubscriptionPackage().getPriceUSD());
        }

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
        
        if (subscriber.getSaathi() != null) {
            subscriberDTO.setSaathiID(subscriber.getSaathi().getAdminUserID());
            subscriberDTO.setSaathi(subscriber.getSaathi());
        }

        List<PatronDTO> patronDTOs = new ArrayList<>();

        if (subscriber != null && subscriber.getSubscriberId() != 0) {
            List<Patron> patrons = patronRepository.findBySubscriber_SubscriberID(subscriber.getSubscriberId());
            
            if (patrons != null && !patrons.isEmpty()) {
                patronDTOs = patrons.stream()
                    .map(this::mapToPatronDTO)
                    .collect(Collectors.toList());
            }
        }

        // Set the PatronDTOs, either the mapped list or an empty list
        subscriberDTO.setPatrons(patronDTOs);
        return subscriberDTO;
    }

    @Override
    public Subscriber convertToEntity(SubscriberDTO subscriberDTO, boolean isPasswordRequired) {
        Subscriber subscriber = new Subscriber();
        subscriber.setFirstName(subscriberDTO.getFirstName());
        subscriber.setLastName(subscriberDTO.getLastName());
        subscriber.setEmail(subscriberDTO.getEmail());
        subscriber.setContactNo(subscriberDTO.getContactNo());
        subscriber.setCountryCode(subscriberDTO.getCountryCode());
        
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

        if (subscriberDTO.getPackageID() != null) {
            SubscriptionPackage subscriptionPackage = subscriptionPackageRepository.findById(subscriberDTO.getPackageID())
                    .orElseThrow(() -> new RuntimeException("SubscriptionPackage not found with ID: " + subscriberDTO.getPackageID()));
            subscriber.setSubscriptionPackage(subscriptionPackage);  
            // Optionally, if you want to set the package name and prices in the DTO
            subscriberDTO.setPackageName(subscriptionPackage.getPackageName());
            subscriberDTO.setPriceINR(subscriptionPackage.getPriceINR());
            subscriberDTO.setPriceUSD(subscriptionPackage.getPriceUSD());
        }

        if (subscriberDTO.getSaathiID() != null) {
            AdminUser saathi = adminUserRepository.findById(subscriberDTO.getSaathiID())
                    .orElseThrow(() -> new RuntimeException("Admin User (Saathi) not found with ID: " + subscriberDTO.getSaathiID()));
            subscriber.setSaathi(saathi);
        }

        subscriber.setStatus(subscriberDTO.getStatus());

        if (subscriberDTO.getCreditCard() != null) {
            CreditCardDTO creditCardDTO = subscriberDTO.getCreditCard();
            CreditCard creditCard = new CreditCard();
            creditCard.setNameOnCard(creditCardDTO.getNameOnCard());
            creditCard.setCreditCardNumber(creditCardDTO.getCreditCardNumber());
            creditCard.setExpiryDate(creditCardDTO.getExpiryDate());
            creditCard.setCvv(creditCardDTO.getCvv());
            creditCard.setSubscriber(subscriber);
            creditCardRepository.save(creditCard);
            subscriber.setCreditCard(creditCard);
        }

        return subscriber;
    }

    @Override
    public List<SubscriberDTO> getSubscribersBySaathi(int saathiId) {
        AdminUser saathi = adminUserRepository.findById(saathiId)
                .orElseThrow(() -> new RuntimeException("Saathi not found with ID: " + saathiId));

        List<Subscriber> subscribers = subscriberRepository.findBySaathi(saathi);

        return subscribers.stream().map(subscriber -> {
            SubscriberDTO subscriberDTO = mapToSubscriberDTO(subscriber);

            List<Patron> patrons = patronRepository.findBySubscriber_SubscriberID(subscriber.getSubscriberId());
            List<PatronDTO> patronDTOs = patrons.stream()
                .map(this::mapToPatronDTO)
                .collect(Collectors.toList());

            subscriberDTO.setPatrons(patronDTOs);

            return subscriberDTO;
        }).collect(Collectors.toList());
    }

    private PatronDTO mapToPatronDTO(Patron patron) {
        PatronDTO dto = new PatronDTO();
        dto.setPatronID(patron.getPatronID());
        dto.setFirstName(patron.getFirstName());
        dto.setLastName(patron.getLastName());
        dto.setEmail(patron.getEmail());
        dto.setCountryCode(patron.getCountryCode());
        dto.setDob(patron.getDob());
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
        return subscriber.getSaathi();
    }

    @Override
    public SubscriberDTO assignSaathiToSubscriber(int subscriberId, int saathiId) {
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberId));
        AdminUser saathi = adminUserRepository.findById(saathiId)
                .orElseThrow(() -> new RuntimeException("Saathi not found with ID: " + saathiId));
        subscriber.setSaathi(saathi);
        Subscriber updatedSubscriber = subscriberRepository.save(subscriber);
        return convertToDTO(updatedSubscriber);
    }

    @Override
    public List<Subscriber> getSubscribersWithoutSaathi() {
 //       return subscriberRepository.findSubscribersWithoutSaathiNative();
    	return subscriberRepository.findBySaathiIsNull();
    }

    @Override
    public SubscriberDTO convertToSubscriberDTO(Subscriber subscriber) {
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
        dto.setPackageID(subscriber.getSubscriptionPackage().getPackageID());
        dto.setPackageName(subscriber.getSubscriptionPackage().getPackageName());
        AdminUser saathi = subscriber.getSaathi();
        if (saathi != null) {
            dto.setSaathi(saathi);
        }

        return dto;
    }

    @Override
    public boolean subscriberExists(Integer subscriberID) {
        return subscriberRepository.existsById(subscriberID);
    }

    @Override
    public Integer getPackageIDBySubscriber(Integer subscriberId) {
        // Fetch the subscriber from the repository by their ID
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberId));

        // Check if the subscriber has an associated SubscriptionPackage
        if (subscriber.getSubscriptionPackage() != null) {
            // Return the PackageID from the associated SubscriptionPackage
            return subscriber.getSubscriptionPackage().getPackageID();
        } else {
            // Return null or throw an exception if no SubscriptionPackage is found
            return null; // or you could throw new RuntimeException("No subscription package found for this subscriber.");
        }
    }

}
