package controller;

import model.dto.AlaCarteServiceDTO;
import service.AlaCarteServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alacarteservices")
public class AlaCarteServiceController {

    @Autowired
    private AlaCarteServicesService alaCarteServicesService;

    @GetMapping
    public ResponseEntity<List<AlaCarteServiceDTO>> getAllServices() {
        List<AlaCarteServiceDTO> services = alaCarteServicesService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlaCarteServiceDTO> getServiceById(@PathVariable Integer id) {
        AlaCarteServiceDTO service = alaCarteServicesService.getServiceById(id);
        return service != null ? ResponseEntity.ok(service) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<AlaCarteServiceDTO> createService(@RequestBody AlaCarteServiceDTO alaCarteServiceDTO) {
    	System.out.println(alaCarteServiceDTO.getCreatedBy());
        AlaCarteServiceDTO createdService = alaCarteServicesService.createService(alaCarteServiceDTO);
        return ResponseEntity.ok(createdService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlaCarteServiceDTO> updateService(@PathVariable Integer id, @RequestBody AlaCarteServiceDTO alaCarteServiceDTO) {
        AlaCarteServiceDTO updatedService = alaCarteServicesService.updateService(id, alaCarteServiceDTO);
        return updatedService != null ? ResponseEntity.ok(updatedService) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        alaCarteServicesService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<AlaCarteServiceDTO>> getActiveServices() {
        List<AlaCarteServiceDTO> activeServices = alaCarteServicesService.getActiveServices();
        return ResponseEntity.ok(activeServices);
    }
}
