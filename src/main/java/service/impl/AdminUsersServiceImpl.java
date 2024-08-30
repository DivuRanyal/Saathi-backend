package service.impl;

import model.AdminUser;
import model.Subscriber;
import repository.AdminUsersRepository;
import service.AdminUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import exception.EmailAlreadyRegisteredException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdminUsersServiceImpl implements AdminUsersService {

    private final AdminUsersRepository adminUsersRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public AdminUsersServiceImpl(AdminUsersRepository adminUsersRepository) {
        this.adminUsersRepository = adminUsersRepository;
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
}
