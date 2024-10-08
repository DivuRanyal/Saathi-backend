package repository;

import model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByMobileNumber(String mobileNumber);
    Users findByEmail(String email);
}
