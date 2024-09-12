package controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import model.SubscriptionPackage;
import model.dto.PackageServiceDTO;
import model.dto.SubscriptionPackageDTO;
import service.SubscriptionPackageService;

@RestController
@RequestMapping("/subscription-package")
public class SubscriptionPackageController {

    @Autowired
    private SubscriptionPackageService subscriptionPackageService;

    @PostMapping
    public ResponseEntity<SubscriptionPackage> createSubscriptionPackage(
            @RequestBody SubscriptionPackageDTO packageDTO) {

        SubscriptionPackage createdPackage = subscriptionPackageService.saveSubscriptionPackageWithServices(packageDTO);

        return ResponseEntity.ok(createdPackage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPackage> updateSubscriptionPackage(
            @PathVariable("id") Integer packageId, @RequestBody SubscriptionPackageDTO packageDTO) {

        SubscriptionPackage updatedPackage = subscriptionPackageService.updateSubscriptionPackageWithServices(packageId, packageDTO);

        return ResponseEntity.ok(updatedPackage);
    }
    
    @GetMapping("/{packageId}")
    public ResponseEntity<SubscriptionPackageDTO> getSubscriptionPackageById(@PathVariable Integer packageId) {
        // Call the service to get the subscription package and its services
        SubscriptionPackageDTO packageDTO = subscriptionPackageService.getSubscriptionPackageWithServices(packageId);

        if (packageDTO != null) {
            return new ResponseEntity<>(packageDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
        
    @GetMapping("/all")
    public ResponseEntity<List<SubscriptionPackageDTO>> getAllSubscriptionPackages() {
        // Call the service to get all subscription packages and their services
        List<SubscriptionPackageDTO> packageDTOs = subscriptionPackageService.getAllSubscriptionPackagesWithServices();

        System.out.println(packageDTOs);
        // Filter out packages with empty or null packageServices
        List<SubscriptionPackageDTO> filteredPackages = packageDTOs.stream()
            .filter(packageDTO -> packageDTO.getPackageServices() != null && !packageDTO.getPackageServices().isEmpty())
            .collect(Collectors.toList());

        System.out.println(filteredPackages);
        if (!filteredPackages.isEmpty()) {
            return new ResponseEntity<>(filteredPackages, HttpStatus.OK);  // Return OK if packages are found
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Return NO_CONTENT if no packages are found
        }
    }


    @GetMapping("/active")
    public ResponseEntity<List<SubscriptionPackageDTO>> getActiveSubscriptionPackages() {
        // Get all subscription packages and their services
        List<SubscriptionPackageDTO> packageDTOs = subscriptionPackageService.getAllSubscriptionPackagesWithServices();

        // Filter out packages with empty or null packageServices and filter packageServices where status=1
        List<SubscriptionPackageDTO> filteredPackages = packageDTOs.stream()
            .map(this::filterActiveServices)  // Use helper method to filter active services
            .filter(packageDTO -> packageDTO.getPackageServices() != null && !packageDTO.getPackageServices().isEmpty()) // Ensure there are still services left
            .collect(Collectors.toList());

        if (!filteredPackages.isEmpty()) {
            return new ResponseEntity<>(filteredPackages, HttpStatus.OK);  // Return OK if packages are found
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Return NO_CONTENT if no packages are found
        }
    }

    private SubscriptionPackageDTO filterActiveServices(SubscriptionPackageDTO packageDTO) {
        // Filter package services where status = 1
        List<PackageServiceDTO> activePackageServices = packageDTO.getPackageServices().stream()
            .filter(serviceDTO -> serviceDTO.getStatus() != null && serviceDTO.getStatus() == 1) // Only services with status = 1
            .collect(Collectors.toList());

        // Set the filtered services back into the packageDTO
        packageDTO.setPackageServices(activePackageServices);

        return packageDTO;
    }


    @GetMapping("/{packageId}/services")
    public ResponseEntity<List<PackageServiceDTO>> getPackageServices(@PathVariable Integer packageId) {
        List<PackageServiceDTO> services = subscriptionPackageService.getPackageServicesByPackageId(packageId);
        return ResponseEntity.ok(services);
    }
}
