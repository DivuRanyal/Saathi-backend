package service;

import model.SubscriptionPackage;
import model.dto.SubscriptionPackageDTO;

public interface SubscriptionPackageService {

	SubscriptionPackage saveSubscriptionPackageWithServices(SubscriptionPackageDTO packageDTO);
	SubscriptionPackage updateSubscriptionPackageWithServices(Integer packageId, SubscriptionPackageDTO packageDTO);
	SubscriptionPackageDTO getSubscriptionPackageWithServices(Integer packageId);
}
