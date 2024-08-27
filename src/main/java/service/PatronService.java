package service;

import model.Patron;

import java.util.List;

public interface PatronService {
    Patron savePatron(Patron patron);
    Patron updatePatron(Patron patron);
    void deletePatron(int patronId);
    Patron getPatronById(int patronId);
    List<Patron> getAllPatrons();
}
