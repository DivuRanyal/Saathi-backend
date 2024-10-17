package service;

import java.util.List;

import model.Interaction;
import model.dto.InteractionDTO;
import model.dto.SubscriberServiceDetailsDTO;

public interface InteractionService {
   List<SubscriberServiceDetailsDTO> getAlaCarteServicesWithCompletionStatusAndServiceDetails(Integer subscriberID);
    InteractionDTO createInteraction(InteractionDTO interactionDTO);
    InteractionDTO updateInteraction(Integer id, InteractionDTO interactionDTO);
    void deleteInteraction(Integer id);
    List<InteractionDTO> getInteractionsBySubscriberID(Integer id);
    List<InteractionDTO> getAllInteractions();
    Interaction rateInteraction(int interactionID, int serviceRating);
}
