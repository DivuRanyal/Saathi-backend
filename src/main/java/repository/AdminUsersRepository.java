package repository;

import model.AdminUser;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUsersRepository extends JpaRepository<AdminUser, Integer> {

    // You can define custom query methods here if needed, e.g.,
    Optional<AdminUser> findByEmail(String email);
    
    List<AdminUser> findByStatus(Integer status);
    
    AdminUser findByEmailAndPassword(String email, String password);
}
