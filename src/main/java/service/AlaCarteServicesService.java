package service;

import java.util.List;
import java.util.Optional;

import model.AlaCarteService;

public interface AlaCarteServicesService {

    AlaCarteService createService(AlaCarteService service);

    AlaCarteService updateService(AlaCarteService service);

    void deleteService(Integer serviceId);

    Optional<AlaCarteService> getServiceById(Integer serviceId);

    List<AlaCarteService> getAllServices();

}
