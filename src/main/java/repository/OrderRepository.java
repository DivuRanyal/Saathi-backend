package repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Additional query methods (if needed) can be defined here
	Optional<Order> findBySubscriberID(Integer subscriberID);
}
