package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import model.SubscriptionPackage;
import model.AdminUser;
import model.PackageServices;
import model.dto.SubscriptionPackageDTO;
import model.dto.PackageServiceDTO;
import repository.SubscriptionPackageRepository;
import service.SubscriptionPackageService;
import repository.AdminUsersRepository;
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
            PackageServices packageServices = new PackageServices();
            packageServices.setSubscriptionPackage(savedPackage);  // Set the saved package
            packageServices.setServiceID(serviceDTO.getServiceID());
            packageServices.setFrequency(serviceDTO.getFrequency());
            packageServices.setFrequencyUnit(serviceDTO.getFrequencyUnit());
            packageServices.setPriceUSD(serviceDTO.getPriceUSD());
            packageServices.setPriceINR(serviceDTO.getPriceINR());
            packageServices.setStatus(serviceDTO.getStatus());
            packageServices.setCreatedDate(new Date());
            packageServices.setCreatedBy(createdByUser.get().getAdminUserID()); // Set createdBy user
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
            if (!updatedServiceIds.contains(existingService.getServiceID())) {
                packageServiceRepository.delete(existingService);
            }
        }

        // Add or update the new services from the DTO
        for (PackageServiceDTO serviceDTO : packageDTO.getPackageServices()) {
            PackageServices packageServices = existingServices.stream()
                    .filter(s -> s.getServiceID().equals(serviceDTO.getServiceID()))
                    .findFirst().orElse(new PackageServices()); // Find existing service or create a new one

            // Update fields of PackageService
            packageServices.setSubscriptionPackage(updatedPackage);
            packageServices.setServiceID(serviceDTO.getServiceID());
            packageServices.setFrequency(serviceDTO.getFrequency());
            packageServices.setFrequencyUnit(serviceDTO.getFrequencyUnit());
            packageServices.setPriceUSD(serviceDTO.getPriceUSD());
            packageServices.setPriceINR(serviceDTO.getPriceINR());
            packageServices.setStatus(serviceDTO.getStatus());
            packageServices.setLastUpdatedDate(new Date());
            packageServices.setUpdatedBy(updatedByUser.getAdminUserID());

            // Save or update each PackageService
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
            .map(this::convertTo_DTO)
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
     // Convert String to Date before setting in DTO
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            // Check if createdDate is not null before conversion
            if (subscriptionPackage.getCreatedDate() != null && !subscriptionPackage.getCreatedDate().isEmpty()) {
                Date createdDate = formatter.parse(subscriptionPackage.getCreatedDate());
                dto.setCreatedDate(createdDate);
            }

            // Check if lastUpdatedDate is not null before conversion
            if (subscriptionPackage.getLastUpdatedDate() != null && !subscriptionPackage.getLastUpdatedDate().isEmpty()) {
                Date lastUpdatedDate = formatter.parse(subscriptionPackage.getLastUpdatedDate());
                dto.setLastUpdatedDate(lastUpdatedDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();  // Handle the exception as per your application logic
            // You can log this error or handle it more gracefully if needed.
        }

        return dto;
    }
    
    private PackageServiceDTO convertTo_DTO(PackageServices packageServices) {
        PackageServiceDTO dto = new PackageServiceDTO();
        dto.setServiceID(packageServices.getServiceID());
        dto.setFrequency(packageServices.getFrequency());
        dto.setFrequencyUnit(packageServices.getFrequencyUnit());
        dto.setPriceUSD(packageServices.getPriceUSD());
        dto.setPriceINR(packageServices.getPriceINR());
        dto.setStatus(packageServices.getStatus());
        dto.setCreatedDate(packageServices.getCreatedDate());
        dto.setLastUpdatedDate(packageServices.getLastUpdatedDate());
        dto.setCreatedBy(packageServices.getCreatedBy());
        dto.setUpdatedBy(packageServices.getUpdatedBy());
        return dto;
    }
    private PackageServiceDTO convertServiceToDTO(PackageServices service) {
        PackageServiceDTO serviceDTO = new PackageServiceDTO();
        serviceDTO.setServiceID(service.getServiceID());
        serviceDTO.setFrequency(service.getFrequency());
        serviceDTO.setFrequencyUnit(service.getFrequencyUnit());
        serviceDTO.setPriceUSD(service.getPriceUSD());
        serviceDTO.setPriceINR(service.getPriceINR());
        serviceDTO.setStatus(service.getStatus());
        serviceDTO.setLastUpdatedDate(service.getLastUpdatedDate());
        serviceDTO.setCreatedBy(service.getCreatedBy());
        serviceDTO.setUpdatedBy(service.getUpdatedBy());
        return serviceDTO;
    }
}
