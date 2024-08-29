package repository;

import model.AdminUser;
import model.Subscriber;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {
    // Additional custom queries can be defined here if needed
	
	Subscriber findByEmailAndPassword(String email, String password);
	// You can define custom query methods here if needed, e.g.,
	
	List<Subscriber> findByStatus(Integer status);
	 Optional<Subscriber> findByEmail(String email);
	 List<Subscriber> findBySaathi(AdminUser saathi);
    
    
}
