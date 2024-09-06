package controller;

import model.dto.PatronCreationResponse;
import model.dto.PatronDTO;
import service.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import exception.EmailAlreadyRegisteredException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/patrons")
public class PatronController {

    @Autowired
    private PatronService patronService;

    @GetMapping
    public ResponseEntity<List<PatronDTO>> getAllPatrons() {
        List<PatronDTO> patrons = patronService.getAllPatrons();
        return new ResponseEntity<>(patrons, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatronDTO> getPatronById(@PathVariable("id") int patronId) {
        PatronDTO patron = patronService.getPatronById(patronId);
        return patron != null ? new ResponseEntity<>(patron, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/subscriber/{subscriberID}")
    public ResponseEntity<List<PatronDTO>> getPatronsBySubscriberId(@PathVariable("subscriberID") int subscriberID) {
        List<PatronDTO> patrons = patronService.getPatronsBySubscriberId(subscriberID);
        return new ResponseEntity<>(patrons, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> createPatrons(@RequestBody List<PatronDTO> patronDTOs) {
        List<PatronDTO> createdPatrons = new ArrayList<>();
        List<String> conflictMessages = new ArrayList<>();
        
        for (PatronDTO patronDTO : patronDTOs) {
            try {
                PatronDTO createdPatron = patronService.savePatron(patronDTO);
                createdPatrons.add(createdPatron);
            } catch (EmailAlreadyRegisteredException e) {
                conflictMessages.add("Email already registered: " + patronDTO.getEmail());
            }
        }
        
        if (!conflictMessages.isEmpty()) {
            // Return a 207 Multi-Status response if some patrons were created and others had conflicts
            return ResponseEntity.status(HttpStatus.MULTI_STATUS)
                    .body(new PatronCreationResponse(createdPatrons, conflictMessages));
        }
        
        return new ResponseEntity<>(createdPatrons, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PatronDTO> updatePatron(@PathVariable("id") int patronId, @RequestBody PatronDTO patronDTO) {
        PatronDTO updatedPatron = patronService.updatePatron(patronId, patronDTO);
        return updatedPatron != null ? new ResponseEntity<>(updatedPatron, HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatron(@PathVariable("id") int patronId) {
        patronService.deletePatron(patronId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
