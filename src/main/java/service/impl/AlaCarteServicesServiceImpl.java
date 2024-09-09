package service.impl;

import model.dto.AlaCarteServiceDTO;
import model.AdminUser;
import model.AlaCarteService;
import repository.AdminUsersRepository;
import repository.AlaCarteServiceRepository;
import service.AlaCarteServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlaCarteServicesServiceImpl implements AlaCarteServicesService {

    @Autowired
    private AlaCarteServiceRepository alaCarteServiceRepository;
    
    @Autowired
    private AdminUsersRepository adminUsersRepository;

    @Override
    public List<AlaCarteServiceDTO> getAllServices() {
        return alaCarteServiceRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AlaCarteServiceDTO getServiceById(Integer id) {
        Optional<AlaCarteService> service = alaCarteServiceRepository.findById(id);
        return service.map(this::convertToDTO).orElse(null);
    }

    @Override
    public AlaCarteServiceDTO createService(AlaCarteServiceDTO alaCarteServiceDTO) {
        AlaCarteService service = convertToEntity(alaCarteServiceDTO);
        AlaCarteService savedService = alaCarteServiceRepository.save(service);
        return convertToDTO(savedService);
    }

    @Override
    public AlaCarteServiceDTO updateService(Integer id, AlaCarteServiceDTO alaCarteServiceDTO) {
        Optional<AlaCarteService> serviceOpt = alaCarteServiceRepository.findById(id);
        if (serviceOpt.isPresent()) {
            AlaCarteService service = serviceOpt.get();
            service.setServiceName(alaCarteServiceDTO.getServiceName());
            service.setServiceDescription(alaCarteServiceDTO.getServiceDescription());
            service.setFrequency(alaCarteServiceDTO.getFrequency());
            service.setFrequencyUnit(alaCarteServiceDTO.getFrequencyUnit());
            service.setPriceINR(alaCarteServiceDTO.getPriceINR());
            service.setPriceUSD(alaCarteServiceDTO.getPriceUSD());
            service.setBusinessHoursStart(alaCarteServiceDTO.getBusinessHoursStart());
            service.setBusinessHoursEnd(alaCarteServiceDTO.getBusinessHoursEnd());
            service.setStatus(alaCarteServiceDTO.getStatus());
            service.setDurationInHours(alaCarteServiceDTO.getDurationInHours());
            AlaCarteService updatedService = alaCarteServiceRepository.save(service);
            return convertToDTO(updatedService);
        }
        return null;
    }

    @Override
    public void deleteService(Integer id) {
        alaCarteServiceRepository.deleteById(id);
    }

    // Helper method to convert entity to DTO
    private AlaCarteServiceDTO convertToDTO(AlaCarteService service) {
        AlaCarteServiceDTO dto = new AlaCarteServiceDTO();
        dto.setServiceID(service.getServiceID());
        dto.setServiceName(service.getServiceName());
        dto.setServiceDescription(service.getServiceDescription());
        dto.setFrequency(service.getFrequency());
        dto.setFrequencyUnit(service.getFrequencyUnit());
        dto.setPriceINR(service.getPriceINR());
        dto.setPriceUSD(service.getPriceUSD());
        dto.setBusinessHoursStart(service.getBusinessHoursStart());
        dto.setBusinessHoursEnd(service.getBusinessHoursEnd());
        dto.setStatus(service.getStatus());
        dto.setDurationInHours(service.getDurationInHours());
        // Check if createdBy is null before accessing getAdminUserID
        if (service.getCreatedBy() != null) {
            dto.setCreatedBy(service.getCreatedBy().getAdminUserID());
        } else {
            dto.setCreatedBy(null); // Handle null case appropriately
        }

        // Check if updatedBy is null before accessing getAdminUserID
        if (service.getUpdatedBy() != null) {
            dto.setUpdatedBy(service.getUpdatedBy().getAdminUserID());
        } else {
            dto.setUpdatedBy(null); // Handle null case appropriately
        }
        return dto;
    }

    // Helper method to convert DTO to entity
    private AlaCarteService convertToEntity(AlaCarteServiceDTO dto) {
        AlaCarteService service = new AlaCarteService();
        service.setServiceID(dto.getServiceID());
        service.setServiceName(dto.getServiceName());
        service.setServiceDescription(dto.getServiceDescription());
        service.setFrequency(dto.getFrequency());
        service.setFrequencyUnit(dto.getFrequencyUnit());
        service.setPriceINR(dto.getPriceINR());
        service.setPriceUSD(dto.getPriceUSD());
        service.setBusinessHoursStart(dto.getBusinessHoursStart());
        service.setBusinessHoursEnd(dto.getBusinessHoursEnd());
        service.setStatus(dto.getStatus());
        service.setDurationInHours(dto.getDurationInHours());
        // Fetch and set createdBy using the ID from the DTO
        if (dto.getCreatedBy() != null) {
            AdminUser createdBy = adminUsersRepository.findById(dto.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("Created by AdminUser not found"));
            service.setCreatedBy(createdBy);
        }

        // Fetch and set updatedBy using the ID from the DTO
        if (dto.getUpdatedBy() != null) {
            AdminUser updatedBy = adminUsersRepository.findById(dto.getUpdatedBy())
                .orElseThrow(() -> new RuntimeException("Updated by AdminUser not found"));
            service.setUpdatedBy(updatedBy);
        }
        return service;
    }
}
