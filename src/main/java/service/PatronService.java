package service;

import model.Patron;
import model.dto.SubscriberDTO;

import java.util.List;

public interface PatronService {
    Patron savePatron(Patron patron);
    Patron updatePatron(Patron patron);
    Patron updatePatron(int patronId, Patron patron);
    void deletePatron(int patronId);
    Patron getPatronById(int patronId);
    List<Patron> getAllPatrons();
}
