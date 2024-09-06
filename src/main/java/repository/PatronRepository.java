package repository;

import model.Patron;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Integer> {

    // Find all patrons based on subscriber ID (returns a list of patrons)
    List<Patron> findBySubscriber_SubscriberID(int subscriberID);

    // Find a single patron based on subscriber ID (returns an optional single patron)
    Optional<Patron> findFirstBySubscriber_SubscriberID(int subscriberID);

    // Find a patron by email
    Optional<Patron> findByEmail(String email);
}
