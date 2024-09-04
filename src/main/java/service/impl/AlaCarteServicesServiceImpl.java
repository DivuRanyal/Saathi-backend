package service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.AlaCarteService;
import repository.AlaCarteServiceRepository;
import service.AlaCarteServicesService;

@Service
public class AlaCarteServicesServiceImpl implements AlaCarteServicesService {

    @Autowired
    private AlaCarteServiceRepository alaCarteServiceRepository;

    @Override
    public AlaCarteService createService(AlaCarteService service) {
        return alaCarteServiceRepository.save(service);
    }

    @Override
    public AlaCarteService updateService(AlaCarteService service) {
        Optional<AlaCarteService> existingService = alaCarteServiceRepository.findById(service.getServiceID());
        if (existingService.isPresent()) {
            return alaCarteServiceRepository.save(service);
        } else {
            throw new IllegalArgumentException("Service not found with id: " + service.getServiceID());
        }
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
