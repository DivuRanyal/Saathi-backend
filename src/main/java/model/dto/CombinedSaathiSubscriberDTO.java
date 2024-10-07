package model.dto;

import java.util.Map;

public class CombinedSaathiSubscriberDTO {
    private Map<String, Long> saathiCounts;
    private SaathiServiceSummaryDTO saathiServiceSummary;
    private Map<String, Long> subscriberCounts;
    private int totalSubscriptions;
    // New fields for total pending and completed services
    private int totalPendingPackageServices;
    private int totalCompletedPackageServices;
    private int totalPendingAlaCarteServices;
    private int totalCompletedAlaCarteServices;
    private long totalSubscriberCounts;
    
    private int totalPackageServices; 
    private int totalAlaCarteServices; 

    // Getters and Setters for new fields
    public int getTotalPendingPackageServices() {
        return totalPendingPackageServices;
    }

    public void setTotalPendingPackageServices(int totalPendingPackageServices) {
        this.totalPendingPackageServices = totalPendingPackageServices;
    }

    public int getTotalCompletedPackageServices() {
        return totalCompletedPackageServices;
    }

    public void setTotalCompletedPackageServices(int totalCompletedPackageServices) {
        this.totalCompletedPackageServices = totalCompletedPackageServices;
    }

    public int getTotalPendingAlaCarteServices() {
        return totalPendingAlaCarteServices;
    }

    public void setTotalPendingAlaCarteServices(int totalPendingAlaCarteServices) {
        this.totalPendingAlaCarteServices = totalPendingAlaCarteServices;
    }

    public int getTotalCompletedAlaCarteServices() {
        return totalCompletedAlaCarteServices;
    }

    public void setTotalCompletedAlaCarteServices(int totalCompletedAlaCarteServices) {
        this.totalCompletedAlaCarteServices = totalCompletedAlaCarteServices;
    }

    // Existing Getters and Setters
    public Map<String, Long> getSaathiCounts() {
        return saathiCounts;
    }

    public void setSaathiCounts(Map<String, Long> saathiCounts) {
        this.saathiCounts = saathiCounts;
    }

    public SaathiServiceSummaryDTO getSaathiServiceSummary() {
        return saathiServiceSummary;
    }

    public void setSaathiServiceSummary(SaathiServiceSummaryDTO saathiServiceSummary) {
        this.saathiServiceSummary = saathiServiceSummary;
    }

    public Map<String, Long> getSubscriberCounts() {
        return subscriberCounts;
    }

    public void setSubscriberCounts(Map<String, Long> subscriberCounts) {
        this.subscriberCounts = subscriberCounts;
    }

	public int getTotalSubscriptions() {
		return totalSubscriptions;
	}

	public void setTotalSubscriptions(int totalSubscriptions) {
		this.totalSubscriptions = totalSubscriptions;
	}

	public long getTotalSubscriberCounts() {
		return totalSubscriberCounts;
	}

	public void setTotalSubscriberCounts(long totalSubscriberCounts) {
		this.totalSubscriberCounts = totalSubscriberCounts;
	}

	public int getTotalPackageServices() {
		return totalPackageServices;
	}

	public void setTotalPackageServices(int totalPackageServices) {
		this.totalPackageServices = totalPackageServices;
	}

	public int getTotalAlaCarteServices() {
		return totalAlaCarteServices;
	}

	public void setTotalAlaCarteServices(int totalAlaCarteServices) {
		this.totalAlaCarteServices = totalAlaCarteServices;
	}

	
	
}
