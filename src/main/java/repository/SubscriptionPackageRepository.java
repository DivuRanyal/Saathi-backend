package repository;

import model.SubscriptionPackage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPackageRepository extends JpaRepository<SubscriptionPackage, Integer> {

    // Custom query methods can be added here if necessary
	List<SubscriptionPackage> findByStatus(Integer status); 
}
