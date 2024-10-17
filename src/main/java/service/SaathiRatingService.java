package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.AdminUser;
import model.SaathiRating;
import model.Subscriber;
import repository.AdminUsersRepository;
import repository.SaathiRatingRepository;
import repository.SubscriberRepository;

@Service
public class SaathiRatingService {

    @Autowired
    private SaathiRatingRepository saathiRatingRepository;

    @Autowired
    private AdminUsersRepository adminUserRepository;
    
    @Autowired
    private SubscriberRepository subscriberRepository;

    // Method to add a new rating and update the average rating
    public void addRating(int subscriberId, int saathiId, int rating) {
        // Find the saathi and subscriber from the database
        AdminUser saathi = adminUserRepository.findById(saathiId)
                .orElseThrow(() -> new RuntimeException("Saathi not found"));
        
        Subscriber subscriber = subscriberRepository.findById(subscriberId)
                .orElseThrow(() -> new RuntimeException("Subscriber not found"));

        // Create a new rating object
        SaathiRating saathiRating = new SaathiRating();
        saathiRating.setRating(rating);
        saathiRating.setSubscriber(subscriber);
        saathiRating.setSaathi(saathi);

        // Save the rating
        saathiRatingRepository.save(saathiRating);

        // Calculate the new average rating
        Double averageRating = saathiRatingRepository.findAverageRatingBySaathiId(saathiId);
        saathi.setAverageRating(averageRating);

        // Update the saathi with the new average rating
        adminUserRepository.save(saathi);
    }
}
