package repository;

import model.SaathiRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SaathiRatingRepository extends JpaRepository<SaathiRating, Integer> {

    @Query("SELECT AVG(r.rating) FROM SaathiRating r WHERE r.saathi.adminUserID = :saathiId")
    Double findAverageRatingBySaathiId(int saathiId);
}
