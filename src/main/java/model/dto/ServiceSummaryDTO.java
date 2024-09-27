package model.dto;

import java.util.List;

//DTO to hold the overall service summary
public class ServiceSummaryDTO {
private int totalServices;
private int totalPending;
private int totalCompleted;
private List<ServiceCountDTO> serviceBreakdown;
private int totalSubscribers; // New field to store total subscribers count
private List<PackageDetailDTO> packageDetails; // New field to store package details

public ServiceSummaryDTO(int totalServices, int totalPending, int totalCompleted, List<ServiceCountDTO> serviceBreakdown) {
   this.totalServices = totalServices;
   this.totalPending = totalPending;
   this.totalCompleted = totalCompleted;
   this.serviceBreakdown = serviceBreakdown;
}

public ServiceSummaryDTO(int totalServices, int totalPending, int totalCompleted, List<ServiceCountDTO> serviceBreakdown, int totalSubscribers, List<PackageDetailDTO> packageDetails) {
    this.totalServices = totalServices;
    this.totalPending = totalPending;
    this.totalCompleted = totalCompleted;
    this.serviceBreakdown = serviceBreakdown;
    this.totalSubscribers = totalSubscribers;
    this.packageDetails = packageDetails;
}
// Getters and setters
public int getTotalServices() { return totalServices; }
public int getTotalPending() { return totalPending; }
public int getTotalCompleted() { return totalCompleted; }
public List<ServiceCountDTO> getServiceBreakdown() { return serviceBreakdown; }

public int getTotalSubscribers() {
	return totalSubscribers;
}

public void setTotalSubscribers(int totalSubscribers) {
	this.totalSubscribers = totalSubscribers;
}

public List<PackageDetailDTO> getPackageDetails() {
	return packageDetails;
}

public void setPackageDetails(List<PackageDetailDTO> packageDetails) {
	this.packageDetails = packageDetails;
}


}
