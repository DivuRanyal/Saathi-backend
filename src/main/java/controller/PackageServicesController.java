package controller;

import model.PackageServices;
import service.PackageServicesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packageServices")
public class PackageServicesController {

    @Autowired
    private PackageServicesService packageServicesService;

    // Get all Package Services
    @GetMapping
    public ResponseEntity<List<PackageServices>> getAllPackageServices() {
        List<PackageServices> packageServicesList = packageServicesService.getAllPackageServices();
        if (packageServicesList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(packageServicesList, HttpStatus.OK);
    }

    // Get Package Service by ID
    @GetMapping("/{id}")
    public ResponseEntity<PackageServices> getPackageServicesById(@PathVariable Integer id) {
        PackageServices packageServices = packageServicesService.getPackageServicesById(id);
        if (packageServices == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(packageServices, HttpStatus.OK);
    }
    
    
}
