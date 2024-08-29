package controller;

import model.AdminUser;
import model.dto.SubscriberDTO;
import service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @PostMapping
    public ResponseEntity<?> createSubscriber(@RequestBody SubscriberDTO subscriberDTO) {
        try {
            SubscriberDTO createdSubscriber = subscriberService.createSubscriber(subscriberDTO);
            return new ResponseEntity<>(createdSubscriber, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Return a 409 Conflict response if the email is already registered
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{subscriberId}")
    public ResponseEntity<SubscriberDTO> updateSubscriber(
            @PathVariable int subscriberId,
            @RequestBody SubscriberDTO subscriberDTO) {
        SubscriberDTO updatedSubscriber = subscriberService.updateSubscriber(subscriberId, subscriberDTO);
        return new ResponseEntity<>(updatedSubscriber, HttpStatus.OK);
    }

    @GetMapping("/{subscriberId}")
    public ResponseEntity<SubscriberDTO> getSubscriberById(@PathVariable int subscriberId) {
        SubscriberDTO subscriber = subscriberService.getSubscriberById(subscriberId);
        return new ResponseEntity<>(subscriber, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SubscriberDTO>> getAllSubscribers() {
        List<SubscriberDTO> subscribers = subscriberService.getAllSubscribers();
        return new ResponseEntity<>(subscribers, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SubscriberDTO>> getActiveSubscribers() {
        List<SubscriberDTO> subscribers = subscriberService.getActiveSubscribers();
        return ResponseEntity.ok(subscribers);
    }
    @DeleteMapping("/{subscriberId}")
    public ResponseEntity<Void> deleteSubscriber(@PathVariable int subscriberId) {
        subscriberService.deleteSubscriber(subscriberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
