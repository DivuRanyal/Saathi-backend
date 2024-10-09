package service;

import model.AdminUser;
import model.dto.AdminUsersDTO;

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
    
     List<AdminUser> getAllSaathiUsers();
    
    boolean isEmailAlreadyRegistered(String email);
    boolean saathiExists(int saathiID) ;
   
    List<AdminUsersDTO> getAllAdminUsersWithSubscribersByUserType();
    
    public long countAssignedSaathi();
    public long countUnassignedSaathi();
    AdminUser changePassword(String email, String oldPassword, String newPassword);
}
