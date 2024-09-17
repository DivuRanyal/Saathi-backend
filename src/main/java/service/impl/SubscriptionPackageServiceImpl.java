package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import model.SubscriptionPackage;
import model.AlaCarteService;
import model.PackageServices;
import model.dto.SubscriptionPackageDTO;
import model.dto.PackageServiceDTO;
import repository.SubscriptionPackageRepository;
import repository.AlaCarteServiceRepository;
import repository.PackageServiceRepository;
import service.SubscriptionPackageService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class SubscriptionPackageServiceImpl implements SubscriptionPackageService {

	@Autowired
	private EntityManager entityManager;
    @Autowired
    private SubscriptionPackageRepository subscriptionPackageRepository;

    @Autowired
    private PackageServiceRepository packageServicesRepository;

    @Autowired
    private AlaCarteServiceRepository alaCarteServiceRepository;

    @Override
    public SubscriptionPackageDTO createSubscriptionPackage(SubscriptionPackageDTO subscriptionPackageDTO) {
        try {
            // Create a new SubscriptionPackage object
            SubscriptionPackage subscriptionPackage = new SubscriptionPackage();

            // Set values only if they are provided
            if (subscriptionPackageDTO.getPackageName() != null) {
                subscriptionPackage.setPackageName(subscriptionPackageDTO.getPackageName());
            }
            if (subscriptionPackageDTO.getPackageDescription() != null) {
                subscriptionPackage.setPackageDescription(subscriptionPackageDTO.getPackageDescription());
            }
            if (subscriptionPackageDTO.getPriceUSD() != null) {
                subscriptionPackage.setPriceUSD(subscriptionPackageDTO.getPriceUSD());
            }
            if (subscriptionPackageDTO.getPriceINR() != null) {
                subscriptionPackage.setPriceINR(subscriptionPackageDTO.getPriceINR());
            }
            if (subscriptionPackageDTO.getStatus() != null) {
                subscriptionPackage.setStatus(subscriptionPackageDTO.getStatus());
            }
            if (subscriptionPackageDTO.getCreatedBy() != null) {
                subscriptionPackage.setCreatedBy(subscriptionPackageDTO.getCreatedBy());
            }
            if (subscriptionPackageDTO.getPackageDiscount() != null) {
                subscriptionPackage.setPackageDiscount(subscriptionPackageDTO.getPackageDiscount());
            }
            
            // Save the new subscription package
            subscriptionPackage = subscriptionPackageRepository.save(subscriptionPackage);

            // Save associated Package Services if present
            if (subscriptionPackageDTO.getPackageServices() != null && !subscriptionPackageDTO.getPackageServices().isEmpty()) {
                savePackageServices(subscriptionPackage, subscriptionPackageDTO.getPackageServices(), subscriptionPackageDTO.getCreatedBy());
            }

            return mapToDTO(subscriptionPackage);

        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("PackageName_UNIQUE")) {
                throw new RuntimeException("Package with this name already exists.");
            } else {
                throw new RuntimeException("Data integrity violation occurred.");
            }
        }
    }
    @Override
    public SubscriptionPackageDTO updateSubscriptionPackage(Integer packageID, SubscriptionPackageDTO subscriptionPackageDTO) {
        // Find the existing subscription package by ID
        SubscriptionPackage subscriptionPackage = subscriptionPackageRepository.findById(packageID)
                .orElseThrow(() -> new RuntimeException("SubscriptionPackage not found with ID: " + packageID));

        // Update values only if they are provided
        if (subscriptionPackageDTO.getPackageName() != null) {
            subscriptionPackage.setPackageName(subscriptionPackageDTO.getPackageName());
        }
        if (subscriptionPackageDTO.getPackageDescription() != null) {
            subscriptionPackage.setPackageDescription(subscriptionPackageDTO.getPackageDescription());
        }
        if (subscriptionPackageDTO.getPriceUSD() != null) {
            subscriptionPackage.setPriceUSD(subscriptionPackageDTO.getPriceUSD());
        }
        if (subscriptionPackageDTO.getPriceINR() != null) {
            subscriptionPackage.setPriceINR(subscriptionPackageDTO.getPriceINR());
        }
        if (subscriptionPackageDTO.getStatus() != null) {
            subscriptionPackage.setStatus(subscriptionPackageDTO.getStatus());
        }

        // Set the updatedBy field and lastUpdatedDate
        if (subscriptionPackageDTO.getUpdatedBy() != null) {
            subscriptionPackage.setUpdatedBy(subscriptionPackageDTO.getUpdatedBy());
        }

        if (subscriptionPackageDTO.getPackageDiscount() != null) {
            subscriptionPackage.setPackageDiscount(subscriptionPackageDTO.getPackageDiscount());
        }
        
        // Handle PackageServices update
        if (subscriptionPackageDTO.getPackageServices() != null && !subscriptionPackageDTO.getPackageServices().isEmpty()) {
            updatePackageServices(subscriptionPackage, subscriptionPackageDTO.getPackageServices(), subscriptionPackageDTO.getUpdatedBy());
        }

        // Save the updated subscription package
        subscriptionPackage = subscriptionPackageRepository.save(subscriptionPackage);

        return mapToDTO(subscriptionPackage);
    }

    private void updatePackageServices(SubscriptionPackage subscriptionPackage, List<PackageServiceDTO> packageServicesDTOList, Integer updatedBy) {
        // Fetch existing services from the database
        List<PackageServices> existingServices = packageServicesRepository.findBySubscriptionPackage(subscriptionPackage);

        // Manually create a map of incoming services using serviceID as the key
        Map<Integer, PackageServiceDTO> incomingServicesMap = new HashMap<>();
        for (PackageServiceDTO dto : packageServicesDTOList) {
            incomingServicesMap.put(dto.getServiceID(), dto);
        }

        // Handle deletion of services that no longer exist in the incoming DTO
        for (PackageServices existingService : existingServices) {
            Integer existingServiceId = existingService.getService().getServiceID();
            if (!incomingServicesMap.containsKey(existingServiceId)) {
                // If the existing service is not in the incoming list, delete it
                System.out.println("Deleting service with ID: " + existingServiceId);
                packageServicesRepository.deleteById(existingServiceId);
               
                // Flush the persistence context to avoid ObjectDeletedException
                packageServicesRepository.flush();
            } else {
                // Update the existing service with new values from DTO
                PackageServiceDTO serviceDTO = incomingServicesMap.get(existingServiceId);
                System.out.println("Updating service with ID: " + existingServiceId);
                
                if (serviceDTO.getFrequency() != null) {
                    existingService.setFrequency(serviceDTO.getFrequency());
                }
                if (serviceDTO.getStatus() != null) {
                    existingService.setStatus(serviceDTO.getStatus());
                }
                existingService.setUpdatedBy(updatedBy);

                // Save the updated service
                packageServicesRepository.save(existingService);

                // Remove the updated service from the map to track which new services need to be added
                incomingServicesMap.remove(existingServiceId);
            }
        }

        // Add new services that are in the DTO but not in the existing services
        for (PackageServiceDTO newServiceDTO : incomingServicesMap.values()) {
            System.out.println("Adding new service with ID: " + newServiceDTO.getServiceID());
            
            PackageServices newService = new PackageServices();
            newService.setSubscriptionPackage(subscriptionPackage);

            AlaCarteService alaCarteService = alaCarteServiceRepository.findById(newServiceDTO.getServiceID())
                    .orElseThrow(() -> new RuntimeException("AlaCarteService not found with ID: " + newServiceDTO.getServiceID()));
            newService.setService(alaCarteService);
            newService.setFrequency(newServiceDTO.getFrequency());
            newService.setStatus(newServiceDTO.getStatus());
            newService.setCreatedBy(updatedBy);

            packageServicesRepository.save(newService);  // Save the new service
        }
    }


    private void savePackageServices(SubscriptionPackage subscriptionPackage, List<PackageServiceDTO> packageServicesDTOList, Integer createdBy) {
        for (PackageServiceDTO packageServicesDTO : packageServicesDTOList) {
            // Check if the service is already added to the package
            if (subscriptionPackage.getPackageServices() != null) {
                boolean serviceExists = subscriptionPackage.getPackageServices()
                    .stream()
                    .anyMatch(existingService -> existingService.getService().getServiceID().equals(packageServicesDTO.getServiceID()));

                if (serviceExists) {
                    throw new RuntimeException("Service with ID " + packageServicesDTO.getServiceID() + " is already added to the package.");
                }
            }

            // Proceed to add the service if it does not exist
            PackageServices packageService = new PackageServices();
            packageService.setSubscriptionPackage(subscriptionPackage);

            // Retrieve the AlaCarteService by its ID and set the service object
            if (packageServicesDTO.getServiceID() != null) {
                AlaCarteService alaCarteService = alaCarteServiceRepository.findById(packageServicesDTO.getServiceID())
                        .orElseThrow(() -> new RuntimeException("AlaCarteService not found with ID: " + packageServicesDTO.getServiceID()));
                packageService.setService(alaCarteService);
            }

            if (packageServicesDTO.getFrequency() != null) {
                packageService.setFrequency(packageServicesDTO.getFrequency());
            }

            if (packageServicesDTO.getStatus() != null) {
                packageService.setStatus(packageServicesDTO.getStatus());
            }

            packageService.setCreatedBy(createdBy);

            subscriptionPackage.getPackageServices().add(packageService); // Add the service to the package
            packageServicesRepository.save(packageService); // Save the service to the database
        }
    }
    @Override
    public SubscriptionPackageDTO getSubscriptionPackageById(Integer packageID) {
        SubscriptionPackage subscriptionPackage = subscriptionPackageRepository.findById(packageID).orElse(null);
        if (subscriptionPackage != null) {
            return mapToDTO(subscriptionPackage);
        }
        return null;
    }

    @Override
    public List<SubscriptionPackageDTO> getAllSubscriptionPackages() {
        List<SubscriptionPackage> packages = subscriptionPackageRepository.findAll();
        return packages.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<SubscriptionPackageDTO> getActiveSubscriptionPackages() {
        List<SubscriptionPackage> activePackages = subscriptionPackageRepository.findByStatus(1);  // Assuming '1' is for active packages
        return activePackages.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Method to map SubscriptionPackage to SubscriptionPackageDTO
    private SubscriptionPackageDTO mapToDTO(SubscriptionPackage subscriptionPackage) {
        SubscriptionPackageDTO dto = new SubscriptionPackageDTO();
        dto.setPackageID(subscriptionPackage.getPackageID());
        dto.setPackageName(subscriptionPackage.getPackageName());
        dto.setPackageDescription(subscriptionPackage.getPackageDescription());
        dto.setPriceUSD(subscriptionPackage.getPriceUSD());
        dto.setPriceINR(subscriptionPackage.getPriceINR());
        dto.setStatus(subscriptionPackage.getStatus());
        dto.setCreatedBy(subscriptionPackage.getCreatedBy());
        dto.setUpdatedBy(subscriptionPackage.getUpdatedBy());
        dto.setPackageDiscount(subscriptionPackage.getPackageDiscount());
        if (subscriptionPackage.getPackageServices() != null) {
            List<PackageServiceDTO> packageServicesDTOList = subscriptionPackage.getPackageServices()
                    .stream()
                    .map((PackageServices service) -> {
                        PackageServiceDTO serviceDTO = new PackageServiceDTO();
                        serviceDTO.setPackageServiceID(service.getPackageServicesID());
                        if (service.getService() != null) {
                            serviceDTO.setServiceID(service.getService().getServiceID());
                            serviceDTO.setServiceName(service.getService().getServiceName());
                        }
                        serviceDTO.setFrequency(service.getFrequency());
                        serviceDTO.setFrequencyUnit(service.getFrequencyUnit());
                        serviceDTO.setStatus(service.getStatus());
                        serviceDTO.setPackageName(subscriptionPackage.getPackageName());
                        serviceDTO.setPackageID(subscriptionPackage.getPackageID());
                        serviceDTO.setCreatedBy(service.getCreatedBy());
                        serviceDTO.setUpdatedBy(service.getUpdatedBy());
                        return serviceDTO;
                    })
                    .collect(Collectors.toList());

            dto.setPackageServices(packageServicesDTOList);
        } else {
            dto.setPackageServices(Collections.emptyList());
        }

        return dto;
    }

    @Transactional
    @Override
    public List<PackageServiceDTO> getPackageServicesByPackageId(Integer packageId) {
        SubscriptionPackage subscriptionPackage = subscriptionPackageRepository.findById(packageId).orElse(null);
        if (subscriptionPackage == null) {
            return Collections.emptyList();
        }

        return subscriptionPackage.getPackageServices()
                .stream()
                .map(service -> {
                    PackageServiceDTO serviceDTO = new PackageServiceDTO();
                    serviceDTO.setPackageServiceID(service.getPackageServicesID());
                    if (service.getService() != null) {
                        serviceDTO.setServiceID(service.getService().getServiceID());
                        serviceDTO.setServiceName(service.getService().getServiceName());
                    }
                    serviceDTO.setFrequency(service.getFrequency());
                    serviceDTO.setFrequencyUnit(service.getFrequencyUnit());
                    serviceDTO.setStatus(service.getStatus());
                    serviceDTO.setPackageName(subscriptionPackage.getPackageName());
                    serviceDTO.setPackageID(subscriptionPackage.getPackageID());
                    serviceDTO.setCreatedBy(service.getCreatedBy());
                   serviceDTO.setUpdatedBy(service.getUpdatedBy());
                    return serviceDTO;
                })
                .collect(Collectors.toList());
    }
	@Override
	public void deleteSubscriptionPackage(Integer packageID) {
		// TODO Auto-generated method stub
		
	}
	
}
