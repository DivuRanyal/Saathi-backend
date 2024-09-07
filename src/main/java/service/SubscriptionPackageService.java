package service;

import java.util.List;

import model.SubscriptionPackage;
import model.dto.PackageServiceDTO;
import model.dto.SubscriptionPackageDTO;

public interface SubscriptionPackageService {

	SubscriptionPackage saveSubscriptionPackageWithServices(SubscriptionPackageDTO packageDTO);
	SubscriptionPackage updateSubscriptionPackageWithServices(Integer packageId, SubscriptionPackageDTO packageDTO);
	SubscriptionPackageDTO getSubscriptionPackageWithServices(Integer packageId);
	List<SubscriptionPackageDTO> getAllSubscriptionPackagesWithServices() ;
	List<PackageServiceDTO> getPackageServicesByPackageId(Integer packageId);
}
