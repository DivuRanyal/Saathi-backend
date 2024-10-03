package model.dto;

public class SaathiServiceCountDTO {
    private String saathiName;
    private int pendingPackageServices;
    private int completedPackageServices;
    private int pendingAlaCarteServices;
    private int completedAlaCarteServices;

    // Constructor
    public SaathiServiceCountDTO(String saathiName, int pendingPackageServices, int completedPackageServices, int pendingAlaCarteServices, int completedAlaCarteServices) {
        this.saathiName = saathiName;
        this.pendingPackageServices = pendingPackageServices;
        this.completedPackageServices = completedPackageServices;
        this.pendingAlaCarteServices = pendingAlaCarteServices;
        this.completedAlaCarteServices = completedAlaCarteServices;
    }

    // Getters and Setters
    public String getSaathiName() {
        return saathiName;
    }

    public void setSaathiName(String saathiName) {
        this.saathiName = saathiName;
    }

    public int getPendingPackageServices() {
        return pendingPackageServices;
    }

    public void setPendingPackageServices(int pendingPackageServices) {
        this.pendingPackageServices = pendingPackageServices;
    }

    public int getCompletedPackageServices() {
        return completedPackageServices;
    }

    public void setCompletedPackageServices(int completedPackageServices) {
        this.completedPackageServices = completedPackageServices;
    }

    public int getPendingAlaCarteServices() {
        return pendingAlaCarteServices;
    }

    public void setPendingAlaCarteServices(int pendingAlaCarteServices) {
        this.pendingAlaCarteServices = pendingAlaCarteServices;
    }

    public int getCompletedAlaCarteServices() {
        return completedAlaCarteServices;
    }

    public void setCompletedAlaCarteServices(int completedAlaCarteServices) {
        this.completedAlaCarteServices = completedAlaCarteServices;
    }
}
