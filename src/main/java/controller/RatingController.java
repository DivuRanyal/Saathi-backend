package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import model.Interaction;
import service.AdminUsersService;
import service.InteractionService;
import service.SaathiRatingService;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private SaathiRatingService saathiRatingService;

    @Autowired
    private InteractionService interactionService;

    @Autowired
    private AdminUsersService adminUsersService;

    @PostMapping("/{interactionID}/rate")
    public ResponseEntity<String> rateInteraction(@PathVariable int interactionID,
                                                  @RequestParam int serviceRating) {
        if (serviceRating < 1 || serviceRating > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5.");
        }

        // Update the interaction rating
       Interaction updatedInteraction=  interactionService.rateInteraction(interactionID, serviceRating);
        Integer saathiID = updatedInteraction.getSaathiID();
        if (saathiID != null) {
            // Call method to update the Saathi's average rating
            adminUsersService.updateSaathiAverageRating(saathiID);
        }
        return ResponseEntity.ok("Interaction rated successfully.");
    }
    
    @PostMapping("/rateSaathi")
    public ResponseEntity<String> rateSaathi(@RequestParam int subscriberID, 
                                             @RequestParam int saathiID, 
                                             @RequestParam int rating) {
        if (rating < 1 || rating > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5.");
        }

        saathiRatingService.addRating(subscriberID, saathiID, rating);
        return ResponseEntity.ok("Rating submitted successfully.");
    }
}
