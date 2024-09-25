package controller;

import model.ServiceReport;
import model.SubscriberAlaCarteServices;
import service.ServiceCompletionServiceNew;
import service.SubscriberAlaCarteServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subscriber-services")
public class SubscriberAlaCarteServicesController {

    @Autowired
    private SubscriberAlaCarteServicesService service;

    @Autowired
    private ServiceCompletionServiceNew serviceCompletionService;

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
        // Create or update the service
 //       System.out.println(serviceRequest.getSubscriberID());
        SubscriberAlaCarteServices createdService = service.createOrUpdateService(serviceRequest);
       
        // Call the trackSubscriberServices method automatically after service creation
        Map<String, List<ServiceReport>> trackedServices = serviceCompletionService.trackSubscriberServices(
                createdService.getSubscriberID(), 
                0, 
                createdService.getSubscriberAlaCarteServicesID()
        );
        
        if (trackedServices == null || trackedServices.isEmpty()) {
            return new ResponseEntity<>(createdService, HttpStatus.CREATED);
        }

        // You can return tracked services in the response or handle as needed
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
