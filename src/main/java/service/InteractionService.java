package service;

import java.util.List;

import model.dto.InteractionDTO;
import model.dto.SubscriberServiceDetailsDTO;

public interface InteractionService {
   List<SubscriberServiceDetailsDTO> getAlaCarteServicesWithCompletionStatusAndServiceDetails(Integer subscriberID);
    InteractionDTO createInteraction(InteractionDTO interactionDTO);
    InteractionDTO updateInteraction(Integer id, InteractionDTO interactionDTO);
    void deleteInteraction(Integer id);
    InteractionDTO getInteractionById(Integer id);
    List<InteractionDTO> getAllInteractions();
}
