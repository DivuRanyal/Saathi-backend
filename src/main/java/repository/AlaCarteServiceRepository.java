package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import model.AlaCarteService;
import model.Subscriber;
import model.SubscriberAlaCarteServices;

public interface AlaCarteServiceRepository extends JpaRepository<AlaCarteService, Integer> {

    // Find all services with a specific status
    List<AlaCarteService> findByStatus(Integer status);
   
    
 // Add this method to fetch ala-carte services by subscriberId
   
}
