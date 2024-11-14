package controller;

import model.AdminUser;
import model.AggregatedServiceReport;
import model.AlaCarteService;
import model.Order;
import model.PreferredDateTime;
import model.PackageServices;
import model.ServiceReport;
import model.Subscriber;
import model.SubscriberAlaCarteServices;
import model.SubscriptionPackage;
import model.dto.CreditCardDTO;
import model.dto.InteractionDTO;
import model.dto.PatronDTO;
import model.dto.PatronServiceDTO;
import model.dto.SubscriberDTO;
import model.dto.SubscriberSaathiDTO;
import model.dto.SubscriptionPackageDTO;
import repository.AlaCarteServiceRepository;
import repository.InteractionRepository;
import repository.OrderRepository;
import repository.PackageServiceRepository;
import repository.SubscriberAlaCarteServicesRepository;
import repository.SubscriberRepository;
import service.AdminUsersService;
import service.CashfreeService;
import service.EmailService;
import service.InteractionService;
import service.PatronService;
//import service.ServiceCompletionService;
import service.ServiceCompletionServiceNew;
import service.SubscriberAlaCarteServicesService;
import service.SubscriberService;
import service.SubscriptionPackageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import exception.EmailAlreadyRegisteredException;
import exception.InvalidOtpException;
import exception.OtpExpiredException;
import exception.SubscriberNotFoundException;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import javax.mail.MessagingException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;
    
    @Autowired
    private CashfreeService cashfreeService;
    @Autowired
    private AdminUsersService adminUsersService;
 //   @Autowired
 //   private ServiceCompletionService completionService;
    
    @Autowired
    private SubscriberAlaCarteServicesService service;

    @Autowired
    private AlaCarteServiceRepository alaCarteServicesRepository;
    @Autowired
    private PatronService patronService;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private ServiceCompletionServiceNew serviceCompletionService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private InteractionService interactionService;
    
    @Autowired
    private SubscriberAlaCarteServicesRepository subscriberAlaCarteServicesRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;
    
    @Autowired
    private InteractionRepository interactionRepository;
    
    @Autowired
    private PackageServiceRepository packageServiceRepository;
    
    @Autowired
    private SubscriptionPackageService subscriptionPackageService;  // Service to fetch SubscriptionPackage

    @PostMapping
    public ResponseEntity<?> createSubscriber(@RequestBody SubscriberDTO subscriberDTO) throws MessagingException, IOException, TemplateException {
        try {
            // Create the subscriber
            SubscriberDTO createdSubscriber = subscriberService.createSubscriber(subscriberDTO);
            
            // Send email notification to admin after successful creation
            String adminEmail = "suchigupta@etheriumtech.com"; // Replace with actual admin email
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
    public ResponseEntity<?> getSubscriberById(@PathVariable int subscriberId) {
        try {
            // Fetch subscriber details
            SubscriberDTO subscriber = subscriberService.getSubscriberById(subscriberId);

            // Fetch services for the subscriber
            Map<String, List<ServiceReport>> services = serviceCompletionService.getSubscriberServices(subscriberId);
            List<Map<String, Object>> servicesWithInteractions = new ArrayList<>();

            if (services != null && !services.isEmpty() && services.containsKey("allServices")) {
                List<ServiceReport> serviceReports = services.get("allServices");

                if (serviceReports != null && !serviceReports.isEmpty()) {
                    // Fetch interactions related to the subscriber
                    List<InteractionDTO> interactions = interactionService.getInteractionsBySubscriberID(subscriberId);

                    // Create the final response list of services, each with its associated interactions
                    for (ServiceReport serviceReport : serviceReports) {
                        Map<String, Object> serviceWithInteraction = new HashMap<>();
                        
                        // Add the service details to the map
                        serviceWithInteraction.put("serviceID", serviceReport.getServiceID());
                        serviceWithInteraction.put("serviceName", serviceReport.getServiceName());
                        serviceWithInteraction.put("packageName", serviceReport.getPackageName());
                        serviceWithInteraction.put("packageServiceID", serviceReport.getPackageServiceID());
                        serviceWithInteraction.put("frequency", serviceReport.getFrequency());
                        serviceWithInteraction.put("frequencyUnit", serviceReport.getFrequencyUnit());
                        serviceWithInteraction.put("completions", serviceReport.getCompletions());
                        serviceWithInteraction.put("completionStatus", serviceReport.getCompletionStatus());
                        serviceWithInteraction.put("completionDate", serviceReport.getCompletionDate());
                        serviceWithInteraction.put("frequencyCount", serviceReport.getFrequencyCount());
                        serviceWithInteraction.put("pending", serviceReport.getPending());
                        serviceWithInteraction.put("alaCarte", serviceReport.isAlaCarte());
                        serviceWithInteraction.put("subscriberAlaCarteServicesID", serviceReport.getSubscriberAlaCarteServicesID());

                        // Handle list of requested dates and times
                        List<Map<String, String>> preferredDatesTimes = new ArrayList<>();
                        if (serviceReport.getPreferredDateTimes() != null) {
                        for (PreferredDateTime preferredDateTime : serviceReport.getPreferredDateTimes()) {
                            Map<String, String> dateTimeMap = new HashMap<>();
                            dateTimeMap.put("preferredDate", preferredDateTime.getPreferredDate().toString());
                            dateTimeMap.put("preferredTime", preferredDateTime.getPreferredTime().toString());
                            dateTimeMap.put("completionStatus", preferredDateTime.getCompletionStatus());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            // Format the LocalDateTime object
                            String formattedDate = preferredDateTime.getRequestedDate().format(formatter);
                            dateTimeMap.put("requestedDate",formattedDate);
                            if (preferredDateTime.getCompletionDate() != null) {
                                dateTimeMap.put("completionDate", preferredDateTime.getCompletionDate().toString());
                            }
                            preferredDatesTimes.add(dateTimeMap);
                        }
                        }
                        serviceWithInteraction.put("preferredDatesTimes", preferredDatesTimes);

                        // Filter interactions for this service
                        List<InteractionDTO> relatedInteractions = interactions.stream()
                            .filter(interaction -> {
                                if (serviceReport.isAlaCarte()) {
                                    return interaction.getSubscriberAlaCarteServicesID() != null &&
                                           interaction.getSubscriberAlaCarteServicesID().equals(serviceReport.getSubscriberAlaCarteServicesID());
                                } else if (serviceReport.getPackageServiceID() != 0 && interaction.getPackageServicesID() != null) {
                                    return interaction.getPackageServicesID().equals(serviceReport.getPackageServiceID());
                                } else {
                                    return false; // No matching service
                                }
                            })
                            .collect(Collectors.toList());
                        // Add the interactions to the service
                        serviceWithInteraction.put("interactions", relatedInteractions);

                        // Add the service with its interactions to the list
                        servicesWithInteractions.add(serviceWithInteraction);
                    }
                }
            }

            // Create a response map combining subscriber and services
            Map<String, Object> response = new HashMap<>();
            // Add the subscriber details
            response.putAll(new ObjectMapper().convertValue(subscriber, Map.class));
            // Add the services
            response.put("services", servicesWithInteractions);

            // Return the combined response
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Log the error and return a response with status 500
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while retrieving data for subscriber ID: " + subscriberId);
        }
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
    
    @GetMapping("/billing/inactive")
    public ResponseEntity<List<SubscriberDTO>> getInActiveBillingSubscribers() {
        List<SubscriberDTO> subscribers = subscriberService.getInActiveBillingSubscribers();
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
    	
     // When retrieving the AdminUser object, construct the full URL for the picture
        if (adminUser.getPicture() != null && !adminUser.getPicture().isEmpty()) {
    
            String pictureUrl = adminUser.getPicture();
            adminUser.setPicture(pictureUrl);  // Set the full URL in the response
        }
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
            @PathVariable int subscriberID, @RequestParam int saathiID,  @RequestBody(required = false)
            Map<String, String> reason) throws IOException, TemplateException {

    	
    	 // Step 1: Check if the Subscriber exists
        boolean subscriberExists = subscriberService.subscriberExists(subscriberID); // You should implement this method
        if (!subscriberExists) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Subscriber not found with ID: " + subscriberID);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        // Step 2: Check if the Saathi (companion) exists
        boolean saathiExists = adminUsersService.saathiExists(saathiID); // You should implement this method
        if (!saathiExists) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Saathi not found with ID: " + saathiID);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        
        // Step 3: Extract Reason from the request body if present
        String reasonForChange = reason != null ? reason.getOrDefault("Reason", "") : ""; // Use the correct key
        System.out.println("Reason for change: " + reasonForChange);

        // Step 1: Assign Saathi to Subscriber and get the updated subscriber
        SubscriberDTO updatedSubscriber = subscriberService.assignSaathiToSubscriber(subscriberID, saathiID, reasonForChange);
//        System.out.println(updatedSubscriber.getSaathi().getAdminUserID());
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
        System.out.println(saathiEmail);
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
    
    @GetMapping("/with-saathi")
    public ResponseEntity<List<SubscriberSaathiDTO>> getSubscribersWithSaathi() {
        List<SubscriberSaathiDTO> subscribers = subscriberService.getSubscribersWithSaathi();
        return ResponseEntity.ok(subscribers);
    }
    
 // Define an endpoint to get the services for a subscriber
    @GetMapping("/{subscriberID}/services")
    public ResponseEntity<?> getSubscriberServices(@PathVariable Integer subscriberID) {
        try {
            // Fetch the services for the subscriber
            Map<String, List<ServiceReport>> services = serviceCompletionService.getSubscriberServices(subscriberID);
            System.out.println(services);
            
            // Check if the list of services is null or empty
            if (services == null || services.isEmpty() || !services.containsKey("allServices")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No services found for subscriber with ID: " + subscriberID);
            }

            List<ServiceReport> serviceReports = services.get("allServices");

            if (serviceReports == null || serviceReports.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No services found for subscriber with ID: " + subscriberID);
            }

            // Fetch interactions related to the subscriber
            List<InteractionDTO> interactions = interactionService.getInteractionsBySubscriberID(subscriberID);

            // Create the final response list of services, each with its associated interactions
            List<Map<String, Object>> servicesWithInteractions = new ArrayList<>();

            for (ServiceReport serviceReport : serviceReports) {
                Map<String, Object> serviceWithInteraction = new HashMap<>();

                // Add the service details to the map
                serviceWithInteraction.put("serviceID", serviceReport.getServiceID());
                serviceWithInteraction.put("serviceName", serviceReport.getServiceName());
                serviceWithInteraction.put("packageName", serviceReport.getPackageName());
                serviceWithInteraction.put("packageServiceID", serviceReport.getPackageServiceID());
                serviceWithInteraction.put("frequency", serviceReport.getFrequency());
                serviceWithInteraction.put("frequencyUnit", serviceReport.getFrequencyUnit());
                serviceWithInteraction.put("completions", serviceReport.getCompletions());
                serviceWithInteraction.put("completionStatus", serviceReport.getCompletionStatus());
                serviceWithInteraction.put("completionDate", serviceReport.getCompletionDate());
                serviceWithInteraction.put("frequencyCount", serviceReport.getFrequencyCount());
                serviceWithInteraction.put("pending", serviceReport.getPending());
                serviceWithInteraction.put("alaCarte", serviceReport.isAlaCarte());
                serviceWithInteraction.put("subscriberAlaCarteServicesID", serviceReport.getSubscriberAlaCarteServicesID());
                serviceWithInteraction.put("frequencyInstance", serviceReport.getFrequencyInstance());
                
                // Handling list of requested dates and times
                List<Map<String, String>> preferredDatesTimes = new ArrayList<>();
                if (serviceReport.getPreferredDateTimes() != null) {
                for (PreferredDateTime preferredDateTime : serviceReport.getPreferredDateTimes()) {
                    Map<String, String> dateTimeMap = new HashMap<>();
                    dateTimeMap.put("preferredDate", preferredDateTime.getPreferredDate().toString());
                    dateTimeMap.put("preferredTime", preferredDateTime.getPreferredTime().toString());
                    dateTimeMap.put("completionStatus", preferredDateTime.getCompletionStatus());
                    System.out.println("preferredDateTime.getRequestedDate()"+preferredDateTime.getRequestedDate());
                    // Define the formatter to display in "yyyy-MM-dd HH:mm:ss" format
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    // Format the LocalDateTime object
                    if(preferredDateTime.getRequestedDate() != null) {
                    	String formattedDate = preferredDateTime.getRequestedDate().format(formatter);
                        dateTimeMap.put("requestedDate",formattedDate);
                    }
                    if (preferredDateTime.getCompletionDate() != null) {
                        dateTimeMap.put("completionDate", preferredDateTime.getCompletionDate().toString());
                    }
                    preferredDatesTimes.add(dateTimeMap);
                }
                }
                serviceWithInteraction.put("preferredDatesTimes", preferredDatesTimes);
                System.out.println("-"+serviceReport.getPackageServiceID());
                // Filter interactions related to the current service
                List<InteractionDTO> relatedInteractions = interactions.stream()
                	    .filter(interaction -> {
                	        // Check for ala-carte services by SubscriberAlaCarteServicesID and frequencyCount
                	        if (serviceReport.isAlaCarte()) {
                	            return interaction.getSubscriberAlaCarteServicesID() != null &&
                	                   interaction.getSubscriberAlaCarteServicesID().equals(serviceReport.getSubscriberAlaCarteServicesID()) &&
                	                   interaction.getFrequencyInstance().equals(serviceReport.getFrequencyInstance());
                	        } 
                	        // Check for package services by PackageServicesID and frequencyCount
                	        else if (serviceReport.getPackageServiceID() != 0 && interaction.getPackageServicesID() != null) {
                	            return interaction.getPackageServicesID().equals(serviceReport.getPackageServiceID()) &&
                	                   interaction.getFrequencyInstance().equals(serviceReport.getFrequencyInstance());
                	        } else {
                	            return false; // No matching service
                	        }
                	    })
                	    .map(interaction -> {
                	        // If the interaction contains documents, you can prepend a base URL if required
                	        if (interaction.getDocuments() != null && !interaction.getDocuments().isEmpty()) {
                	            String documentUrl = interaction.getDocuments();
                	            interaction.setDocuments(documentUrl); // Set the document URL in the response
                	        }
                	        return interaction;
                	    })
                	    .collect(Collectors.toList());

                // Add the interactions to the service
                serviceWithInteraction.put("interactions", relatedInteractions);

                // Add the service with its interactions to the list
                servicesWithInteractions.add(serviceWithInteraction);
            }

            // Return the response containing services with interactions
            return ResponseEntity.ok(servicesWithInteractions);

        } catch (Exception e) {
            // Log the error and return a response with status 500
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while retrieving services for subscriber ID: " + subscriberID);
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
/*    @PutMapping("/{subscriberId}/services/{serviceId}/complete")
    public ResponseEntity<String> updateServiceCompletion(@PathVariable Long subscriberId, @PathVariable int serviceId) {
        Map<String, List<ServiceReport>> updatedServices = serviceCompletionService.updateServiceCompletion(subscriberId, serviceId);
       
        if (updatedServices != null) {
            return ResponseEntity.ok("Service completion updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found for the subscriber.");
        }
    }
 */  
    
    @PostMapping("/Services")
    public ResponseEntity<Map<String, List<ServiceReport>>> trackSubscriberServices(
            @RequestParam Integer subscriberID,
            @RequestParam(required = false, defaultValue = "0") int packageID,
            @RequestParam(required = false, defaultValue = "0") int subscriberAlaCarteServicesID
    		) {
        // Call the service method to track services
        Map<String, List<ServiceReport>> trackedServices = serviceCompletionService.trackSubscriberServices(subscriberID, packageID, subscriberAlaCarteServicesID,null,null);
        // Check if any services were tracked
        if (trackedServices == null || trackedServices.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body(null);
       }
        // Return the tracked services in the response
        return ResponseEntity.ok(trackedServices);
   //     return null;
    }
           
    @PostMapping("/{subscriberID}/services/{serviceID}/complete")
    public ResponseEntity<String> updateServiceCompletion(
            @PathVariable Integer subscriberID,
            @PathVariable Integer serviceID,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("description") String description,
            @RequestParam("isAlaCarte") Boolean isAlaCarte,
            @RequestParam(name="frequencyInstance",required=false) Integer frequencyInstance,
            @RequestParam(name = "subscriberAlaCarteServicesID", required = false) Integer subscriberAlaCarteServiceID, // Distinguish ala-carte services
            @RequestParam(name = "preferredDate", required = false) String preferredDate, // Required for package services
            @RequestParam(name = "preferredTime", required = false) String preferredTime // Required for package services
    ) {

        // Step 1: Fetch the packageServiceID associated with the subscriber
        Integer packageID = subscriberService.getPackageIDBySubscriber(subscriberID);
        System.out.println("packageID: " + packageID);

        // Parse the preferredDate and preferredTime from the input strings
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate parsedPreferredDate=null;
        LocalTime parsedPreferredTime=null;
        if (preferredDate != null && preferredTime != null) {
         parsedPreferredDate = LocalDate.parse(preferredDate, dateFormatter);
         parsedPreferredTime = LocalTime.parse(preferredTime, timeFormatter);
        }
        // If no valid package or ala-carte service found for the subscriber
        if (packageID == null && subscriberAlaCarteServiceID == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No valid package or ala-carte service found for the subscriber.");
        }

        // Ensure preferredDate and preferredTime are provided for package services
 //       if (!isAlaCarte && (preferredDate == null || preferredTime == null)) {
 //           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Preferred date and time must be provided for package services.");
 //       }

        // Step 2: Update service completion logic
        Map<String, List<ServiceReport>> updatedServices;

        if (isAlaCarte) {
            // Update ala-carte service completion
            updatedServices = serviceCompletionService.updateServiceCompletion(subscriberID, serviceID, subscriberAlaCarteServiceID, true, null, null,frequencyInstance);
        } else {
            // Update package service completion with preferred date and time
        	if (subscriberAlaCarteServiceID != null) {
        		
        	    // Call with preferredDate and preferredTime
        	    updatedServices = serviceCompletionService.updateServiceCompletion(
        	        subscriberID, 
        	        serviceID, 
        	        subscriberAlaCarteServiceID, 
        	        false, 
        	        parsedPreferredDate, 
        	        parsedPreferredTime,frequencyInstance
        	    );
        	} else {
        	    // Call with null for preferredDate and preferredTime
        		System.out.println("hhh");
        	    updatedServices = serviceCompletionService.updateServiceCompletion(
        	        subscriberID, 
        	        serviceID, 
        	        null, 
        	        false, 
        	        null, 
        	        null,frequencyInstance
        	    );
        	}
       }
        if (updatedServices != null) {
            try {
                String filePath = null;
                String fileUrl=null;
                if (file != null && !file.isEmpty()) {
                	// Fetch the adminUserID using the subscriberID
                    Integer adminUserID = subscriberService.getAdminUserIDBySubscriber(subscriberID); // Adjust the service name
                    // Build the folder path for the uploaded file
                    String uploadDirectory = "/home/saathi/tomcat/webapps/saathi_images/" + adminUserID + "/" + subscriberID + "/";
  //            String uploadDirectory="C:\\Users\\ether\\OneDrive\\Documents\\New folder\\"+ adminUserID + "\\" + subscriberID + "\\";
                    File directory = new File(uploadDirectory);
                    if (!directory.exists()) {
                        directory.mkdirs(); // Create the directory if it doesn't exist
                    }
                    // Define the file path and save it to the server
                    String fileName = file.getOriginalFilename();
                    filePath = uploadDirectory + fileName;
                    File dest = new File(filePath);
                    file.transferTo(dest);

                    // Construct the file URL for use in the response or storage
                     fileUrl = "https://saathi.etheriumtech.com:444/saathi_images/" + adminUserID + "/" + subscriberID + "/" + fileName;
                }
                // Step 4: Create and add interaction when the service is completed
                InteractionDTO interactionDTO = new InteractionDTO();
                interactionDTO.setSubscriberID(subscriberID);
                if(subscriberAlaCarteServiceID!=null) {
                	 interactionDTO.setSubscriberAlaCarteServicesID(subscriberAlaCarteServiceID);
                }
                if(frequencyInstance!=null) {
                	System.out.println("frequencyCount"+frequencyInstance);
               	 interactionDTO.setFrequencyInstance(frequencyInstance);
               }	
                interactionDTO.setDescription(description); // Get the description from the request payload
                interactionDTO.setDocuments(fileUrl);  // Store the file path in the documents attribute
                List<ServiceReport> services = updatedServices.get("allServices");
                if (services != null) {
                    for (ServiceReport service : services) {
                        // Check both serviceID and the type (Ala-carte or Package) to make sure it's the correct service
                        if (service.getServiceID() == serviceID) {
                        	 System.out.println("service.getPackageServiceID()"+service.getPackageServiceID());
                            // Check if the request indicates it's an Ala-carte service and match it with the existing service report
                            if (isAlaCarte && service.getSubscriberAlaCarteServicesID() != null && service.getSubscriberAlaCarteServicesID()==subscriberAlaCarteServiceID) {
                                // This is the matching ala-carte service
                                System.out.println("Processing ala-carte service...");
                               interactionDTO.setSubscriberAlaCarteServicesID(service.getSubscriberAlaCarteServicesID());
                 //               interactionDTO.setSubscriberAlaCarteServicesID(subscriberAlaCarteServiceID);
                                interactionDTO.setPackageServicesID(null); 
                                break;// Clear packageServiceID for ala-carte service
                            }
                          
                            // Check if it's a package service by ensuring isAlaCarte is false and there's a valid packageServiceID
                            else if (!isAlaCarte && service.getPackageServiceID() != null && service.getPackageServiceID() != 0) {
                                // This is the matching package service
                                System.out.println("Processing package service...");
                                interactionDTO.setPackageServicesID(service.getPackageServiceID()); // Set the packageServiceID
                                interactionDTO.setSubscriberAlaCarteServicesID(subscriberAlaCarteServiceID); 
                                break;// Clear ala-carte ID for package service
                            }
                            // Set the completion status only if the service is fully completed
                            interactionDTO.setCompletionStatus("Completed".equals(service.getCompletionStatus()) ? 1 : 0);
                            // Since the matching service is found, break the loop
                           
                        }
                    }
                }
                // Step 6: Save the interaction to the database
                interactionService.createInteraction(interactionDTO);
                // Return a success message
                return ResponseEntity.ok("Service completion updated successfully and interaction with file added.");
            } catch (IOException e) {
                e.printStackTrace();
                // If file upload fails, return an internal server error response
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
            }
        } else {
            // If the service is not found for the subscriber, return a 404 response
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found for the subscriber.");
        }
    }
   
    @PutMapping("/service/updateRequest")
    public ResponseEntity<?> updateServiceRequest(
            @RequestBody HashMap<String, Object> requestBody) {

        try {
            // Extract request details
            String preferredDate = (String) requestBody.get("preferredDate"); 
            String preferredTime = (String) requestBody.get("preferredTime");
            Integer subscriberID = (Integer) requestBody.get("subscriberID");
            Integer serviceID = (Integer) requestBody.get("serviceID");

            // Fetch the subscriber details
            Subscriber subscriber = subscriberRepository.findById(subscriberID)
                    .orElseThrow(() -> new RuntimeException("Subscriber not found with ID: " + subscriberID));
            
            // Get the packageID from the subscriber's package
            Integer packageID = subscriber.getSubscriptionPackage().getPackageID();
            
            // Fetch the services associated with the package
            List<PackageServices> packageServices = packageServiceRepository.findServicesByPackageId(packageID);
            
            // Variable to hold frequency count
            Integer frequencyCount = 0;
            PackageServices matchedPackageService = null;
            // Iterate through the services in the package and get the frequency for the matching service
            for (PackageServices packageService : packageServices) {
                if (packageService.getService().getServiceID().equals(serviceID)) {
                    frequencyCount = packageService.getFrequency();
                    matchedPackageService = packageService;
                    break;
                }
            }

            // Fetch the current frequency instance for the service (based on completed instances)
            int currentFrequencyInstance = subscriberAlaCarteServicesRepository.countBySubscriberIdAndServiceIdAndIsPackageServiceTrue(subscriberID, serviceID);

            System.out.println("currentFrequencyInstance"+currentFrequencyInstance);
            // Increment the frequencyInstance
            int frequencyInstance = currentFrequencyInstance + 1;

            // Count the existing interactions based on subscriberID and packageServiceID
            int interactionCount = interactionRepository.countBySubscriberIDAndPackageServices_PackageServicesID_CurrentMonth(
                    subscriberID, matchedPackageService.getPackageServicesID());

            // Set the frequencyInstance to be interactionCount + 1
             frequencyInstance = frequencyInstance + interactionCount ;
            // Ensure frequencyInstance doesn't exceed frequencyCount
            if (frequencyInstance > frequencyCount) {
                return ResponseEntity.badRequest()
                        .body("Frequency instance exceeds the allowed count for this service.");
            }

            // Parse the preferredDate and preferredTime from the input strings
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalDate parsedPreferredDate = LocalDate.parse(preferredDate, dateFormatter);
            LocalTime parsedPreferredTime = LocalTime.parse(preferredTime, timeFormatter);

            // Automatically set the createdDate using the system's current date and time
            LocalDateTime createdDate = LocalDateTime.now();

            // Call the service layer to update the service request
            Map<String, List<ServiceReport>> updatedServiceReportMap = serviceCompletionService.updateServiceRequestedDateTime(
                    subscriberID, serviceID, false, parsedPreferredDate, parsedPreferredTime, createdDate, frequencyInstance);

            System.out.println("updatedServiceReportMap" + updatedServiceReportMap);
            
            // Return the updated list of ServiceReports in the response
            return ResponseEntity.ok(updatedServiceReportMap);

        } catch (Exception e) {
            // Handle exceptions and return error message with HTTP status
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the service request.");
        }
    }


 // Endpoint to rebuild all services for a given subscriber
    @GetMapping("/rebuild/{subscriberID}")
    public ResponseEntity<Map<String, List<ServiceReport>>> rebuildAllServices(
            @PathVariable("subscriberID") int subscriberID) {
        try {
            // Call the service method to rebuild all services for the subscriber
            Map<String, List<ServiceReport>> allServicesMap = serviceCompletionService.rebuildAllServices(subscriberID);

            if (allServicesMap == null || allServicesMap.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // 204 No Content if no services found
            }

            // Return the result in the ResponseEntity with HTTP 200 OK
            return ResponseEntity.ok(allServicesMap);

        } catch (Exception e) {
            // Log the error (optional)
            e.printStackTrace();

            // Return an internal server error with message
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
        
    @GetMapping("/rebuild-all-subscribers")
    public ResponseEntity<Map<Integer, Map<String, List<ServiceReport>>>> rebuildForAllSubscribers() {
        try {
            // Step 1: Find all subscribers that purchased packages
            List<Integer> packageSubscriberIDs = subscriberRepository.findSubscribersWithPurchasedPackages();
            System.out.println("-"+packageSubscriberIDs);
            // Step 2: Find all subscribers that have ala-carte services but no interactions (untracked services)
            List<Integer> alaCarteSubscriberIDs = subscriberAlaCarteServicesRepository.findSubscribersWithUntrackedAlaCarteServices();
            // Combine the two lists of subscribers (using a Set to avoid duplicates)
            System.out.println("-"+alaCarteSubscriberIDs);
            Set<Integer> allSubscriberIDs = new HashSet<>();
            allSubscriberIDs.addAll(packageSubscriberIDs);
            allSubscriberIDs.addAll(alaCarteSubscriberIDs);

            // Step 3: Filter subscribers with incomplete services by comparing with Interaction records
            Set<Integer> subscribersWithIncompleteServices = new HashSet<>();
            for (Integer subscriberID : allSubscriberIDs) {
                // Step 3.1: Check package services completion status
                int totalPackageServices = packageServiceRepository.countServicesBySubscriber(subscriberID);
                int completedPackageServices = interactionRepository.countCompletedPackageServicesBySubscriber(subscriberID);
                System.out.println("-"+totalPackageServices);
                System.out.println("-"+completedPackageServices);
                if (completedPackageServices < totalPackageServices) {
                    subscribersWithIncompleteServices.add(subscriberID);
                }
                // Step 3.2: Check ala-carte services completion status
                int totalAlaCarteServices = subscriberAlaCarteServicesRepository.countAlaCarteServicesBySubscriber(subscriberID);
                int completedAlaCarteServices = interactionRepository.countCompletedAlaCarteServicesBySubscriber(subscriberID);
                if (completedAlaCarteServices < totalAlaCarteServices) {
                    subscribersWithIncompleteServices.add(subscriberID);
                }
            }
            
            // Step 4: Call rebuildAllServices for subscribers with incomplete services
            Map<Integer, Map<String, List<ServiceReport>>> allServicesMapForAllSubscribers = new HashMap<>();
            for (Integer subscriberID : subscribersWithIncompleteServices) {
                Map<String, List<ServiceReport>> allServicesMap = serviceCompletionService.rebuildAllServices(subscriberID);
                System.out.println("allServicesMap"+allServicesMap);
                allServicesMapForAllSubscribers.put(subscriberID, allServicesMap);
            }

            // Return the map with all subscribers' services
            return ResponseEntity.ok(allServicesMapForAllSubscribers);
        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam Integer otp) {
        try {
            int result = subscriberService.verifyOtp(email, otp);
            if (result == 1) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        } catch (SubscriberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during OTP verification. Please try again.");
        }
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completeRegistration(@RequestParam String email, @RequestBody SubscriberDTO additionalDetails) {
        try {
            SubscriberDTO response = subscriberService.completeRegistration(email, additionalDetails);
            // Send email notification to admin after successful creation
            String adminEmail = "suchigupta@etheriumtech.com"; // Replace with actual admin email
            String subject = "New Subscriber Added";
            Map<String, Object> model = new HashMap<>();
            
            // Create a Map for the subscriber data
            Map<String, Object> subscriberData = new HashMap<>();
            subscriberData.put("name", response.getFirstName() + " " + response.getLastName());

            // Add the subscriber map to the main model
            model.put("subscriber", subscriberData); // Pass the subscriber object

            // Assuming you have an email service that sends Freemarker templated emails
            emailService.sendEmail(adminEmail, subject, "subscriber-added-email.ftlh", model);
            
            return ResponseEntity.ok(response);
        } catch (SubscriberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during registration completion. Please try again.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerSubscriber(@RequestBody SubscriberDTO subscriberDTO) {
        try {
            SubscriberDTO response = subscriberService.createSubscriber(subscriberDTO);
            return ResponseEntity.ok(response);
        } catch (EmailAlreadyRegisteredException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
        	System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration. Please try again.");
        }
    }
       
    @GetMapping("/counts")
    public Map<String, Long> getSubscriberCounts() {
        Map<String, Long> subscriberCounts = new HashMap<>();
        subscriberCounts.put("activeBillingStatusZero", subscriberService.countActiveSubscribersWithBillingStatusZero());
        subscriberCounts.put("activeBillingStatusOne", subscriberService.countActiveSubscribersWithBillingStatusOne());
        subscriberCounts.put("inactiveSubscribers", subscriberService.countInactiveSubscribers());
        return subscriberCounts;
    }
           
    @GetMapping("/aggregate/{subscriberID}")
    public ResponseEntity<List<AggregatedServiceReport>> getAggregatedServiceReportsFromCache(@PathVariable int subscriberID) {
        try {
        	
            // Aggregate the service data based on packageServiceID from cache
            List<AggregatedServiceReport> aggregatedReports = serviceCompletionService.aggregateServiceDataByPackageServiceIDFromCache(subscriberID);

            // Return the aggregated data as the response
            return ResponseEntity.ok(aggregatedReports);

        } catch (Exception e) {
            // Handle any exceptions and return an error response
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @PostMapping("/create-order")
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, String> orderData) throws JsonMappingException, JsonProcessingException {
        // Extract subscriberId, orderAmount, and currency from the request data
    	 String packageID = orderData.get("packageID");
        String subscriberID = orderData.get("subscriberID");
        String currency = "INR";

        // Fetch subscriber details from the database
        SubscriberDTO subscriber = subscriberService.getSubscriberById(Integer.parseInt(subscriberID));
        if (subscriber == null) {
            return ResponseEntity.badRequest().body(null);  // Handle case when subscriber is not found
        }

     // Fetch package details using packageID
        SubscriptionPackageDTO subscriptionPackage = subscriptionPackageService.getSubscriptionPackageById(Integer.parseInt(packageID));
        if (subscriptionPackage == null) {
            return ResponseEntity.badRequest().body(null); // Handle case when package is not found
        }

        // Convert priceINR to String for orderAmount
        String orderAmount = subscriptionPackage.getPriceINR().toPlainString();
        
        // Create a new Order object and set initial details
        Order order = new Order();
        order.setSubscriberID(subscriber.getSubscriberID());
        order.setAmount(Double.parseDouble(orderAmount));
        order.setCurrency(currency != null ? currency : "INR"); // Default currency to INR if not provided
        order.setCreatedAt(new Date());

        // Save the initial order to generate orderID
        order = orderRepository.save(order);

        // Call the service to create the order and update it with response details
        Order updatedOrder = cashfreeService.createOrder(
            order.getOrderID(), 
            orderAmount, 
            subscriber.getSubscriberID(), 
            subscriber.getCountryCode() + subscriber.getContactNo(), 
            subscriber.getFirstName() + " " + subscriber.getLastName()
        );

        // Update the saved order with response details
        order.setCfOrderID(updatedOrder.getCfOrderID());
        order.setOrderStatus(updatedOrder.getOrderStatus());
        order.setPaymentSessionID(updatedOrder.getPaymentSessionID());
        order.setOrderExpiryTime(updatedOrder.getOrderExpiryTime());
        order.setUpdatedAt(new Date());  // Set the update time
        order.setPackageID(Integer.parseInt(packageID));
        // Save the updated order in the database
        order = orderRepository.save(order);

        // Return the populated Order object
        return ResponseEntity.ok(order);
    }


} 
