package repository;

import model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {
    // Additional custom queries can be defined here if needed
	
	Subscriber findByEmailAndPassword(String email, String password);
}
