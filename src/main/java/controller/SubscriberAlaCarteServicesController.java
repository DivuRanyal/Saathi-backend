package controller;

import model.SubscriberAlaCarteServices;
import service.SubscriberAlaCarteServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriber-services")
public class SubscriberAlaCarteServicesController {

    @Autowired
    private SubscriberAlaCarteServicesService service;

    @GetMapping
    public ResponseEntity<List<SubscriberAlaCarteServices>> getAllServices() {
        List<SubscriberAlaCarteServices> services = service.getAllServices();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriberAlaCarteServices> getServiceById(@PathVariable int id) {
        SubscriberAlaCarteServices serviceById = service.getServiceById(id);
        if (serviceById != null) {
            return new ResponseEntity<>(serviceById, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<SubscriberAlaCarteServices> createService(@RequestBody SubscriberAlaCarteServices serviceRequest) {
    	 System.out.println(serviceRequest);
        SubscriberAlaCarteServices createdService = service.createOrUpdateService(serviceRequest);
        System.out.println(createdService);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriberAlaCarteServices> updateService(@PathVariable int id, @RequestBody SubscriberAlaCarteServices serviceRequest) {
        SubscriberAlaCarteServices existingService = service.getServiceById(id);
        if (existingService != null) {
            serviceRequest.setSubscriberAlaCarteServicesID(id); // Ensure the ID matches the path variable
            SubscriberAlaCarteServices updatedService = service.createOrUpdateService(serviceRequest);
            return new ResponseEntity<>(updatedService, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable int id) {
        SubscriberAlaCarteServices existingService = service.getServiceById(id);
        if (existingService != null) {
            service.deleteService(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
