package service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.dto.SubscriberServiceDetailsDTO;
import repository.InteractionRepository;
import service.InteractionService;

import java.util.List;

@Service  // Ensure this annotation is present
public class InteractionServiceImpl implements InteractionService {

    private final InteractionRepository interactionRepository;

    @Autowired
    public InteractionServiceImpl(InteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    @Override
    public List<SubscriberServiceDetailsDTO> getAlaCarteServicesWithCompletionStatusAndServiceDetails(Integer subscriberID) {
        return interactionRepository.findAlaCarteServicesWithCompletionStatusAndServiceDetails(subscriberID);
    }
}
