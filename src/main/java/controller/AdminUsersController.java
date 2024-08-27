package controller;

import model.AdminUsers;
import model.dto.AdminUsersDto;
import service.AdminUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin-users")
public class AdminUsersController {

    private final AdminUsersService adminUsersService;

    @Autowired
    public AdminUsersController(AdminUsersService adminUsersService) {
        this.adminUsersService = adminUsersService;
    }

    // GET: /admin-users
    @GetMapping
    public ResponseEntity<List<AdminUsers>> getAllAdminUsers() {
        List<AdminUsers> adminUsers = adminUsersService.getAllAdminUsers();
        return ResponseEntity.ok(adminUsers);
    }

    // GET: /admin-users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AdminUsers> getAdminUserById(@PathVariable int id) {
        Optional<AdminUsers> adminUser = adminUsersService.getAdminUserById(id);
        return adminUser.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

 // POST: /admin-users
    @PostMapping
    public ResponseEntity<AdminUsers> createAdminUser(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam(value = "DOB",required=false) Date dob,
            @RequestParam("contactNo") String contactNo,
            @RequestParam("countryCode") String countryCode,
            @RequestParam(value="briefBio",required=false) String briefBio,
            @RequestParam("userType") String userType,
            @RequestParam("password") String password,
            @RequestParam("status") Integer status,
            @RequestParam("createdBy") Integer createdBy,
           
    		
            @RequestParam(value = "picture", required = false) MultipartFile picture) {
        // Create AdminUsers instance and set fields from request parameters
        AdminUsers adminUser = new AdminUsers();
        adminUser.setFirstName(firstName);
        adminUser.setLastName(lastName);
        adminUser.setEmail(email);
 //       adminUser.setDob(dob);
        adminUser.setContactNo(contactNo);
        adminUser.setCountryCode(countryCode);
        adminUser.setBriefBio(briefBio != null ? briefBio : "");

        adminUser.setUserType(userType);
        adminUser.setPassword(password);
        adminUser.setStatus(status);
        adminUser.setCreatedBy(createdBy);
      
       
        // Handle picture upload as you have implemented
        // ...
        
        AdminUsers createdAdminUser = adminUsersService.createAdminUser(adminUser);
        return ResponseEntity.ok(createdAdminUser);
    }

 // PUT: /admin-users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<AdminUsers> updateAdminUser(@PathVariable int id, @ModelAttribute AdminUsersDto adminUserUpdateDto) {
        Optional<AdminUsers> existingAdminUserOptional = adminUsersService.getAdminUserById(id);

        if (existingAdminUserOptional.isPresent()) {
            AdminUsers existingAdminUser = existingAdminUserOptional.get();

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
            if (adminUserUpdateDto.getCreatedBy() != null) {
                // Set the createdBy based on your application's logic
                // existingAdminUser.setCreatedBy(adminUserUpdateDto.getCreatedBy());
            }
            if (adminUserUpdateDto.getUpdatedBy() != null) {
                // Set the updatedBy based on your application's logic
                // existingAdminUser.setUpdatedBy(adminUserUpdateDto.getUpdatedBy());
            }

            // Handle picture upload if present
            MultipartFile file = adminUserUpdateDto.getPicture();
            if (file != null && !file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                String storageLocation = "C:\\Users\\ether\\OneDrive\\Documents\\PROJECT\\" + fileName;

                try {
                    file.transferTo(new File(storageLocation));
                    existingAdminUser.setPicture(storageLocation);
                } catch (IOException e) {
                    return ResponseEntity.status(500).body(null);
                }
            }

            AdminUsers updatedAdminUser = adminUsersService.updateAdminUser(existingAdminUser);
            return ResponseEntity.ok(updatedAdminUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: /admin-users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminUser(@PathVariable int id) {
        Optional<AdminUsers> adminUser = adminUsersService.getAdminUserById(id);
        if (adminUser.isPresent()) {
            adminUsersService.deleteAdminUser(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
 // GET: /admin-users/active
    @GetMapping("/active")
    public ResponseEntity<List<AdminUsers>> getActiveAdminUsers() {
        List<AdminUsers> activeUsers = adminUsersService.getActiveAdminUsers();
        return ResponseEntity.ok(activeUsers);
    }
}
