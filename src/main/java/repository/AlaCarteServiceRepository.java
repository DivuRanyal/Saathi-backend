package repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import model.AlaCarteService;

public interface AlaCarteServiceRepository extends JpaRepository<AlaCarteService, Integer> {

    // Find all services with a specific status
    List<AlaCarteService> findByStatus(Integer status);

   

}
