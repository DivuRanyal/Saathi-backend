package controller;

import model.AdminUser;
import model.dto.AdminUsersDto;
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
    public ResponseEntity<AdminUser> createAdminUser(
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
       
        // Handle picture upload 
		if (picture != null && !picture.isEmpty()) {
			String fileName = picture.getOriginalFilename();
	//		String storageLocation = "C:\\Users\\ether\\OneDrive\\Documents\\PROJECT\\" + fileName;
			String storageLocation = "/home/saathi/tomcat/webapps/saathi_images/" + fileName;

			try {
				picture.transferTo(new File(storageLocation));
				adminUser.setPicture(storageLocation);
			} catch (IOException e) {
				return ResponseEntity.status(500).body(null);
			}
		}
        
        AdminUser createdAdminUser = adminUsersService.createAdminUser(adminUser);
        return ResponseEntity.ok(createdAdminUser);
    }

 // PUT: /admin-users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AdminUser> updateAdminUser(@PathVariable int id, @ModelAttribute AdminUsersDto adminUserUpdateDto) {
        Optional<AdminUser> existingAdminUserOptional = adminUsersService.getAdminUserById(id);

        if (existingAdminUserOptional.isPresent()) {
            AdminUser existingAdminUser = existingAdminUserOptional.get();

            // Update only the fields that are provided in the DTO
            if (adminUserUpdateDto.getFirstName() != null) {
                existingAdminUser.setFirstName(adminUserUpdateDto.getFirstName());
            }
            if (adminUserUpdateDto.getLastName() != null) {
                existingAdminUser.setLastName(adminUserUpdateDto.getLastName());
            }
            if (adminUserUpdateDto.getEmail() != null) {
                existingAdminUser.setEmail(adminUserUpdateDto.getEmail());
            }
            if (adminUserUpdateDto.getDob() != null) {
                existingAdminUser.setDob(adminUserUpdateDto.getDob());
            }
            if (adminUserUpdateDto.getContactNo() != null) {
                existingAdminUser.setContactNo(adminUserUpdateDto.getContactNo());
            }
            if (adminUserUpdateDto.getCountryCode() != null) {
                existingAdminUser.setCountryCode(adminUserUpdateDto.getCountryCode());
            }
            if (adminUserUpdateDto.getBriefBio() != null) {
                existingAdminUser.setBriefBio(adminUserUpdateDto.getBriefBio());
            }
            if (adminUserUpdateDto.getUserType() != null) {
                existingAdminUser.setUserType(adminUserUpdateDto.getUserType());
            }
            if (adminUserUpdateDto.getPassword() != null) {
                existingAdminUser.setPassword(adminUserUpdateDto.getPassword());
            }
            if (adminUserUpdateDto.getStatus() != null) {
                existingAdminUser.setStatus(adminUserUpdateDto.getStatus());
            }
            
            if (adminUserUpdateDto.getUpdatedBy() != null) {
                // Set the updatedBy based on your application's logic
                 existingAdminUser.setUpdatedBy(adminUserUpdateDto.getUpdatedBy());
            }

            // Handle picture upload if present
            MultipartFile file = adminUserUpdateDto.getPicture();
            if (file != null && !file.isEmpty()) {
                String fileName = file.getOriginalFilename();
  //              String storageLocation = "C:\\Users\\ether\\OneDrive\\Documents\\PROJECT\\" + fileName;
                String storageLocation = "/home/saathi/tomcat/webapps/saathi_images/" + fileName;
                try {
                    file.transferTo(new File(storageLocation));
                    existingAdminUser.setPicture(storageLocation);
                } catch (IOException e) {
                    return ResponseEntity.status(500).body(null);
                }
            }

            AdminUser updatedAdminUser = adminUsersService.updateAdminUser(existingAdminUser);
            return ResponseEntity.ok(updatedAdminUser);
        } else {
            return ResponseEntity.notFound().build();
        }
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
}
