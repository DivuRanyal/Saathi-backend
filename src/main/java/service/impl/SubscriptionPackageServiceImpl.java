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

    
}
