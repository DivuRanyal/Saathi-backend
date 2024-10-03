package model.dto;

import java.util.List;

public class SaathiServiceSummaryDTO {
    private int totalServices;
    private int totalSubscribers;
    private List<SaathiServiceCountDTO> saathiServiceCounts;
    private List<PackageDetailDTO> packageDetails;

    // Constructor
    public SaathiServiceSummaryDTO(int totalServices, int totalSubscribers, List<SaathiServiceCountDTO> saathiServiceCounts, List<PackageDetailDTO> packageDetails) {
        this.totalServices = totalServices;
        this.totalSubscribers = totalSubscribers;
        this.saathiServiceCounts = saathiServiceCounts;
        this.packageDetails = packageDetails;
    }

    // Getters and Setters
    public int getTotalServices() {
        return totalServices;
    }

    public void setTotalServices(int totalServices) {
        this.totalServices = totalServices;
    }

    public int getTotalSubscribers() {
        return totalSubscribers;
    }

    public void setTotalSubscribers(int totalSubscribers) {
        this.totalSubscribers = totalSubscribers;
    }

    public List<SaathiServiceCountDTO> getSaathiServiceCounts() {
        return saathiServiceCounts;
    }

    public void setSaathiServiceCounts(List<SaathiServiceCountDTO> saathiServiceCounts) {
        this.saathiServiceCounts = saathiServiceCounts;
    }

    public List<PackageDetailDTO> getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(List<PackageDetailDTO> packageDetails) {
        this.packageDetails = packageDetails;
    }
}
