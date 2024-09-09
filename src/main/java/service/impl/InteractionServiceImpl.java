package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Interaction;
import model.SubscriberAlaCarteServices;  // Make sure to import this
import model.dto.InteractionDTO;
import model.dto.SubscriberServiceDetailsDTO;
import repository.InteractionRepository;
import repository.SubscriberAlaCarteServicesRepository;  // Make sure to import this repository to fetch the SubscriberAlaCarteServices entity
import service.InteractionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service  // Ensure this annotation is present
public class InteractionServiceImpl implements InteractionService {

    private final InteractionRepository interactionRepository;
    private final SubscriberAlaCarteServicesRepository subscriberAlaCarteServicesRepository; // Inject the repository to fetch the entity

    @Autowired
    public InteractionServiceImpl(InteractionRepository interactionRepository, SubscriberAlaCarteServicesRepository subscriberAlaCarteServicesRepository) {
        this.interactionRepository = interactionRepository;
        this.subscriberAlaCarteServicesRepository = subscriberAlaCarteServicesRepository;
    }

    @Override
    public List<SubscriberServiceDetailsDTO> getAlaCarteServicesWithCompletionStatusAndServiceDetails(Integer subscriberID) {
        // Query should now resolve the `subscriberAlaCarteServices` mapping properly
        return interactionRepository.findAlaCarteServicesWithCompletionStatusAndServiceDetails(subscriberID);
    }
    
    @Override
    public InteractionDTO createInteraction(InteractionDTO interactionDTO) {
        Interaction interaction = convertToEntity(interactionDTO);
        interaction.setCreatedDate(LocalDateTime.now());
        interactionRepository.save(interaction);
        return convertToDTO(interaction);
    }

    @Override
    public InteractionDTO updateInteraction(Integer id, InteractionDTO interactionDTO) {
        Interaction interaction = interactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Interaction not found"));
        interaction.setLastUpdatedDate(LocalDateTime.now());
        
        // Update fields from DTO
        interaction.setSubscriberID(interactionDTO.getSubscriberID());
        interaction.setSaathiID(interactionDTO.getSaathiID());
        interaction.setInteractionType(interactionDTO.getInteractionType());
        interaction.setDocuments(interactionDTO.getDocuments());
        interaction.setCompletionStatus(interactionDTO.getCompletionStatus());
        interaction.setPackageServicesID(interactionDTO.getPackageServicesID());
        interaction.setDescription(interactionDTO.getDescription());
        
        // Fetch the SubscriberAlaCarteServices entity by its ID
        SubscriberAlaCarteServices subscriberAlaCarteServices = subscriberAlaCarteServicesRepository
            .findById(interactionDTO.getSubscriberAlaCarteServicesID())
            .orElseThrow(() -> new RuntimeException("SubscriberAlaCarteServices not found"));
        
        interaction.setSubscriberAlaCarteServices(subscriberAlaCarteServices);  // Corrected relationship
        interactionRepository.save(interaction);
        return convertToDTO(interaction);
    }

    @Override
    public void deleteInteraction(Integer id) {
        interactionRepository.deleteById(id);
    }

    @Override
    public InteractionDTO getInteractionById(Integer id) {
        Interaction interaction = interactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Interaction not found"));
        return convertToDTO(interaction);
    }

    @Override
    public List<InteractionDTO> getAllInteractions() {
        return interactionRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Converts InteractionDTO to Interaction entity
    private Interaction convertToEntity(InteractionDTO dto) {
        Interaction interaction = new Interaction();
        interaction.setSubscriberID(dto.getSubscriberID());
        interaction.setSaathiID(dto.getSaathiID());
        interaction.setInteractionType(dto.getInteractionType());
        interaction.setDocuments(dto.getDocuments());
        interaction.setCreatedDate(dto.getCreatedDate());
        interaction.setLastUpdatedDate(dto.getLastUpdatedDate());
        interaction.setCompletionStatus(dto.getCompletionStatus());
        interaction.setPackageServicesID(dto.getPackageServicesID());
        interaction.setDescription(dto.getDescription());
        
        // Fetch the SubscriberAlaCarteServices entity by its ID
        if (dto.getSubscriberAlaCarteServicesID() != null) {
            SubscriberAlaCarteServices subscriberAlaCarteServices = subscriberAlaCarteServicesRepository
                .findById(dto.getSubscriberAlaCarteServicesID())
                .orElseThrow(() -> new RuntimeException("SubscriberAlaCarteServices not found"));
            interaction.setSubscriberAlaCarteServices(subscriberAlaCarteServices);
        }

        return interaction;
    }

    // Converts Interaction entity to InteractionDTO
    private InteractionDTO convertToDTO(Interaction interaction) {
        InteractionDTO dto = new InteractionDTO();
        dto.setInteractionID(interaction.getInteractionID());
        dto.setSubscriberID(interaction.getSubscriberID());
        dto.setSaathiID(interaction.getSaathiID());
        dto.setInteractionType(interaction.getInteractionType());
        dto.setDocuments(interaction.getDocuments());
        dto.setCreatedDate(interaction.getCreatedDate());
        dto.setLastUpdatedDate(interaction.getLastUpdatedDate());
        dto.setCompletionStatus(interaction.getCompletionStatus());
        dto.setPackageServicesID(interaction.getPackageServicesID());
        dto.setDescription(interaction.getDescription());

        // Map the `subscriberAlaCarteServicesID` from the Interaction entity
        if (interaction.getSubscriberAlaCarteServices() != null) {
            dto.setSubscriberAlaCarteServicesID(interaction.getSubscriberAlaCarteServices().getSubscriberAlaCarteServicesID());
        }

        return dto;
    }
}
