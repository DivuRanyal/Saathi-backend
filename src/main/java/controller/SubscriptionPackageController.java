package controller;

import model.SubscriptionPackage;
import service.SubscriptionPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/packages")
public class SubscriptionPackageController {

    private final SubscriptionPackageService subscriptionPackageService;

    @Autowired
    public SubscriptionPackageController(SubscriptionPackageService subscriptionPackageService) {
        this.subscriptionPackageService = subscriptionPackageService;
    }

    @GetMapping
    public List<SubscriptionPackage> getAllPackages() {
        return subscriptionPackageService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPackage> getSubscriptionPackage(@PathVariable int id) {
        SubscriptionPackage subscriptionPackage = subscriptionPackageService.getSubscriptionPackageById(id);
        return ResponseEntity.ok(subscriptionPackage);
    }

    @PostMapping
    public SubscriptionPackage createPackage(@RequestBody SubscriptionPackage subscriptionPackage) {
        return subscriptionPackageService.save(subscriptionPackage);
    }

    @PutMapping("/{id}")
    public SubscriptionPackage updatePackage(@PathVariable int id, @RequestBody SubscriptionPackage subscriptionPackage) {
        return subscriptionPackageService.update(id, subscriptionPackage);
    }

    @DeleteMapping("/{id}")
    public void deletePackage(@PathVariable int id) {
        subscriptionPackageService.deleteById(id);
    }
    
    
}
