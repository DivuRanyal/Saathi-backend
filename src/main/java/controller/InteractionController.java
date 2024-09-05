package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.dto.SubscriberServiceDetailsDTO;
import service.InteractionService;

import java.util.List;

@RestController
@RequestMapping("/interactions")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;

    @GetMapping("/alacarte-services/{subscriberID}")
    public ResponseEntity<List<SubscriberServiceDetailsDTO>> getAlaCarteServicesWithCompletionStatusAndServiceDetails(@PathVariable Integer subscriberID) {
        List<SubscriberServiceDetailsDTO> result = interactionService.getAlaCarteServicesWithCompletionStatusAndServiceDetails(subscriberID);
        return ResponseEntity.ok(result);
    }
    
    
}
