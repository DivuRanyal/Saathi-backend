package controller;

import model.AdminUser;
import model.dto.AdminUsersDTO;
import model.dto.SubscriberDTO;
import service.AdminUsersService;
import service.SubscriberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import exception.EmailAlreadyRegisteredException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin-users")
public class AdminUsersController {

    private final AdminUsersService adminUsersService;
    
    private final SubscriberService subscriberService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


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
            @RequestParam(value = "dob") 
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob,

            @RequestParam("contactNo") String contactNo,
            @RequestParam("countryCode") String countryCode,
            @RequestParam(value="briefBio",required=false) String briefBio,
            @RequestParam("userType") String userType,
            @RequestParam("password") String password,
            @RequestParam("status") Integer status,
            @RequestParam("createdBy") Integer createdBy,
           
    		
            @RequestParam(value = "picture", required = false) MultipartFile picture) {
    	
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
}


