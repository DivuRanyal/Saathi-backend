package model.dto;

import java.util.Map;

public class CombinedSaathiSubscriberDTO {
    private Map<String, Long> saathiCounts;
    private SaathiServiceSummaryDTO saathiServiceSummary;
    private Map<String, Long> subscriberCounts;

    // Fields for pending and completed services
    private int totalPendingPackageServices;
    private int totalCompletedPackageServices;
    private int totalPendingAlaCarteServices;
    private int totalCompletedAlaCarteServices;

    // New fields for total services
    private int totalPackageServices;   // Sum of totalPendingPackageServices + totalCompletedPackageServices
    private int totalAlaCarteServices;  // Sum of totalPendingAlaCarteServices + totalCompletedAlaCarteServices
    private int totalSubscriptionCount;

    // Method to calculate total services
    public void calculateTotalServices() {
        this.totalPackageServices = this.totalPendingPackageServices + this.totalCompletedPackageServices;
        this.totalAlaCarteServices = this.totalPendingAlaCarteServices + this.totalCompletedAlaCarteServices;
    }

    // Getters and Setters for all fields
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

    public int getTotalSubscriptionCount() {
        return totalSubscriptionCount;
    }

    public void setTotalSubscriptionCount(int totalSubscriptionCount) {
        this.totalSubscriptionCount = totalSubscriptionCount;
    }

   
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
}
