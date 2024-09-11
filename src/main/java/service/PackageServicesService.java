package service;

import model.PackageServices;

import java.util.List;

public interface PackageServicesService {

    // Method to fetch all package services
    List<PackageServices> getAllPackageServices();

    // Method to fetch a specific package service by its ID
    PackageServices getPackageServicesById(Integer id);

    // Method to create a new PackageService
    PackageServices createPackageService(PackageServices packageServices);

    // Method to update an existing PackageService
    PackageServices updatePackageService(Integer id, PackageServices packageServices);

    // Method to delete a PackageService by its ID
    void deletePackageService(Integer id);
}
