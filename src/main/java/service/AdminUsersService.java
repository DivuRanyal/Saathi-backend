package service;

import model.AdminUsers;

import java.util.List;
import java.util.Optional;

public interface AdminUsersService {

    List<AdminUsers> getAllAdminUsers();
    
    public List<AdminUsers> getActiveAdminUsers();
    Optional<AdminUsers> getAdminUserById(int userId);

    AdminUsers createAdminUser(AdminUsers adminUser);

    AdminUsers updateAdminUser(AdminUsers adminUser);

    void deleteAdminUser(int userId);
}
