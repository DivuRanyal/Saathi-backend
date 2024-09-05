package service;

import java.util.List;

import model.dto.SubscriberServiceDetailsDTO;

public interface InteractionService {
    List<SubscriberServiceDetailsDTO> getAlaCarteServicesWithCompletionStatusAndServiceDetails(Integer subscriberID);
}
