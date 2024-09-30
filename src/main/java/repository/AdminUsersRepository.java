package repository;

import model.AdminUser;
import model.dto.AdminUsersDTO;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUsersRepository extends JpaRepository<AdminUser, Integer> {

    // You can define custom query methods here if needed, e.g.,
    Optional<AdminUser> findByEmail(String email);
    
    List<AdminUser> findByStatus(Integer status);
    
    AdminUser findByEmailAndPassword(String email, String password);
    
    @Query("SELECT au FROM AdminUser au WHERE au.userType = :userType")
    List<AdminUser> findAllByUserType(@Param("userType") String userType);
    
    @Query("SELECT au FROM AdminUser au WHERE au.userType = :userType")
    List<AdminUsersDTO> findByUserType(@Param("userType") String userType);
    
    @Query("SELECT a FROM AdminUser a LEFT JOIN FETCH a.subscribers WHERE a.userType = 'Saathi'")
    List<AdminUser> findAllAdminUsersWithSubscribersByUserType();
    
    // Count Saathi users who are assigned to at least one subscriber
    @Query("SELECT COUNT(a) FROM AdminUser a WHERE a.userType = 'Saathi' AND a.subscribers IS NOT EMPTY")
    long countAssignedSaathi();

    // Count Saathi users who are not assigned to any subscriber
    @Query("SELECT COUNT(a) FROM AdminUser a WHERE a.userType = 'Saathi' AND a.subscribers IS EMPTY")
    long countUnassignedSaathi();
}
