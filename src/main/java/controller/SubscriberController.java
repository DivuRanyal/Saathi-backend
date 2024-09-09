package controller;

import model.AdminUser;
import model.AlaCarteService;
import model.ServiceReport;
import model.Subscriber;
import model.SubscriberAlaCarteServices;
import model.dto.CreditCardDTO;
import model.dto.PatronDTO;
import model.dto.PatronServiceDTO;
import model.dto.SubscriberDTO;

import service.EmailService;
import service.PatronService;
import service.ServiceCompletionService;
import service.ServiceCompletionServiceNew;
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
import java.util.Optional;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;
    
 //   @Autowired
 //   private ServiceCompletionService completionService;
    
    @Autowired
    private SubscriberAlaCarteServicesService service;

    @Autowired
    private PatronService patronService;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private ServiceCompletionServiceNew serviceCompletionService;

    @Autowired
    private ServiceCompletionService serviceCompletionService1;

    @PostMapping
    public ResponseEntity<?> createSubscriber(@RequestBody SubscriberDTO subscriberDTO) throws MessagingException, IOException, TemplateException {
        try {
            // Create the subscriber
            SubscriberDTO createdSubscriber = subscriberService.createSubscriber(subscriberDTO);
            
            // Send email notification to admin after successful creation
            String adminEmail = "divya1111sharma@gmail.com"; // Replace with actual admin email
            String subject = "New Subscriber Added";
            Map<String, Object> model = new HashMap<>();
            
            // Create a Map for the subscriber data
            Map<String, Object> subscriberData = new HashMap<>();
            subscriberData.put("name", createdSubscriber.getFirstName() + " " + createdSubscriber.getLastName());

            // Add the subscriber map to the main model
            model.put("subscriber", subscriberData); // Pass the subscriber object

            // Assuming you have an email service that sends Freemarker templated emails
            emailService.sendEmail(adminEmail, subject, "subscriber-added-email.ftlh", model);
            
            // Return the created subscriber details
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
        Map<String, Object> model = new HashMap<>();
        model.put("saathiName", "John Doe");
        model.put("subscriberName", "Jane Smith");
        model.put("subscriberPhone", "123-456-7890");
        model.put("subscriberEmail", "ranyal123divya@gmail.com");
        model.put("subscriptionPackage", "Gold");

        // Patron details
        model.put("patronName", "Mr. Smith");
        model.put("patronRelationship", "Father");
        model.put("patronAddress", "123 Rural St, Village, Country");

        try {
            // Send email using the email service
            emailService.sendSaathiAssignedEmail(saathiEmail, model);
            return "Email sent successfully!";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Failed to send email.";
        }
    }
   
    @GetMapping("/sendEmail")
    public String sendEmail(
        @RequestParam String to, 
        @RequestParam String subject, 
        @RequestParam String body) {

        emailService.sendSimpleEmail(to, subject, body);
        return "Email sent successfully!";
    }
  /*  @PutMapping("/{subscriberID}/assign-saathi")
    public ResponseEntity<Map<String, Object>> assignSaathiToSubscriber(
            @PathVariable int subscriberID, @RequestParam int saathiID) throws IOException, TemplateException {

        // Step 1: Assign Saathi to Subscriber and get the updated subscriber
        SubscriberDTO updatedSubscriber = subscriberService.assignSaathiToSubscriber(subscriberID, saathiID);

        // Step 2: Fetch the Patron details for the Subscriber
        PatronDTO patronDetails = patronService.getPatronBySubscriberId(subscriberID);

        // Step 3: Create a response model (Map) with additional details
        Map<String, Object> model = new HashMap<>();

        // Step 4: Populate the model with data from the updated subscriber
        model.put("subscriberID", updatedSubscriber.getSubscriberID());
        model.put("subscriberName", updatedSubscriber.getFirstName() + " " + updatedSubscriber.getLastName());
        model.put("subscriberPhone", updatedSubscriber.getContactNo());
        model.put("subscriberEmail", updatedSubscriber.getEmail());
        model.put("subscriptionPackage", updatedSubscriber.getPackageID() != null ? "Gold" : "Standard");

        // Saathi details
        String saathiEmail = updatedSubscriber.getSaathi().getEmail(); 
        if (updatedSubscriber.getSaathi() != null) {
            model.put("saathiName", updatedSubscriber.getSaathi().getFirstName());
            saathiEmail = updatedSubscriber.getSaathi().getEmail(); // Assuming Saathi has an email field
        } else {
            model.put("saathiName", "John Doe"); // Fallback or default value
        }

        // Step 5: Add Patron details dynamically based on the Subscriber
        model.put("patronName", patronDetails.getFirstName() + " " + patronDetails.getLastName());
        model.put("patronRelationship", patronDetails.getRelation());
        model.put("patronAddress", patronDetails.getAddress1() + ", " + patronDetails.getAddress2() + ", " + 
                                    patronDetails.getCity() + ", " + patronDetails.getState() + ", " + patronDetails.getCountry());

        // Step 6: Send an email using the email service
        try {
            emailService.sendSaathiAssignedEmail(saathiEmail, model);
        } catch (MessagingException e) {
            e.printStackTrace();
            model.put("emailStatus", "Failed to send email.");
        }

        // Step 7: Return the model and HttpStatus.OK
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
*/
    
    @PutMapping("/{subscriberID}/assign-saathi")
    public ResponseEntity<Map<String, Object>> assignSaathiToSubscriber(
            @PathVariable int subscriberID, @RequestParam int saathiID) throws IOException, TemplateException {

        // Step 1: Assign Saathi to Subscriber and get the updated subscriber
        SubscriberDTO updatedSubscriber = subscriberService.assignSaathiToSubscriber(subscriberID, saathiID);

        // Step 2: Fetch the Patron details for the Subscriber
 //       PatronDTO patronDetails = patronService.getPatronBySubscriberId(subscriberID);

        // Step 3: Create a response model (Map) with additional details
        Map<String, Object> model = new HashMap<>();

        // Step 4: Populate the model with data from the updated subscriber
        model.put("subscriberID", updatedSubscriber.getSubscriberID());
        model.put("subscriberName", updatedSubscriber.getFirstName() + " " + updatedSubscriber.getLastName());
        model.put("subscriberPhone", updatedSubscriber.getContactNo());
        model.put("subscriberEmail", updatedSubscriber.getEmail());
        model.put("subscriptionPackage", updatedSubscriber.getPackageName() != null ? "Gold" : "Standard");

        // Saathi details
        String saathiEmail = updatedSubscriber.getSaathi().getEmail(); 
        if (updatedSubscriber.getSaathi() != null) {
            model.put("saathiName", updatedSubscriber.getSaathi().getFirstName());
            saathiEmail = updatedSubscriber.getSaathi().getEmail(); // Assuming Saathi has an email field
        } else {
            model.put("saathiName", "John Doe"); // Fallback or default value
        }

        // Step 5: Add Patron details dynamically based on the Subscriber
  //      model.put("patronName", patronDetails.getFirstName() + " " + patronDetails.getLastName());
  //      model.put("patronRelationship", patronDetails.getRelation());
 //       model.put("patronAddress", patronDetails.getAddress1() + ", " + patronDetails.getAddress2() + ", " + 
//                                    patronDetails.getCity() + ", " + patronDetails.getState() + ", " + patronDetails.getCountry());

        // Step 6: Send an email using the email service
        try {
            emailService.sendSaathiAssignedEmail(saathiEmail, model);
        } catch (MessagingException e) {
            e.printStackTrace();
            model.put("emailStatus", "Failed to send email.");
        }

        // Step 7: Return the model and HttpStatus.OK
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
    
    @GetMapping("/without-saathi")
    public ResponseEntity<List<Subscriber>> getSubscribersWithoutSaathi() {
        List<Subscriber> subscribers = subscriberService.getSubscribersWithoutSaathi();
        return ResponseEntity.ok(subscribers);
    }
    
 // Define an endpoint to get the services for a subscriber
    @GetMapping("/{subscriberId}/services")
    public ResponseEntity<?> getSubscriberServices(@PathVariable Long subscriberId) {
        try {
            // Fetch the services for the subscriber
            Map<String, List<ServiceReport>> services = serviceCompletionService.getSubscriberServices(subscriberId);

            // Check if the list of services is null or empty
            if (services == null || services.isEmpty() || !services.containsKey("allServices")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No services found for subscriber with ID: " + subscriberId);
            }

            List<ServiceReport> serviceReports = services.get("allServices");
            
            if (serviceReports == null || serviceReports.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No services found for subscriber with ID: " + subscriberId);
            }

            // Return the list of service reports
            return ResponseEntity.ok(serviceReports);
            
        } catch (Exception e) {
            // Log the error and return a response with status 500
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving services for subscriber ID: " + subscriberId);
        }
    }


    // Endpoint for confirming a package purchase
 /*   @PostMapping("/{subscriberId}/confirmPackagePurchase/{packageId}")
    public ResponseEntity<String> confirmPackagePurchase(@PathVariable Long subscriberId, @PathVariable int packageId) {
        serviceCompletionService.trackSubscriberPackage(subscriberId, packageId);
        return ResponseEntity.ok("Package purchase confirmed and services are being tracked.");
    }
*/
    // Endpoint for updating service completion for a subscriber
    @PutMapping("/{subscriberId}/services/{serviceId}/complete")
    public ResponseEntity<String> updateServiceCompletion(@PathVariable Long subscriberId, @PathVariable int serviceId) {
        Map<String, List<ServiceReport>> updatedServices = serviceCompletionService.updateServiceCompletion(subscriberId, serviceId);
       
        if (updatedServices != null) {
            return ResponseEntity.ok("Service completion updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found for the subscriber.");
        }
    }
    
    @PostMapping("/Services")
    public ResponseEntity<Map<String, List<ServiceReport>>> trackSubscriberServices(
            @RequestParam Long subscriberId,
            @RequestParam(required = false, defaultValue = "0") int packageId,
            @RequestParam(required = false, defaultValue = "0") int subscriberAlaCarteServiceId
    		) {

        // Call the service method to track services
        Map<String, List<ServiceReport>> trackedServices = serviceCompletionService.trackSubscriberServices(subscriberId, packageId, subscriberAlaCarteServiceId);

        // Check if any services were tracked
        if (trackedServices == null || trackedServices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body(null);
       }

        // Return the tracked services in the response
        return ResponseEntity.ok(trackedServices);
    	
   //     return null;
    }
      
}
