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
    public List<Patron> getAllPatrons() {
        return patronRepository.findAll();
    }

    @Override
    public Patron getPatronById(int patronId) {
        Optional<Patron> patron = patronRepository.findById(patronId);
        return patron.orElse(null);
    }

    @Override
    public Patron savePatron(Patron patron) {
        return patronRepository.save(patron);
    }

    @Override
    public Patron updatePatron(int patronId, Patron patronDetails) {
        return patronRepository.findById(patronId).map(patron -> {
            patron.setFirstName(patronDetails.getFirstName());
            patron.setLastName(patronDetails.getLastName());
            patron.setEmail(patronDetails.getEmail());
            patron.setContactNo(patronDetails.getContactNo());
            patron.setCountryCode(patronDetails.getCountryCode());
            patron.setDob(patronDetails.getDob());
            patron.setSubscriber(patronDetails.getSubscriber());
            patron.setAddress1(patronDetails.getAddress1());
            patron.setAddress2(patronDetails.getAddress2());
            patron.setCity(patronDetails.getCity());
            patron.setState(patronDetails.getState());
            patron.setCountry(patronDetails.getCountry());
            patron.setRelation(patronDetails.getRelation());
           
            return patronRepository.save(patron);
        }).orElse(null);
    }

    @Override
    public void deletePatron(int patronId) {
        if (patronRepository.existsById(patronId)) {
            patronRepository.deleteById(patronId);
        }
    }

	@Override
	public Patron updatePatron(Patron patron) {
		// TODO Auto-generated method stub
		return null;
	}
}
