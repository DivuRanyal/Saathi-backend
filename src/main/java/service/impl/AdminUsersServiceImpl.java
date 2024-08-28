package service.impl;

import model.AdminUser;
import repository.AdminUsersRepository;
import service.AdminUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public AdminUser createAdminUser(AdminUser adminUser) {
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
        AdminUser adminUser = adminUsersRepository.findByEmail(email);

        // Check if user exists and if the raw password matches the encoded password
        if (adminUser != null && passwordEncoder.matches(rawPassword, adminUser.getPassword())) {
            return adminUser;
        }

        return null; // Return null if credentials are invalid
    }
	
}
