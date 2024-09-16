package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import model.dto.PackageServiceDTO;
import model.dto.SubscriptionPackageDTO;
import service.SubscriptionPackageService;

import java.util.List;

@RestController
@RequestMapping("/subscription-package")
public class SubscriptionPackageController {

    @Autowired
    private SubscriptionPackageService subscriptionPackageService;

    @PostMapping
    public ResponseEntity<SubscriptionPackageDTO> createSubscriptionPackage(@RequestBody SubscriptionPackageDTO subscriptionPackageDTO) {
        SubscriptionPackageDTO createdPackage = subscriptionPackageService.createSubscriptionPackage(subscriptionPackageDTO);
        return ResponseEntity.ok(createdPackage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPackageDTO> updateSubscriptionPackage(@PathVariable Integer id, @RequestBody SubscriptionPackageDTO subscriptionPackageDTO) {
        SubscriptionPackageDTO updatedPackage = subscriptionPackageService.updateSubscriptionPackage(id, subscriptionPackageDTO);
        return ResponseEntity.ok(updatedPackage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPackageDTO> getSubscriptionPackageById(@PathVariable Integer id) {
        SubscriptionPackageDTO packageDTO = subscriptionPackageService.getSubscriptionPackageById(id);
        if (packageDTO != null) {
            return ResponseEntity.ok(packageDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubscriptionPackageDTO>> getAllSubscriptionPackages() {
        List<SubscriptionPackageDTO> packages = subscriptionPackageService.getAllSubscriptionPackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SubscriptionPackageDTO>> getActiveSubscriptionPackages() {
        List<SubscriptionPackageDTO> activePackages = subscriptionPackageService.getActiveSubscriptionPackages();
        return ResponseEntity.ok(activePackages);
    }
    
    @GetMapping("/{packageId}/services")
    public ResponseEntity<List<PackageServiceDTO>> getPackageServices(@PathVariable Integer packageId) {
        List<PackageServiceDTO> services = subscriptionPackageService.getPackageServicesByPackageId(packageId);
        return ResponseEntity.ok(services);
    }
}
