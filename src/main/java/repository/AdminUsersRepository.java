package repository;

import model.AdminUsers;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUsersRepository extends JpaRepository<AdminUsers, Integer> {

    // You can define custom query methods here if needed, e.g.,
    AdminUsers findByEmail(String email);
    
    List<AdminUsers> findByStatus(Integer status);
}
