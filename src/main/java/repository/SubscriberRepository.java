package repository;

import model.Subscriber;
import model.dto.SubscriberDTO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {
    // Additional custom queries can be defined here if needed
	
	Subscriber findByEmailAndPassword(String email, String password);
	// You can define custom query methods here if needed, e.g.,
	Subscriber findByEmail(String email);
	List<Subscriber> findByStatus(Integer status);
    
    
}
