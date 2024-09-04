package repository;

import model.SubscriberAlaCarteServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberAlaCarteServicesRepository extends JpaRepository<SubscriberAlaCarteServices, Integer> {
}
