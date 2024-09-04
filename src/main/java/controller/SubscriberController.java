package controller;

import model.AdminUser;
import model.SubscriberAlaCarteServices;
import model.dto.PatronDTO;
import model.dto.PatronServiceDTO;
import model.dto.SubscriberDTO;
import service.EmailService;
import service.PatronService;
import service.SubscriberAlaCarteServicesService;
import service.SubscriberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import exception.EmailAlreadyRegisteredException;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import javax.mail.MessagingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;
    
    @Autowired
    private SubscriberAlaCarteServicesService service;

    @Autowired
    private PatronService patronService;
    
    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> createSubscriber(@RequestBody SubscriberDTO subscriberDTO) {
        try {
            SubscriberDTO createdSubscriber = subscriberService.createSubscriber(subscriberDTO);
            return new ResponseEntity<>(createdSubscriber, HttpStatus.CREATED);
        } catch (EmailAlreadyRegisteredException e) {
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
        
    @PostMapping("/services")
 //   public ResponseEntity<SubscriberAlaCarteServices> createService(@RequestBody SubscriberAlaCarteServices serviceRequest) {
    public ResponseEntity<?> createService(@RequestBody String serviceRequest) {
    	 System.out.println(serviceRequest);
 //       SubscriberAlaCarteServices createdService = service.createOrUpdateService(serviceRequest);
 //       System.out.println(createdService);
        return new ResponseEntity<>(serviceRequest, HttpStatus.CREATED);
    }
    
  
    @PostMapping("/savePatronAndService")
    public ResponseEntity<PatronServiceDTO> savePatronAndService(@RequestBody PatronServiceDTO patronServiceDTO) {
        // Ensure that the incoming DTO is not null
        if (patronServiceDTO == null || patronServiceDTO.getSubscriberAlaCarteServices() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Save Patron
        PatronDTO savedPatron = patronService.savePatron(patronServiceDTO.getPatronDTO());

        // Save SubscriberAlaCarteServices
        SubscriberAlaCarteServices serviceRequest = patronServiceDTO.getSubscriberAlaCarteServices();
        SubscriberAlaCarteServices savedService = service.createOrUpdateService(serviceRequest);

        // Construct the response DTO with saved data
        PatronServiceDTO responseDTO = new PatronServiceDTO();
        responseDTO.setPatronDTO(savedPatron);
        responseDTO.setSubscriberAlaCarteServices(savedService);

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/saathi")
    public ResponseEntity<AdminUser> getSubscriberDetails(@PathVariable int id) {
    	AdminUser adminUser = subscriberService.getSubscriberDetails(id);
        return new ResponseEntity<>(adminUser, HttpStatus.OK);
    }
    
    @GetMapping("/sendTestEmail")
    public String sendTestEmail(@RequestParam String saathiEmail) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
        Map<String, Object> model = new HashMap();
        model.put("saathiName", "John Doe");
        model.put("subscriberName", "Jane Smith");
        model.put("subscriberPhone", "123-456-7890");
        model.put("subscriberEmail", "ranyal123divya@gmail.com");
        model.put("subscriptionPackage", "Gold");

        // If there are patron details
        model.put("patronName", "Mr. Smith");
        model.put("patronRelationship", "Father");
        model.put("patronAddress", "123 Rural St, Village, Country");

        try {
            emailService.sendSaathiAssignedEmail(saathiEmail, model);
            return "Email sent successfully!";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Failed to send email.";
        }
    }
}
