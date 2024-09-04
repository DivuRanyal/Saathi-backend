package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import model.AlaCarteService;
import repository.AlaCarteServiceRepository;
import service.AlaCarteServicesService;

@Service
public class ServicesServiceImpl implements AlaCarteServicesService {

    private final AlaCarteServiceRepository alaCarteServiceRepository;

    @Autowired
    public ServicesServiceImpl(AlaCarteServiceRepository alaCarteServiceRepository) {
        this.alaCarteServiceRepository = alaCarteServiceRepository;
    }

    @Override
    public AlaCarteService createService(AlaCarteService service) {
        return alaCarteServiceRepository.save(service);
    }

    @Override
    public AlaCarteService updateService(AlaCarteService service) {
        return alaCarteServiceRepository.save(service);
    }

    @Override
    public void deleteService(Integer serviceId) {
        alaCarteServiceRepository.deleteById(serviceId);
    }

    @Override
    public Optional<AlaCarteService> getServiceById(Integer serviceId) {
        return alaCarteServiceRepository.findById(serviceId);
    }

    @Override
    public List<AlaCarteService> getAllServices() {
        return alaCarteServiceRepository.findAll();
    }
}
