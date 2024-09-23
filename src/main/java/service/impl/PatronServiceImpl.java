package service.impl;

import model.dto.PatronDTO;
import model.Patron;
import model.Subscriber;
import repository.PatronRepository;
import repository.SubscriberRepository;
import service.PatronService;
 // Assuming you have this service to fetch Subscriber
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import exception.EmailAlreadyRegisteredException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatronServiceImpl implements PatronService {

    @Autowired
    private PatronRepository patronRepository;

    @Autowired
    private SubscriberRepository subscriberRepository;
    
    @Override
    public List<PatronDTO> getAllPatrons() {
        return patronRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PatronDTO getPatronById(int patronId) {
        return patronRepository.findById(patronId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public PatronDTO savePatron(PatronDTO patronDTO) {
        // Check if email is already registered
   /*     Optional<Patron> existingPatron = patronRepository.findByEmail(patronDTO.getEmail());
        if (existingPatron.isPresent()) {
            // Handle the case where the email is already registered
        	throw new EmailAlreadyRegisteredException("The email address is already registered.");

            // Alternatively, you could return a special DTO or handle it in another way.
        }
     */   
        Patron patron = convertToEntity(patronDTO);
        Patron savedPatron = patronRepository.save(patron);
        return convertToDTO(savedPatron);
    }

    @Override
    public PatronDTO updatePatron(int patronId, PatronDTO patronDTO) {
        Optional<Patron> existingPatron = patronRepository.findById(patronId);
        if (existingPatron.isPresent()) {
            Patron patronToUpdate = convertToEntity(patronDTO);
            patronToUpdate.setPatronID(existingPatron.get().getPatronID());
            Patron updatedPatron = patronRepository.save(patronToUpdate);
            return convertToDTO(updatedPatron);
        }
        return null;
    }

    @Override
    public void deletePatron(int patronId) {
        if (patronRepository.existsById(patronId)) {
            patronRepository.deleteById(patronId);
        }
    }

    @Override
    public List<PatronDTO> getPatronsBySubscriberId(int subscriberID) {
        return patronRepository.findBySubscriber_SubscriberID(subscriberID).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Conversion methods

    private PatronDTO convertToDTO(Patron patron) {
        PatronDTO dto = new PatronDTO();
        dto.setPatronID(patron.getPatronID());
        dto.setFirstName(patron.getFirstName());
        dto.setLastName(patron.getLastName());
        dto.setEmail(patron.getEmail());
        dto.setContactNo(patron.getContactNo());
        dto.setCountryCode(patron.getCountryCode());
        dto.setDob(patron.getDob());
        dto.setComments(patron.getComments());
        // Safely handle the case where the Subscriber might be null
        if (patron.getSubscriber() != null) {
            dto.setSubscriberID(patron.getSubscriber().getSubscriberID());
        } else {
            dto.setSubscriberID(null);
        }

        dto.setAddress1(patron.getAddress1());
        dto.setAddress2(patron.getAddress2());
        dto.setCity(patron.getCity());
        dto.setState(patron.getState());
        dto.setCountry(patron.getCountry());
        dto.setRelation(patron.getRelation());
        dto.setCreatedDate(patron.getCreatedDate());
        dto.setLastUpdatedDate(patron.getLastUpdatedDate());
        dto.setCreatedBy(patron.getCreatedBy());
        dto.setUpdatedBy(patron.getUpdatedBy());
        return dto;
    }

    private Patron convertToEntity(PatronDTO dto) {
        Patron patron = new Patron();
        patron.setPatronID(dto.getPatronID());
        patron.setFirstName(dto.getFirstName());
        patron.setLastName(dto.getLastName());
        patron.setEmail(dto.getEmail());
        patron.setContactNo(dto.getContactNo());
        patron.setCountryCode(dto.getCountryCode());
        patron.setDob(dto.getDob());

     // Directly fetch Subscriber entity from the repository
        if (dto.getSubscriberID() != null) {
            Subscriber subscriber = subscriberRepository.findById(dto.getSubscriberID()).orElse(null);
            patron.setSubscriber(subscriber);
        } else {
            patron.setSubscriber(null);
        }
        patron.setAddress1(dto.getAddress1());
        patron.setAddress2(dto.getAddress2());
        patron.setCity(dto.getCity());
        patron.setState(dto.getState());
        patron.setCountry(dto.getCountry());
        patron.setRelation(dto.getRelation());
        patron.setComments(dto.getComments());
        patron.setCreatedBy(dto.getCreatedBy());
        patron.setUpdatedBy(dto.getUpdatedBy());
        return patron;
    }
    
    @Override
    public PatronDTO getPatronBySubscriberId(int subscriberId) {
        Patron patron = patronRepository.findFirstBySubscriber_SubscriberID(subscriberId)
            .orElseThrow(() -> new RuntimeException("No Patron found for Subscriber ID: " + subscriberId));
        return convertToPatronDTO(patron);
    }

    // Convert Patron entity to DTO
    private PatronDTO convertToPatronDTO(Patron patron) {
        PatronDTO patronDTO = new PatronDTO();
        
        patronDTO.setPatronID(patron.getPatronID());
        patronDTO.setFirstName(patron.getFirstName());
        patronDTO.setLastName(patron.getLastName());
        patronDTO.setRelation(patron.getRelation());
        patronDTO.setAddress1(patron.getAddress1());
        patronDTO.setAddress2(patron.getAddress2());
        patronDTO.setCity(patron.getCity());
        patronDTO.setState(patron.getState());
        patronDTO.setCountry(patron.getCountry());
        return patronDTO;
    }
}
