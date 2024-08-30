package repository;

import model.Patron;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Integer> {
    // Add custom query methods here if needed
	
	List<Patron> findBySubscriberSubscriberID(int subscriberID);
	 Optional<Patron> findByEmail(String email);
}
