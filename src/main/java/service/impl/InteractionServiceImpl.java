package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.Interaction;
import model.PackageServices;
import model.SubscriberAlaCarteServices;  // Make sure to import this
import model.dto.InteractionDTO;
import model.dto.SubscriberServiceDetailsDTO;
import repository.InteractionRepository;
import repository.PackageServiceRepository;
import repository.SubscriberAlaCarteServicesRepository;  // Make sure to import this repository to fetch the SubscriberAlaCarteServices entity
import service.InteractionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

@Service  // Ensure this annotation is present
public class InteractionServiceImpl implements InteractionService {

    private final InteractionRepository interactionRepository;
    private final PackageServiceRepository packageServiceRepository; // Inject the repository to fetch
    private final SubscriberAlaCarteServicesRepository subscriberAlaCarteServicesRepository; // Inject the repository to fetch the entity

    @Autowired
    public InteractionServiceImpl(
        InteractionRepository interactionRepository, 
        SubscriberAlaCarteServicesRepository subscriberAlaCarteServicesRepository, 
        PackageServiceRepository packageServiceRepository // Correct parameter name
    ) {
        this.interactionRepository = interactionRepository;
        this.subscriberAlaCarteServicesRepository = subscriberAlaCarteServicesRepository;
        this.packageServiceRepository = packageServiceRepository; // Correct initialization
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
        // Fetch and set the PackageServices entity
        if (interactionDTO.getPackageServicesID() != null) {
            PackageServices packageServices = packageServiceRepository.findById(interactionDTO.getPackageServicesID())
                .orElseThrow(() -> new RuntimeException("PackageServices not found for ID: " + interactionDTO.getPackageServicesID()));
            interaction.setPackageServices(packageServices);
        }

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
 //   	PackageServices packageServices = packageServiceRepository.findById(dto.getPackageServicesID())
 //   		    .orElseThrow(() -> new EntityNotFoundException("PackageServices not found for ID: " + dto.getPackageServicesID()));

    		// Set the fetched PackageServices entity in the Interaction object
    	// Check if PackageServicesID is not null before fetching from repository
        if (dto.getPackageServicesID() != null) {
            PackageServices packageServices = packageServiceRepository.findById(dto.getPackageServicesID())
                    .orElseThrow(() -> new EntityNotFoundException("PackageServices not found for ID: " + dto.getPackageServicesID()));
            interaction.setPackageServices(packageServices); // Set the PackageServices entity in Interaction
        } else {
            interaction.setPackageServices(null); // Set to null if no PackageServicesID is provided
        }
       
        interaction.setSubscriberID(dto.getSubscriberID());
        interaction.setSaathiID(dto.getSaathiID());
        interaction.setInteractionType(dto.getInteractionType());
        interaction.setDocuments(dto.getDocuments());
        interaction.setCreatedDate(dto.getCreatedDate());
        interaction.setLastUpdatedDate(dto.getLastUpdatedDate());
        interaction.setCompletionStatus(dto.getCompletionStatus());
       
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
     // Set PackageServicesID only if PackageServices is not null
        if (interaction.getPackageServices() != null) {
            dto.setPackageServicesID(interaction.getPackageServices().getPackageServicesID());
        } else {
            dto.setPackageServicesID(null); // Set to null or handle it according to your logic
        }

        dto.setDescription(interaction.getDescription());

        // Map the `subscriberAlaCarteServicesID` from the Interaction entity
        if (interaction.getSubscriberAlaCarteServices() != null) {
            dto.setSubscriberAlaCarteServicesID(interaction.getSubscriberAlaCarteServices().getSubscriberAlaCarteServicesID());
        }

        return dto;
    }
}
