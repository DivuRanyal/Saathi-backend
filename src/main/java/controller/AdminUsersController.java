package controller;

import model.AdminUser;
import model.ServiceReport;
import model.Subscriber;
import model.SubscriptionPackage;
import model.dto.AdminUsersDTO;
import model.dto.CombinedSaathiSubscriberDTO;
import model.dto.PackageDetailDTO;
import model.dto.SaathiServiceCountDTO;
import model.dto.SaathiServiceSummaryDTO;
import model.dto.ServiceCountDTO;
import model.dto.ServiceSummaryDTO;
import model.dto.SubscriberDTO;
import model.dto.SubscriberSaathiDTO;
import model.dto.SubscriberServicesDTO;
import model.dto.SubscriptionPackageDTO;
import service.AdminUsersService;
import service.EmailService;
import service.ServiceCompletionServiceNew;
import service.SubscriberService;
import service.SubscriptionPackageService;

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
import java.util.stream.Collectors;

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
    private SubscriptionPackageService subscriptionPackageService;
 

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
       
	    AdminUser createdAdminUser = adminUsersService.createAdminUser(adminUser);
        
	 // Handle picture upload if present
        if (picture != null && !picture.isEmpty()) {
            // Create a directory for the admin using the admin_id
            String adminIdFolder = "/home/saathi/tomcat/webapps/saathi_images/" + createdAdminUser.getAdminUserID();
            File directory = new File(adminIdFolder);
            if (!directory.exists()) {
                directory.mkdirs();  // Create the directory if it doesn't exist
            }

            // Save the file in the new directory
            String fileName = picture.getOriginalFilename();
            String storageLocation = adminIdFolder + "/" + fileName;
            try {
                picture.transferTo(new File(storageLocation));
                // Store only the relative path in the database
                adminUser.setPicture(createdAdminUser.getAdminUserID() + "/" + fileName);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        // Update the adminUser with the picture path
        adminUsersService.updateAdminUser(adminUser);

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
            
            // Create a directory for the admin using the admin_id
            String adminIdFolder = "/home/saathi/tomcat/webapps/saathi_images/" + existingAdminUser.getAdminUserID();
            File directory = new File(adminIdFolder);
            if (!directory.exists()) {
                directory.mkdirs();  // Create the directory if it doesn't exist
            }

            // Save the file in the new directory
            String storageLocation = adminIdFolder + "/" + fileName;
            try {
                picture.transferTo(new File(storageLocation));
                String baseUrl = "https://saathi.etheriumtech.com:444/saathi_images/";  // Base URL for the images
                // Store only the relative path in the database
    //            String baseUrl = "http://uat.etheriumtech.com:8080/saathi_images/";
                existingAdminUser.setPicture(baseUrl + existingAdminUser.getAdminUserID() + "/" + fileName);
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
                    
                    // Filter out services where packageServiceID is not null and only include valid services
                    List<ServiceReport> filteredServices = new ArrayList<>();
                    for (ServiceReport serviceReport : serviceReports) {
                        if (serviceReport.getPackageServiceID() == null && serviceReport.getServiceName() != null  && !"Completed".equals(serviceReport.getCompletionStatus())) { // Check for valid services
                            System.out.println(serviceReport.getPackageName());
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
                           
                            filteredServices.add(serviceReport); // Add the valid service to the filtered list
                        }
                    }
                    System.out.println("filteredServices.size()"+filteredServices.size());
                    // Only add the subscriber if they have valid filtered services
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
    @GetMapping("/saathis/subscribers/services")
    public ResponseEntity<?> getSaathisWithSubscribersAndServices() {
        try {
            // Fetch the list of all Saathis (AdminUsers with userType "Saathi")
            List<AdminUser> saathis = adminUsersService.getAllSaathiUsers();

            // Check if there are any Saathis
            if (saathis == null || saathis.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Saathi found.");
            }

            // Create a list to hold each Saathi with their subscribers and services
            List<Map<String, Object>> saathiSubscribersList = new ArrayList<>();

            // Loop through each Saathi and fetch their subscribers and services
            for (AdminUser saathi : saathis) {
                int saathiId = saathi.getAdminUserID();
                List<SubscriberDTO> subscribers = subscriberService.getSubscribersBySaathiID(saathiId);

                // Create a map to hold Saathi and their subscribers
                Map<String, Object> saathiData = new HashMap<>();
                saathiData.put("AdminUserID", saathiId);
                saathiData.put("saathiFirstName", saathi.getFirstName());
                saathiData.put("saathiLastName", saathi.getLastName());
                saathiData.put("email", saathi.getEmail());

                List<Map<String, Object>> subscriberServicesList = new ArrayList<>(); // List to hold subscribers and their services

                for (SubscriberDTO subscriberDTO : subscribers) {
                    // Fetch services for the subscriber
                    Map<String, List<ServiceReport>> services = serviceCompletionService.getSubscriberServices(subscriberDTO.getSubscriberID());

                    if (services != null && services.containsKey("allServices")) {
                        List<ServiceReport> serviceReports = services.get("allServices");

                        List<Map<String, Object>> filteredServices = new ArrayList<>();
                        for (ServiceReport serviceReport : serviceReports) {
                            Map<String, Object> serviceData = new HashMap<>();
                            serviceData.put("serviceID", serviceReport.getServiceID());
                            serviceData.put("serviceName", serviceReport.getServiceName());
                            serviceData.put("packageName", serviceReport.getPackageName());
                            serviceData.put("packageServiceID", serviceReport.getPackageServiceID());
                            serviceData.put("frequency", serviceReport.getFrequency());
                            serviceData.put("frequencyUnit", serviceReport.getFrequencyUnit());
                            serviceData.put("completionStatus", serviceReport.getCompletionStatus());

                            // Add color attribute based on requested date/time
                            if (serviceReport.getRequestedDate() != null && serviceReport.getRequestedTime() != null) {
                                LocalDateTime requestedDateTime = LocalDateTime.of(
                                        LocalDate.parse(serviceReport.getRequestedDate().toString()),
                                        LocalTime.parse(serviceReport.getRequestedTime().toString())
                                );

                                LocalDateTime currentTime = LocalDateTime.now(ZoneId.systemDefault());
                                Duration duration = Duration.between(currentTime, requestedDateTime);
                                long hours = duration.toHours();

                                if (hours >= 0 && hours <= 24) {
                                    serviceReport.setColor("green");
                                } else if (hours > 24) {
                                    serviceReport.setColor("amber");
                                } else {
                                    serviceReport.setColor("red");
                                }
                            } else {
                                serviceReport.setColor("no color");
                            }

                            serviceData.put("color", serviceReport.getColor());
                            serviceData.put("requestedDate", serviceReport.getRequestedDate());
                            serviceData.put("requestedTime", serviceReport.getRequestedTime());

                            filteredServices.add(serviceData);
                        }

                        // Add subscriber and their services
                        Map<String, Object> subscriberData = new HashMap<>();
                        subscriberData.put("subscriberID", subscriberDTO.getSubscriberID());
                        subscriberData.put("subscriberName", subscriberDTO.getFirstName());
                        subscriberData.put("services", filteredServices);

                        subscriberServicesList.add(subscriberData);
                    }
                }

                // Add the subscribers and services to the current Saathi
                saathiData.put("subscribers", subscriberServicesList);
                saathiSubscribersList.add(saathiData);
            }

            return ResponseEntity.ok(saathiSubscribersList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving services for Saathis.");
        }
    }

    @GetMapping("/{saathiId}/subscribers/services/all")
    public ResponseEntity<?> getAllSubscribersServicesBySaathi(@PathVariable int saathiId) {
        try {
            // Fetch the list of subscribers for the given Saathi (AdminUser)
            List<SubscriberDTO> subscribers = subscriberService.getSubscribersBySaathiID(saathiId);

            // Fetch all available subscription packages
            List<SubscriptionPackageDTO> allPackages = subscriptionPackageService.getActiveSubscriptionPackages();

            // Check if the list of subscribers is null or empty
            if (subscribers == null || subscribers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No subscribers found for Saathi with ID: " + saathiId);
            }

            // Initialize counters and maps to track services and their status
            int totalPending = 0;
            int totalCompleted = 0;
            int totalServices = 0;
            int totalSubscribers = subscribers.size(); // Track the total number of subscribers

            // Counter to track the number of subscribers with billingStatus = 0
            int inActiveSubscribers = 0;

            // A map to hold service type breakdown (e.g., Phone Call, House Errand, etc.)
            Map<String, ServiceCountDTO> serviceBreakdown = new HashMap<>();
            
            // A map to hold the breakdown of subscription packages and their subscriber count
            Map<String, Integer> packageSubscriberCount = new HashMap<>();

            // Initialize the packageSubscriberCount map with all available packages, setting their count to 0
            for (SubscriptionPackageDTO subscriptionPackage : allPackages) {
                packageSubscriberCount.put(subscriptionPackage.getPackageName(), 0); // Default count is 0
            }

            // Loop through each subscriber and fetch their services
            for (SubscriberDTO subscriber : subscribers) {
                // Get the name of the package the subscriber is subscribed to
                String packageName = subscriber.getPackageName();
                
                // Update the package subscriber count
                if (packageName != null) {
                    packageSubscriberCount.put(packageName, packageSubscriberCount.getOrDefault(packageName, 0) + 1);
                }

                // Check if billingStatus is 0 for this subscriber and increment the counter
                if (subscriber.getBillingStatus() == 0) {
                	inActiveSubscribers++;
                }

                // Fetch the services for each subscriber
                Map<String, List<ServiceReport>> services = serviceCompletionService.getSubscriberServices(subscriber.getSubscriberID());

                // Check if the services exist and are valid
                if (services != null && services.containsKey("allServices")) {
                    List<ServiceReport> serviceReports = services.get("allServices");

                    // Loop through each service and process it
                    for (ServiceReport serviceReport : serviceReports) {
                        if (serviceReport.getFrequencyCount() != 0) {
                            totalServices += serviceReport.getFrequencyCount();  // Count every service

                            // Determine if the service is completed or pending
                            if ("Completed".equals(serviceReport.getCompletionStatus())) {
                                totalCompleted++;
                            } else {
                                totalPending++;
                            }

                            // Get the service name (e.g., "Phone Call", "House Errand") and track it in the breakdown map
                            String serviceName = serviceReport.getServiceName();
                            ServiceCountDTO countDTO = serviceBreakdown.getOrDefault(serviceName, new ServiceCountDTO(serviceName));

                            // Update the counts for this particular service type
                            if ("Completed".equals(serviceReport.getCompletionStatus())) {
                                countDTO.incrementCompleted();
                            } else {
                                countDTO.incrementPending();
                            }

                            // Update the map with the new counts
                            serviceBreakdown.put(serviceName, countDTO);
                        }
                    }
                }
            }

            // Create a list of package details to send in the response
            List<PackageDetailDTO> packageDetails = packageSubscriberCount.entrySet().stream()
                    .map(entry -> new PackageDetailDTO(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            // Create the response object
            ServiceSummaryDTO summary = new ServiceSummaryDTO(
                    totalServices,
                    totalPending,
                    totalCompleted,
                    new ArrayList<>(serviceBreakdown.values()),
                    totalSubscribers, // Include total number of subscribers
                    packageDetails,  // Include package subscriber breakdown
                    inActiveSubscribers // Include the count of subscribers with billingStatus = 0
            );

            // Return the result
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            // Log the error and return a response with status 500
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving services for Saathi ID: " + saathiId);
        }
    }

    @GetMapping("/saathi/counts")
    public Map<String, Long> getSaathiCounts() {
        Map<String, Long> saathiCounts = new HashMap<>();
        long assignedSaathi = adminUsersService.countAssignedSaathi();
        long unassignedSaathi = adminUsersService.countUnassignedSaathi();
        long totalSaathi = assignedSaathi + unassignedSaathi;

        saathiCounts.put("assignedSaathi", assignedSaathi);
        saathiCounts.put("unassignedSaathi", unassignedSaathi);
        saathiCounts.put("totalSaathi", totalSaathi);

        return saathiCounts;
    }

    
    @GetMapping("/subscribers/services/all/saathis")
    public ResponseEntity<?> getAllSaathiSubscribersServices() {
        try {
            // Fetch the list of admin users of userType 'Saathi'
            List<AdminUser> saathiUsers = adminUsersService.getAllSaathiUsers();

            // Fetch all available subscription packages
            List<SubscriptionPackageDTO> allPackages = subscriptionPackageService.getActiveSubscriptionPackages();

            // Initialize the overall counters and data holders
            int totalServices = 0;
            int totalSubscribers = 0;

            // A list to hold Saathi-specific breakdowns
            List<SaathiServiceCountDTO> saathiServiceCountList = new ArrayList<>();

            // A map to hold the breakdown of subscription packages and their subscriber count
            Map<String, Integer> packageSubscriberCount = new HashMap<>();

            // Initialize the packageSubscriberCount map with all available packages, setting their count to 0
            for (SubscriptionPackageDTO subscriptionPackage : allPackages) {
                packageSubscriberCount.put(subscriptionPackage.getPackageName(), 0); // Default count is 0
            }

            // Loop through each Saathi user and fetch their subscribers
            for (AdminUser saathi : saathiUsers) {
                List<SubscriberDTO> subscribers = subscriberService.getSubscribersBySaathiID(saathi.getAdminUserID());

                // Initialize counters for this specific Saathi
                int saathiPendingPackageServices = 0;
                int saathiCompletedPackageServices = 0;
                int saathiPendingAlaCarteServices = 0;
                int saathiCompletedAlaCarteServices = 0;

                // Check if the list of subscribers is null or empty
                if (subscribers == null || subscribers.isEmpty()) {
                    continue; // Skip this Saathi if no subscribers found
                }

                totalSubscribers += subscribers.size(); // Track the total number of subscribers

                // Loop through each subscriber and fetch their services
                for (SubscriberDTO subscriber : subscribers) {
                    // Get the name of the package the subscriber is subscribed to
                    String packageName = subscriber.getPackageName();

                    // Update the package subscriber count
                    if (packageName != null) {
                        packageSubscriberCount.put(packageName, packageSubscriberCount.getOrDefault(packageName, 0) + 1);
                    }

                    // Fetch the services for each subscriber
                    Map<String, List<ServiceReport>> services = serviceCompletionService.getSubscriberServices(subscriber.getSubscriberID());

                    // Check if the services exist and are valid
                    if (services != null && services.containsKey("allServices")) {
                        List<ServiceReport> serviceReports = services.get("allServices");

                        // Loop through each service and process it
                        for (ServiceReport serviceReport : serviceReports) {
                            if (serviceReport.getFrequencyCount() != 0) {
                                totalServices += serviceReport.getFrequencyCount();  // Count every service occurrence based on frequencyCount

                                // Determine if the service is a package or ala-carte service
                                if (!serviceReport.isAlaCarte()) {
                                    // Package service logic
                                    if ("Not Started".equals(serviceReport.getCompletionStatus())) {
                                    	saathiPendingPackageServices += serviceReport.getFrequencyCount();  // All pending if not completed
                                        
                                        } else {
                                        	saathiCompletedPackageServices += serviceReport.getCompletions();  // Completed services based on actual completions
                                            saathiPendingPackageServices += (serviceReport.getFrequencyCount() - serviceReport.getCompletions());  // Remaining are pending
                                        }
                                } else {
                                    // Ala-carte service logic
                                    if ("Not Started".equals(serviceReport.getCompletionStatus())) {
                                    	saathiPendingAlaCarteServices += serviceReport.getFrequencyCount();  // All ala-carte services pending if not completed
                                        
                                        } else {
                                        	saathiCompletedAlaCarteServices += serviceReport.getCompletions();  // Completed ala-carte services
                                            saathiPendingAlaCarteServices += (serviceReport.getFrequencyCount() - serviceReport.getCompletions());  // Remaining ala-carte services pending
                                         }
                                }
                            }
                        }

                    }
                }

                // After processing all subscribers for this Saathi, create a DTO for the counts
                SaathiServiceCountDTO saathiServiceCountDTO = new SaathiServiceCountDTO(
                    saathi.getFirstName()+saathi.getLastName(), 
                    saathiPendingPackageServices,
                    saathiCompletedPackageServices,
                    saathiPendingAlaCarteServices,
                    saathiCompletedAlaCarteServices
                );

                // Add this Saathi's counts to the overall list
                saathiServiceCountList.add(saathiServiceCountDTO);
            }

            // Create a list of package details to send in the response
            List<PackageDetailDTO> packageDetails = packageSubscriberCount.entrySet().stream()
                    .map(entry -> new PackageDetailDTO(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            // Create the response object with combined Saathi-specific data
            SaathiServiceSummaryDTO summary = new SaathiServiceSummaryDTO(
                    totalServices,
                    totalSubscribers, 
                    saathiServiceCountList, // List of Saathi's service counts
                    packageDetails  // Include package subscriber breakdown
            );

            // Return the result
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            // Log the error and return a response with status 500
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving services for all Saathi users.");
        }
    }

    @GetMapping("/combined/saathi-subscriber-counts")
    public ResponseEntity<?> getCombinedSaathiSubscriberCounts() {
        try {
            CombinedSaathiSubscriberDTO combinedDto = new CombinedSaathiSubscriberDTO();

            // Call the Saathi counts API logic
            Map<String, Long> saathiCounts = new HashMap<>();
            long assignedSaathi = adminUsersService.countAssignedSaathi();
            long unassignedSaathi = adminUsersService.countUnassignedSaathi();
            long totalSaathi = assignedSaathi + unassignedSaathi;
            saathiCounts.put("assignedSaathi", assignedSaathi);
            saathiCounts.put("unassignedSaathi", unassignedSaathi);
            saathiCounts.put("totalSaathi", totalSaathi);
            combinedDto.setSaathiCounts(saathiCounts);

            // Initialize totals for all Saathi
            int totalPendingPackageServices = 0;
            int totalCompletedPackageServices = 0;
            int totalPendingAlaCarteServices = 0;
            int totalCompletedAlaCarteServices = 0;

            // Call the Saathi subscriber services API logic
            List<AdminUser> saathiUsers = adminUsersService.getAllSaathiUsers();
            for (AdminUser saathi : saathiUsers) {
                List<SubscriberDTO> subscribers = subscriberService.getSubscribersBySaathiID(saathi.getAdminUserID());

                for (SubscriberDTO subscriber : subscribers) {
                    Map<String, List<ServiceReport>> services = serviceCompletionService.getSubscriberServices(subscriber.getSubscriberID());
                    if (services != null && services.containsKey("allServices")) {
                        List<ServiceReport> serviceReports = services.get("allServices");

                        for (ServiceReport serviceReport : serviceReports) {
                            if (!serviceReport.isAlaCarte()) {
                                // Package services
                                if ("Not Started".equals(serviceReport.getCompletionStatus())) {
                                    totalPendingPackageServices += serviceReport.getFrequencyCount();
                                } else {
                                    totalCompletedPackageServices += serviceReport.getCompletions();
                                    totalPendingPackageServices += (serviceReport.getFrequencyCount() - serviceReport.getCompletions());
                                }
                            } else {
                                // Ala-carte services
                                if ("Not Started".equals(serviceReport.getCompletionStatus())) {
                                    totalPendingAlaCarteServices += serviceReport.getFrequencyCount();
                                } else {
                                    totalCompletedAlaCarteServices += serviceReport.getCompletions();
                                    totalPendingAlaCarteServices += (serviceReport.getFrequencyCount() - serviceReport.getCompletions());
                                }
                            }
                        }
                    }
                }
            }

            // Set the calculated values
            combinedDto.setTotalPendingPackageServices(totalPendingPackageServices);
            combinedDto.setTotalCompletedPackageServices(totalCompletedPackageServices);
            combinedDto.setTotalPendingAlaCarteServices(totalPendingAlaCarteServices);
          combinedDto.setTotalCompletedAlaCarteServices(totalCompletedAlaCarteServices);

            // Calculate total services
            combinedDto.calculateTotalServices();

            // Call the subscriber counts API logic
            Map<String, Long> subscriberCounts = new HashMap<>();
            long registeredSubscribers = subscriberService.countActiveSubscribersWithBillingStatusZero();
            long completedSubscribers = subscriberService.countActiveSubscribersWithBillingStatusOne();
            subscriberCounts.put("RegisteredUsers", registeredSubscribers);
            subscriberCounts.put("CompletedUsers", completedSubscribers);
            subscriberCounts.put("TotalSubscribers", registeredSubscribers + completedSubscribers);
            combinedDto.setSubscriberCounts(subscriberCounts);
            System.out.println(combinedDto);
            // Return the combined data
            return ResponseEntity.ok(combinedDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while retrieving combined counts.");
        }
    }

}