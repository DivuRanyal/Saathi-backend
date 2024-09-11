package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import model.SubscriptionPackage;
import model.AdminUser;
import model.AlaCarteService;
import model.PackageServices;
import model.dto.SubscriptionPackageDTO;
import model.dto.PackageServiceDTO;
import repository.SubscriptionPackageRepository;
import service.SubscriptionPackageService;
import repository.AdminUsersRepository;
import repository.AlaCarteServiceRepository;
import repository.PackageServiceRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionPackageServiceImpl implements SubscriptionPackageService {

    @Autowired
    private SubscriptionPackageRepository subscriptionPackageRepository;

    @Autowired
    private PackageServiceRepository packageServiceRepository;

    @Autowired
    private AdminUsersRepository adminUserRepository; // To fetch AdminUser by ID

    @Autowired
    private AlaCarteServiceRepository alaCarteServiceRepository;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    @Transactional
    public SubscriptionPackage saveSubscriptionPackageWithServices(SubscriptionPackageDTO packageDTO) {
        // Fetch AdminUser for createdBy
        Optional<AdminUser> createdByUser = adminUserRepository.findById(packageDTO.getCreatedBy());
        if (!createdByUser.isPresent()) {
            throw new RuntimeException("Admin User not found for ID: " + packageDTO.getCreatedBy());
        }

        // Convert DTO to SubscriptionPackage entity
        SubscriptionPackage subscriptionPackage = new SubscriptionPackage();
        subscriptionPackage.setPackageName(packageDTO.getPackageName());
        subscriptionPackage.setPackageDescription(packageDTO.getPackageDescription());
        subscriptionPackage.setPriceUSD(packageDTO.getPriceUSD());
        subscriptionPackage.setPriceINR(packageDTO.getPriceINR());
        subscriptionPackage.setStatus(packageDTO.getStatus());
        subscriptionPackage.setCreatedDate(new Date());
        subscriptionPackage.setCreatedBy(createdByUser.get().getAdminUserID()); // Set createdBy user

        // Save the Subscription Package first
        SubscriptionPackage savedPackage = subscriptionPackageRepository.save(subscriptionPackage);

     // Now save the associated PackageServices
        for (PackageServiceDTO serviceDTO : packageDTO.getPackageServices()) {
            // Fetch the AlaCarteService entity by the serviceID from the DTO
            AlaCarteService alaCarteService = alaCarteServiceRepository.findById(serviceDTO.getServiceID())
                    .orElseThrow(() -> new RuntimeException("Service not found for ID: " + serviceDTO.getServiceID()));

            // Create a new PackageServices entity
            PackageServices packageServices = new PackageServices();
            packageServices.setSubscriptionPackage(savedPackage);  // Set the saved SubscriptionPackage entity
            packageServices.setService(alaCarteService);  // Set the fetched AlaCarteService entity
            packageServices.setFrequency(serviceDTO.getFrequency());  // Set the frequency from DTO
            packageServices.setFrequencyUnit(serviceDTO.getFrequencyUnit());  // Set the frequency unit from DTO
            packageServices.setPriceUSD(serviceDTO.getPriceUSD());  // Set the USD price from DTO
            packageServices.setPriceINR(serviceDTO.getPriceINR());  // Set the INR price from DTO
            packageServices.setStatus(serviceDTO.getStatus());  // Set the status from DTO
            packageServices.setCreatedDate(new Date());  // Set the current date for createdDate
            packageServices.setCreatedBy(createdByUser.get().getAdminUserID());  // Set the createdBy user ID

            // Save each PackageService
            packageServiceRepository.save(packageServices);
        }

        return savedPackage;
    }

    @Override
    @Transactional
    public SubscriptionPackage updateSubscriptionPackageWithServices(Integer packageId, SubscriptionPackageDTO packageDTO) {
        // Fetch AdminUser for updatedBy
        Optional<AdminUser> updatedByUserOptional = adminUserRepository.findById(packageDTO.getUpdatedBy());
        if (!updatedByUserOptional.isPresent()) {
            throw new RuntimeException("Admin User not found for ID: " + packageDTO.getUpdatedBy());
        }

        AdminUser updatedByUser = updatedByUserOptional.get();

        // Find the existing subscription package by ID
        Optional<SubscriptionPackage> existingPackageOptional = subscriptionPackageRepository.findById(packageId);
        if (!existingPackageOptional.isPresent()) {
            throw new RuntimeException("Subscription Package not found for ID: " + packageId);
        }

        SubscriptionPackage existingPackage = existingPackageOptional.get();

        // Update fields of the SubscriptionPackage
        existingPackage.setPackageName(packageDTO.getPackageName());
        existingPackage.setPackageDescription(packageDTO.getPackageDescription());
        existingPackage.setPriceUSD(packageDTO.getPriceUSD());
        existingPackage.setPriceINR(packageDTO.getPriceINR());
        existingPackage.setStatus(packageDTO.getStatus());
        existingPackage.setLastUpdatedDate(new Date());
        existingPackage.setUpdatedBy(updatedByUser.getAdminUserID()); // Set updatedBy user

        // Save the updated Subscription Package
        SubscriptionPackage updatedPackage = subscriptionPackageRepository.save(existingPackage);

        // Retrieve existing PackageServices associated with this package
        List<PackageServices> existingServices = packageServiceRepository.findBySubscriptionPackage(updatedPackage);

        // Convert updated services from DTO to a list of service IDs
        List<Integer> updatedServiceIds = packageDTO.getPackageServices().stream()
                .map(PackageServiceDTO::getServiceID).collect(Collectors.toList());

        // Remove services that are no longer present in the updated DTO
        for (PackageServices existingService : existingServices) {
            if (!updatedServiceIds.contains(existingService.getService().getServiceID())) {
                packageServiceRepository.delete(existingService);
            }
        }

        // Add or update the new services from the DTO
        for (PackageServiceDTO serviceDTO : packageDTO.getPackageServices()) {
            // Fetch the AlaCarteService entity by the serviceID from the DTO
            AlaCarteService alaCarteService = alaCarteServiceRepository.findById(serviceDTO.getServiceID())
                    .orElseThrow(() -> new RuntimeException("Service not found for ID: " + serviceDTO.getServiceID()));

            // Check if the service already exists in the package
            PackageServices packageServices = existingServices.stream()
                    .filter(s -> s.getService().getServiceID().equals(serviceDTO.getServiceID()))
                    .findFirst()
                    .orElse(new PackageServices()); // Create new if not found

            // Set or update the fields for PackageServices
            packageServices.setSubscriptionPackage(updatedPackage);  // Use updatedPackage here
            packageServices.setService(alaCarteService);  // Set the fetched AlaCarteService entity
            packageServices.setFrequency(serviceDTO.getFrequency());  // Set the frequency from DTO
            packageServices.setFrequencyUnit(serviceDTO.getFrequencyUnit());  // Set the frequency unit from DTO
            packageServices.setPriceUSD(serviceDTO.getPriceUSD());  // Set the USD price from DTO
            packageServices.setPriceINR(serviceDTO.getPriceINR());  // Set the INR price from DTO
            packageServices.setStatus(serviceDTO.getStatus());  // Set the status from DTO
            packageServices.setLastUpdatedDate(new Date());  // Set the current date for lastUpdatedDate
            packageServices.setUpdatedBy(updatedByUser.getAdminUserID());  // Set the updatedBy user ID

            // Save or update the PackageServices
            packageServiceRepository.save(packageServices);
        }

        return updatedPackage;
    }


    // Method to get the SubscriptionPackage and its services
    @Override
    public SubscriptionPackageDTO getSubscriptionPackageWithServices(Integer packageId) {
        Optional<SubscriptionPackage> subscriptionPackageOptional = subscriptionPackageRepository.findById(packageId);

        if (!subscriptionPackageOptional.isPresent()) {
            return null;
        }

        SubscriptionPackage subscriptionPackage = subscriptionPackageOptional.get();

        // Convert the SubscriptionPackage to DTO
        SubscriptionPackageDTO packageDTO = convertToDTO(subscriptionPackage);

        // Fetch associated PackageServices
        List<PackageServices> packageServicesList = packageServiceRepository.findBySubscriptionPackage(subscriptionPackage);

        // Convert PackageServices to DTO
        List<PackageServiceDTO> serviceDTOs = packageServicesList.stream()
                .map(this::convertServiceToDTO)
                .collect(Collectors.toList());

        // Add services to the SubscriptionPackageDTO
        packageDTO.setPackageServices(serviceDTOs);

        return packageDTO;
    }

    @Override
    public List<SubscriptionPackageDTO> getAllSubscriptionPackagesWithServices() {
        List<SubscriptionPackage> packages = subscriptionPackageRepository.findAll();  // Fetch all packages

        // Convert each SubscriptionPackage entity to DTO with associated services
        return packages.stream()
                .map(pkg -> {
                    // Fetch associated services for each package
                    SubscriptionPackageDTO packageDTO = convertToDTO(pkg);

                    List<PackageServices> packageServicesList = packageServiceRepository.findBySubscriptionPackage(pkg);
                    
                    List<PackageServiceDTO> serviceDTOs = packageServicesList.stream()
                            .map(this::convertServiceToDTO)
                            .collect(Collectors.toList());
                    
                    packageDTO.setPackageServices(serviceDTOs);  // Add services to DTO
                    
                    return packageDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PackageServiceDTO> getPackageServicesByPackageId(Integer packageId) {
        // Find the subscription package by ID
        Optional<SubscriptionPackage> subscriptionPackageOptional = subscriptionPackageRepository.findById(packageId);
        if (!subscriptionPackageOptional.isPresent()) {
            throw new RuntimeException("Subscription Package not found for ID: " + packageId);
        }

        SubscriptionPackage subscriptionPackage = subscriptionPackageOptional.get();

        // Get package services associated with the package
        List<PackageServices> packageServicesList = packageServiceRepository.findBySubscriptionPackage(subscriptionPackage);

        // Convert to DTO
        List<PackageServiceDTO> serviceDTOs = packageServicesList.stream()
            .map(this::convertServiceToDTO)
            .collect(Collectors.toList());

        return serviceDTOs;
    }

    private SubscriptionPackageDTO convertToDTO(SubscriptionPackage subscriptionPackage) {
        SubscriptionPackageDTO dto = new SubscriptionPackageDTO();
        dto.setPackageID(subscriptionPackage.getPackageID());
        dto.setPackageName(subscriptionPackage.getPackageName());
        dto.setPackageDescription(subscriptionPackage.getPackageDescription());
        dto.setPriceUSD(subscriptionPackage.getPriceUSD());
        dto.setPriceINR(subscriptionPackage.getPriceINR());
        dto.setStatus(subscriptionPackage.getStatus());
        dto.setCreatedBy(subscriptionPackage.getCreatedBy());
        dto.setUpdatedBy(subscriptionPackage.getUpdatedBy());
     
        return dto;
    }

    private PackageServiceDTO convertServiceToDTO(PackageServices service) {
        PackageServiceDTO serviceDTO = new PackageServiceDTO();
        serviceDTO.setServiceID(service.getService().getServiceID());
        serviceDTO.setFrequency(service.getFrequency());
        serviceDTO.setFrequencyUnit(service.getFrequencyUnit());
        serviceDTO.setPriceUSD(service.getPriceUSD());
        serviceDTO.setPriceINR(service.getPriceINR());
        serviceDTO.setStatus(service.getStatus());
        serviceDTO.setLastUpdatedDate(service.getLastUpdatedDate());
        serviceDTO.setCreatedBy(service.getCreatedBy());
        serviceDTO.setUpdatedBy(service.getUpdatedBy());
        serviceDTO.setPackageServiceID(service.getPackageServicesID());
        
        return serviceDTO;
    }
}
