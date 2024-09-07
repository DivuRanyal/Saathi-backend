package controller;

import java.util.List;

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
        
    @GetMapping
    public ResponseEntity<List<SubscriptionPackageDTO>> getAllSubscriptionPackages() {
        // Call the service to get all subscription packages and their services
        List<SubscriptionPackageDTO> packageDTOs = subscriptionPackageService.getAllSubscriptionPackagesWithServices();

        if (!packageDTOs.isEmpty()) {
            return new ResponseEntity<>(packageDTOs, HttpStatus.OK);  // Return OK if packages are found
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Return NO_CONTENT if no packages are found
        }
    }

    @GetMapping("/{packageId}/services")
    public ResponseEntity<List<PackageServiceDTO>> getPackageServices(@PathVariable Integer packageId) {
        List<PackageServiceDTO> services = subscriptionPackageService.getPackageServicesByPackageId(packageId);
        return ResponseEntity.ok(services);
    }
}
