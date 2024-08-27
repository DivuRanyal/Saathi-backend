package service.impl;

import model.Patron;
import repository.PatronRepository;
import service.PatronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatronServiceImpl implements PatronService {

    @Autowired
    private PatronRepository patronRepository;

    @Override
    public Patron savePatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @Override
    public Patron updatePatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @Override
    public void deletePatron(int patronId) {
        patronRepository.deleteById(patronId);
    }

    @Override
    public Patron getPatronById(int patronId) {
        Optional<Patron> patron = patronRepository.findById(patronId);
        return patron.orElse(null);  // Return the Patron if found, else return null
    }

    @Override
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }
    
    
}
