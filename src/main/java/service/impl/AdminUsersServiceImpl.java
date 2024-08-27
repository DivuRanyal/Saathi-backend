package service.impl;

import model.AdminUsers;
import repository.AdminUsersRepository;
import service.AdminUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminUsersServiceImpl implements AdminUsersService {

    private final AdminUsersRepository adminUsersRepository;

    @Autowired
    public AdminUsersServiceImpl(AdminUsersRepository adminUsersRepository) {
        this.adminUsersRepository = adminUsersRepository;
    }

    @Override
    public List<AdminUsers> getAllAdminUsers() {
        return adminUsersRepository.findAll();
    }

    @Override
    public Optional<AdminUsers> getAdminUserById(int userId) {
        return adminUsersRepository.findById(userId);
    }

    @Override
    public AdminUsers createAdminUser(AdminUsers adminUser) {
        return adminUsersRepository.save(adminUser);
    }

    @Override
    public AdminUsers updateAdminUser(AdminUsers adminUser) {
        return adminUsersRepository.save(adminUser);
    }

    @Override
    public void deleteAdminUser(int userId) {
        adminUsersRepository.deleteById(userId);
    }
    
    @Override
    public List<AdminUsers> getActiveAdminUsers() {
        return adminUsersRepository.findByStatus(1);
    }
}
