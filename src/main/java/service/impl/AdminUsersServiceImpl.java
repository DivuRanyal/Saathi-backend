package service.impl;

import model.AdminUser;
import model.Subscriber;
import model.dto.AdminUsersDTO;
import model.dto.SubscriberDTO;
import repository.AdminUsersRepository;
import repository.InteractionRepository;
import repository.SubscriberRepository;
import service.AdminUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import exception.EmailAlreadyRegisteredException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class AdminUsersServiceImpl implements AdminUsersService {

    private final AdminUsersRepository adminUsersRepository;
    
    @Autowired
    private final SubscriberRepository subscriberRepository;
    
    @Autowired
    private final InteractionRepository interactionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public AdminUsersServiceImpl(AdminUsersRepository adminUsersRepository) {
        this.adminUsersRepository = adminUsersRepository;
		this.subscriberRepository = null;
		this.interactionRepository = null;
    }

    @Override
    public List<AdminUser> getAllAdminUsers() {
        return adminUsersRepository.findAll();
    }

    @Override
    public Optional<AdminUser> getAdminUserById(int userId) {
        return adminUsersRepository.findById(userId);
    }

    public AdminUser createAdminUser(AdminUser adminUser) {
        // Check if email already exists
        Optional<AdminUser> existingAdminUser = adminUsersRepository.findByEmail(adminUser.getEmail());

        if (existingAdminUser.isPresent()) {
        	throw new EmailAlreadyRegisteredException("The email address is already registered.");
        }

        // Proceed with saving the new AdminUser
        return adminUsersRepository.save(adminUser);
    }
    
    @Override
    public AdminUser updateAdminUser(AdminUser adminUser) {
        return adminUsersRepository.save(adminUser);
    }

    @Override
    public void deleteAdminUser(int userId) {
        adminUsersRepository.deleteById(userId);
    }
    
    @Override
    public List<AdminUser> getActiveAdminUsers() {
        return adminUsersRepository.findByStatus(1);
    }

    
    @Override
    public AdminUser findByEmailAndPassword(String email, String rawPassword) {
        // Use Optional to safely handle the possibility that the subscriber does not exist
        Optional<AdminUser> optionalAdminUser = adminUsersRepository.findByEmail(email);

        if (optionalAdminUser.isPresent()) {
        	AdminUser adminUser = optionalAdminUser.get();

            // Check if the raw password matches the encoded password
            if (passwordEncoder.matches(rawPassword, adminUser.getPassword())) {

                return adminUser;
            }
        }

        return null; // Return null if credentials are invalid or subscriber does not exist
    }
    
    @Override
    public List<AdminUser> getAllSaathiUsers() {
        return adminUsersRepository.findAllByUserType("Saathi");
    }
    
    @Override
    public boolean isEmailAlreadyRegistered(String email) {
        return adminUsersRepository.findByEmail(email).isPresent();
    }
    
    @Override
    public boolean saathiExists(int saathiID) {
        return adminUsersRepository.existsById(saathiID);
    }
    
    @Override
    public List<AdminUsersDTO> getAllAdminUsersWithSubscribersByUserType() {
        // Fetch AdminUsers of type "Saathi"
        List<AdminUser> adminUsers = adminUsersRepository.findAllAdminUsersWithSubscribersByUserType();

        System.out.println(adminUsers);

        // Check if the list is not empty to avoid mapping null or empty results
        if (adminUsers != null && !adminUsers.isEmpty()) {
            // Use a Set to filter out duplicates based on adminUserID
            Set<AdminUser> uniqueAdminUsers = new HashSet<>(adminUsers);

            // Map the unique AdminUser entities to AdminUsersDTO using stream
            return uniqueAdminUsers.stream()
                                   .map(this::mapToDTO)
                                   .collect(Collectors.toList());
        } else {
            // Return an empty list if no AdminUsers are found
            return Collections.emptyList();
        }
    }


    
    private AdminUsersDTO mapToDTO(AdminUser adminUser) {
        AdminUsersDTO dto = new AdminUsersDTO();
        dto.setAdminUserID(adminUser.getAdminUserID());
        dto.setFirstName(adminUser.getFirstName());
        dto.setLastName(adminUser.getLastName());
        dto.setEmail(adminUser.getEmail());
        dto.setDob(adminUser.getDob());
        dto.setContactNo(adminUser.getContactNo());
        dto.setCountryCode(adminUser.getCountryCode());
        dto.setBriefBio(adminUser.getBriefBio());
        String picturePath = adminUser.getPicture(); // Assuming this is the path from the database
        dto.setPicturePath(picturePath); // Add a path field in DTO

        
        dto.setUserType(adminUser.getUserType());
 //       dto.setCreatedDate(adminUser.getCreatedDate());
//        dto.setLastUpdatedDate(adminUser.getLastUpdatedDate());
        dto.setStatus(adminUser.getStatus());
        dto.setCreatedBy(adminUser.getCreatedBy());
        dto.setUpdatedBy(adminUser.getUpdatedBy());

        // Map the list of Subscribers
        List<SubscriberDTO> subscribers = adminUser.getSubscribers().stream()
                .map(this::mapSubscriberToDTO)
                .collect(Collectors.toList());

        dto.setSubscribers(subscribers);

        return dto;
    }
    
    private SubscriberDTO mapSubscriberToDTO(Subscriber subscriber) {
        SubscriberDTO subscriberDTO = new SubscriberDTO();
        subscriberDTO.setSubscriberID(subscriber.getSubscriberID());
        subscriberDTO.setFirstName(subscriber.getFirstName());
        subscriberDTO.setLastName(subscriber.getLastName());
        subscriberDTO.setEmail(subscriber.getEmail());
        subscriberDTO.setContactNo(subscriber.getContactNo());
        subscriberDTO.setStatus(subscriber.getStatus());
        return subscriberDTO;
    }
    
    @Override
    public long countAssignedSaathi() {
        return adminUsersRepository.countAssignedSaathi();
    }

    @Override
    public long countUnassignedSaathi() {
        return adminUsersRepository.countUnassignedSaathi();
    }
    
    @Override
    public AdminUser changePassword(String email, String oldPassword, String newPassword) {
        // Find AdminUser by email
        Optional<AdminUser> optionalAdminUser = adminUsersRepository.findByEmail(email);
        if (!optionalAdminUser.isPresent()) {
            throw new IllegalArgumentException("AdminUser not found.");
        }

        AdminUser adminUser = optionalAdminUser.get();

        // Validate old password using passwordEncoder
        if (!passwordEncoder.matches(oldPassword, adminUser.getPassword())) {
            throw new IllegalArgumentException("Incorrect old password.");
        }

        // Encode the new password and update
        adminUser.setPassword(passwordEncoder.encode(newPassword));
        adminUsersRepository.save(adminUser);  // Save updated user

        return adminUser;
    }
    
    @Override
    @Transactional
    public void updateSaathiAverageRating(int saathiID) {
        // Step 1: Fetch the Saathi (AdminUser)
        AdminUser saathi = adminUsersRepository.findByAdminUserID(saathiID);

        if (saathi != null) {
            // Step 2: Fetch the list of subscribers assigned to this Saathi
            List<Subscriber> subscribers = subscriberRepository.findSubscribersBySaathiID(saathiID);

            if (subscribers != null && !subscribers.isEmpty()) {
                // Step 3: Extract subscriber IDs
                List<Integer> subscriberIDs = subscribers.stream()
                        .map(Subscriber::getSubscriberID)
                        .collect(Collectors.toList());

                // Step 4: Calculate the average rating of interactions for these subscribers
                Double averageRating = interactionRepository.findAverageRatingBySubscriberIDs(subscriberIDs);
  //              System.out.println("averageRating"+averageRating);
                // Step 5: Update the Saathi's average rating
                if (averageRating != null) {
                    saathi.setAverageRating(averageRating);
                    adminUsersRepository.save(saathi);
                }
            }
        }
    }
}
