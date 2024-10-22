package controller;

import model.AlaCarteService;
import model.ServiceReport;
import model.Subscriber;
import model.SubscriberAlaCarteServices;
import model.dto.SubscriberAlaCarteServicesDTO;
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
    public ResponseEntity<SubscriberAlaCarteServices> createService(@RequestBody SubscriberAlaCarteServicesDTO serviceRequest) {
        // Convert DTO to entity
        SubscriberAlaCarteServices entity = mapDTOToEntity(serviceRequest);

        // Validate and save the service
        SubscriberAlaCarteServices createdService = service.createOrUpdateService(entity);

        // Automatically track subscriber services
       Map<String, List<ServiceReport>> trackedServices = serviceCompletionService.trackSubscriberServices(
                createdService.getSubscriber().getSubscriberID(),
                0,
                createdService.getSubscriberAlaCarteServicesID(),
                createdService.getServiceDate(),
                createdService.getServiceTime()
        );
        
 //       Map<String, List<ServiceReport>> trackedServices = serviceCompletionService.rebuildAllServices(createdService.getSubscriber().getSubscriberID());
        System.out.println("allServicesMap"+trackedServices);
       
        // If tracking is not needed or empty, return the created service
        if (trackedServices == null || trackedServices.isEmpty()) {
            return new ResponseEntity<>(createdService, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    private SubscriberAlaCarteServices mapDTOToEntity(SubscriberAlaCarteServicesDTO dto) {
        SubscriberAlaCarteServices entity = new SubscriberAlaCarteServices();
        
        // Mapping fields from DTO to entity
        entity.setSubscriberAlaCarteServicesID(dto.getSubscriberAlaCarteServicesID());
        entity.setServiceID(dto.getServiceID());
        entity.setServiceDate(dto.getServiceDate());
        entity.setServiceTime(dto.getServiceTime());
        entity.setBillingStatus(dto.getBillingStatus());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setLastUpdatedDate(dto.getLastUpdatedDate());
        entity.setIsAccepted(dto.getIsAccepted());
        entity.setIsPackageService(dto.getIsPackageService());
        
        // Setting subscriber object based on subscriberID
        Subscriber subscriber = new Subscriber();
        subscriber.setSubscriberID(dto.getSubscriberID());
        entity.setSubscriber(subscriber);
        
        // Similarly, you can handle AlaCarteService if needed
        AlaCarteService service = new AlaCarteService();
        service.setServiceID(dto.getServiceID());
        entity.setService(service);

        return entity;
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
