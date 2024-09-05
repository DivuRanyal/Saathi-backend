package service;

import model.dto.AlaCarteServiceDTO;
import java.util.List;

public interface AlaCarteServicesService {

    List<AlaCarteServiceDTO> getAllServices();
    AlaCarteServiceDTO getServiceById(Integer id);
    AlaCarteServiceDTO createService(AlaCarteServiceDTO alaCarteServiceDTO);
    AlaCarteServiceDTO updateService(Integer id, AlaCarteServiceDTO alaCarteServiceDTO);
    void deleteService(Integer id);
}
