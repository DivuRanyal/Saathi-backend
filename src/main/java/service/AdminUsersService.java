package service;

import model.AdminUser;

import java.util.List;
import java.util.Optional;

public interface AdminUsersService {

    List<AdminUser> getAllAdminUsers();
    
    public List<AdminUser> getActiveAdminUsers();
    Optional<AdminUser> getAdminUserById(int userId);

    AdminUser createAdminUser(AdminUser adminUser);

    AdminUser updateAdminUser(AdminUser adminUser);

    void deleteAdminUser(int userId);
    
    AdminUser findByEmailAndPassword(String email, String password);
}
