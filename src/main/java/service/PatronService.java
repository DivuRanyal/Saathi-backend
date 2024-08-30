package service;

import model.Patron;
import model.dto.PatronDTO;
import model.dto.SubscriberDTO;

import java.util.List;
import java.util.Optional;

public interface PatronService {

    List<PatronDTO> getAllPatrons();

    PatronDTO getPatronById(int patronId);

    PatronDTO savePatron(PatronDTO patronDTO);

    PatronDTO updatePatron(int patronId, PatronDTO patronDTO);

    void deletePatron(int patronId);
    
    List<PatronDTO> getPatronsBySubscriberId(int subscriberID);
}