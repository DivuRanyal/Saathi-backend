package model.dto;

import java.util.List;

//DTO to hold the overall service summary
public class ServiceSummaryDTO {
    private int totalServices;
    private int totalPending;
    private int totalCompleted;
    private List<ServiceCountDTO> serviceBreakdown;
    private int totalSubscribers; // Field to store total subscribers count
    private List<PackageDetailDTO> packageDetails; // Field to store package details
    private int inActiveSubscribers; // New field to store the count of subscribers with billingStatus = 0

    // Constructor without totalSubscribers and packageDetails
    public ServiceSummaryDTO(int totalServices, int totalPending, int totalCompleted, List<ServiceCountDTO> serviceBreakdown) {
        this.totalServices = totalServices;
        this.totalPending = totalPending;
        this.totalCompleted = totalCompleted;
        this.serviceBreakdown = serviceBreakdown;
    }

    // Constructor with totalSubscribers, packageDetails, and billingStatusZeroCount
    public ServiceSummaryDTO(int totalServices, int totalPending, int totalCompleted, List<ServiceCountDTO> serviceBreakdown, int totalSubscribers, List<PackageDetailDTO> packageDetails, int inActiveSubscribers) {
        this.totalServices = totalServices;
        this.totalPending = totalPending;
        this.totalCompleted = totalCompleted;
        this.serviceBreakdown = serviceBreakdown;
        this.totalSubscribers = totalSubscribers;
        this.packageDetails = packageDetails;
        this.inActiveSubscribers = inActiveSubscribers;
    }

    // Getters and setters
    public int getTotalServices() { 
        return totalServices; 
    }

    public void setTotalServices(int totalServices) {
        this.totalServices = totalServices;
    }

    public int getTotalPending() {
        return totalPending;
    }

    public void setTotalPending(int totalPending) {
        this.totalPending = totalPending;
    }

    public int getTotalCompleted() {
        return totalCompleted;
    }

    public void setTotalCompleted(int totalCompleted) {
        this.totalCompleted = totalCompleted;
    }

    public List<ServiceCountDTO> getServiceBreakdown() {
        return serviceBreakdown;
    }

    public void setServiceBreakdown(List<ServiceCountDTO> serviceBreakdown) {
        this.serviceBreakdown = serviceBreakdown;
    }

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

	public int getInActiveSubscribers() {
		return inActiveSubscribers;
	}

	public void setInActiveSubscribers(int inActiveSubscribers) {
		this.inActiveSubscribers = inActiveSubscribers;
	}

   
}
