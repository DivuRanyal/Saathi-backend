package service;

import java.util.List;

import model.dto.PackageServiceDTO;
import model.dto.SubscriptionPackageDTO;

public interface SubscriptionPackageService {

	 SubscriptionPackageDTO createSubscriptionPackage(SubscriptionPackageDTO subscriptionPackageDTO);
	    SubscriptionPackageDTO updateSubscriptionPackage(Integer packageID, SubscriptionPackageDTO subscriptionPackageDTO);
	    SubscriptionPackageDTO getSubscriptionPackageById(Integer packageID);
	    List<SubscriptionPackageDTO> getAllSubscriptionPackages();
	    List<SubscriptionPackageDTO> getActiveSubscriptionPackages();  // Method to get only active packages
	    List<PackageServiceDTO> getPackageServicesByPackageId(Integer packageId);
	    void deleteSubscriptionPackage(Integer packageID);
	
}
