package controller;

import model.AdminUser;
import model.ServiceReport;
import model.dto.AdminUsersDTO;
import model.dto.SubscriberDTO;
import model.dto.SubscriberServicesDTO;
import service.AdminUsersService;
import service.EmailService;
import service.ServiceCompletionServiceNew;
import service.SubscriberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import exception.EmailAlreadyRegisteredException;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/admin-users")
public class AdminUsersController {

    private final AdminUsersService adminUsersService;
    
    private final SubscriberService subscriberService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private ServiceCompletionServiceNew serviceCompletionService;

 

    @Autowired
    public AdminUsersController(AdminUsersService adminUsersService, SubscriberService subscriberService) {
        this.adminUsersService = adminUsersService;
		this.subscriberService = subscriberService;
    }

    // GET: /admin-users
    @GetMapping
    public ResponseEntity<List<AdminUser>> getAllAdminUsers() {
        List<AdminUser> adminUsers = adminUsersService.getAllAdminUsers();
        return ResponseEntity.ok(adminUsers);
    }

    // GET: /admin-users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AdminUser> getAdminUserById(@PathVariable int id) {
        Optional<AdminUser> adminUser = adminUsersService.getAdminUserById(id);
        return adminUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

 // POST: /admin-users
    @PostMapping
    public ResponseEntity<?> createAdminUser(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam(value = "dob",required=false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob,

            @RequestParam("contactNo") String contactNo,
            @RequestParam("countryCode") String countryCode,
            @RequestParam(value="briefBio",required=false) String briefBio,
            @RequestParam("userType") String userType,
            @RequestParam("password") String password,
            @RequestParam("status") Integer status,
            @RequestParam("createdBy") Integer createdBy,
           
    		
            @RequestParam(value = "picture", required = false) MultipartFile picture) throws MessagingException, IOException, TemplateException {
    	
    	// Date format to convert Date to String
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Create AdminUsers instance and set fields from request parameters
    	try {
        AdminUser adminUser = new AdminUser();
        adminUser.setFirstName(firstName);
        adminUser.setLastName(lastName);
        adminUser.setEmail(email);
        String dobStr = dob != null ? dateFormat.format(dob) : null;
        adminUser.setDob(dobStr);

        adminUser.setContactNo(contactNo);
        adminUser.setCountryCode(countryCode);
        adminUser.setBriefBio(briefBio != null ? briefBio : "");

        adminUser.setUserType(userType);
        adminUser.setPassword(passwordEncoder.encode(password));
        adminUser.setStatus(status);
        adminUser.setCreatedBy(createdBy);
        System.out.println(dob);
       
	      // Handle picture upload if present
        if (picture != null && !picture.isEmpty()) {
            String fileName = picture.getOriginalFilename();
            String storageLocation = "/home/saathi/tomcat/webapps/saathi_images/" + fileName;
            try {
                picture.transferTo(new File(storageLocation));
                adminUser.setPicture(storageLocation);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        
        AdminUser createdAdminUser = adminUsersService.createAdminUser(adminUser);
        
        // Send email based on userType
        if ("Admin".equalsIgnoreCase(userType)) {
            // Send email with Admin template
            emailService.sendAdminEmail( password,createdAdminUser);
        } else if ("Saathi".equalsIgnoreCase(userType)) {
            // Send email with Saathi template
            emailService.sendSaathiEmail(password,createdAdminUser);
        }
        
        return ResponseEntity.ok(createdAdminUser);
    } catch (EmailAlreadyRegisteredException e) {
        // Handle the exception and return a custom message with a 409 Conflict status
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
    }

 // PUT: /admin-users/{id}
    @PostMapping("/{id}")
    public ResponseEntity<AdminUser> updateAdminUser(
            @PathVariable int id,
            @RequestParam(value="firstName",required=false) String firstName,
            @RequestParam(value="lastName",required=false) String lastName,
            @RequestParam(value="email",required=false) String email,
            @RequestParam(value = "dob",required=false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob,
            @RequestParam(value="contactNo",required=false) String contactNo,
            @RequestParam(value="countryCode",required=false) String countryCode,
            @RequestParam(value = "briefBio", required = false) String briefBio,
            @RequestParam(value="userType", required = false) String userType,
            @RequestParam(value="password", required = false) String password,
            @RequestParam(value="status", required = false) Integer status,
            @RequestParam(value="updatedBy", required = false) Integer updatedBy,
            @RequestParam(value = "picture", required = false) MultipartFile picture) {

        Optional<AdminUser> existingAdminUserOptional = adminUsersService.getAdminUserById(id);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (!existingAdminUserOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        AdminUser existingAdminUser = existingAdminUserOptional.get();

        // Update fields if they are not null
        if (firstName != null) {
            existingAdminUser.setFirstName(firstName);
        }
        if (lastName != null) {
            existingAdminUser.setLastName(lastName);
        }
        if (email != null) {
            existingAdminUser.setEmail(email);
        }
        if (dob != null) {
            String dobStr = dateFormat.format(dob);
            existingAdminUser.setDob(dobStr);
        }
        if (contactNo != null) {
            existingAdminUser.setContactNo(contactNo);
        }
        if (countryCode != null) {
            existingAdminUser.setCountryCode(countryCode);
        }
        if (briefBio != null) {
            existingAdminUser.setBriefBio(briefBio);
        }
        if (userType != null) {
        	
            existingAdminUser.setUserType(userType);
        }
        if (password != null) {
            existingAdminUser.setPassword(passwordEncoder.encode(password));
        }
        if (status != null) {
            existingAdminUser.setStatus(status);
        }
        if (updatedBy != null) {
            existingAdminUser.setUpdatedBy(updatedBy);
        }

        // Handle picture upload if present
        if (picture != null && !picture.isEmpty()) {
            String fileName = picture.getOriginalFilename();
            String storageLocation = "/home/saathi/tomcat/webapps/saathi_images/" + fileName;
            try {
                picture.transferTo(new File(storageLocation));
                existingAdminUser.setPicture(storageLocation);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        AdminUser updatedAdminUser = adminUsersService.updateAdminUser(existingAdminUser);
        return ResponseEntity.ok(updatedAdminUser);
    }

    // DELETE: /admin-users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminUser(@PathVariable int id) {
        Optional<AdminUser> adminUser = adminUsersService.getAdminUserById(id);
        if (adminUser.isPresent()) {
            adminUsersService.deleteAdminUser(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
 // GET: /admin-users/active
    @GetMapping("/active")
    public ResponseEntity<List<AdminUser>> getActiveAdminUsers() {
        List<AdminUser> activeUsers = adminUsersService.getActiveAdminUsers();
        return ResponseEntity.ok(activeUsers);
    }
    
    
    @GetMapping("/{saathiId}/subscribers")
    public ResponseEntity<List<SubscriberDTO>> getSubscribersBySaathi(@PathVariable int saathiId) {
        List<SubscriberDTO> subscribers = subscriberService.getSubscribersBySaathi(saathiId);
        return ResponseEntity.ok(subscribers);
    }
    
    @GetMapping("/saathi")
    public List<AdminUser> getSaathiUsers() {
        return adminUsersService.getAllSaathiUsers();
    }
    
    @GetMapping("/saathi/subscribers")
    public ResponseEntity<List<AdminUsersDTO>> getAllSaathiAdminUsersWithSubscribers() {
        List<AdminUsersDTO> adminUsers = adminUsersService.getAllAdminUsersWithSubscribersByUserType();
        return ResponseEntity.ok(adminUsers);
    }
    
    @GetMapping("/{saathiId}/subscribers/services")
    public ResponseEntity<?> getSubscribersServicesBySaathi(@PathVariable int saathiId) {
        try {
            // Fetch the list of subscribers for the given Saathi (AdminUser)
            List<SubscriberDTO> subscribers = subscriberService.getSubscribersBySaathiID(saathiId);

            // Check if the list of subscribers is null or empty
            if (subscribers == null || subscribers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No subscribers found for Saathi with ID: " + saathiId);
            }

            // Create a list to hold each subscriber's services DTO
            List<SubscriberServicesDTO> subscriberServicesDTOList = new ArrayList<>();

            // Loop through each subscriber and fetch their services
            for (SubscriberDTO subscriber : subscribers) {
                // Fetch the services for each subscriber
                Map<String, List<ServiceReport>> services = serviceCompletionService.getSubscriberServices(subscriber.getSubscriberID());

                // Check if the services exist and are valid
                if (services != null && services.containsKey("allServices")) {
                    List<ServiceReport> serviceReports = services.get("allServices");

                    // Filter out services where packageServiceID is not null
                    List<ServiceReport> filteredServices = new ArrayList<>();
                    for (ServiceReport serviceReport : serviceReports) {
                        if (serviceReport.getPackageServiceID() == null) { // Only process services where packageServiceID is null
                            
                            // Add color attribute based on requestedDate and requestedTime
                            if (serviceReport.getRequestedDate() != null && serviceReport.getRequestedTime() != null) {
                                // Combine requested date and time into a LocalDateTime
                                LocalDateTime requestedDateTime = LocalDateTime.of(
                                        LocalDate.parse(serviceReport.getRequestedDate().toString()), // Parse the requested date
                                        LocalTime.parse(serviceReport.getRequestedTime().toString())  // Parse the requested time
                                );

                                // Get the current time
                                LocalDateTime currentTime = LocalDateTime.now(ZoneId.systemDefault());

                                // Calculate the duration between the current time and the requested time
                                Duration duration = Duration.between(currentTime, requestedDateTime);
                                long hours = duration.toHours();

                                if (hours >= 0 && hours <= 24) {
                                    // Requested date/time is within the next 24 hours
                                    serviceReport.setColor("green");
                                } else if (hours > 24) {
                                    // Requested date/time is more than 24 hours in the future
                                    serviceReport.setColor("amber");
                                } else {
                                    // Requested date/time is in the past
                                    serviceReport.setColor("red");
                                }
                            } else {
                                serviceReport.setColor("no color"); // Handle case where requestedDate or requestedTime is missing
                            }

                            filteredServices.add(serviceReport); // Add the service to the filtered list
                        }
                    }

                    if (!filteredServices.isEmpty()) {
                        // Create and add the DTO to the list
                        SubscriberServicesDTO dto = new SubscriberServicesDTO(
                                subscriber.getSubscriberID(),
                                subscriber.getFirstName(),
                                filteredServices
                        );
                        subscriberServicesDTOList.add(dto);
                    }
                }
            }

            // If no services were found for any subscribers
            if (subscriberServicesDTOList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No services found for any subscribers under Saathi with ID: " + saathiId);
            }

            // Return the list containing subscriber services DTO
            return ResponseEntity.ok(subscriberServicesDTOList);
        } catch (Exception e) {
            // Log the error and return a response with status 500
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving services for Saathi ID: " + saathiId);
        }
    }

}