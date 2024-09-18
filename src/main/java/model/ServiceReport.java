package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ServiceReport implements Serializable {
    private int serviceID;
    private String serviceName;
    private String packageName;
    private int packageServiceID;
    private int frequency; // How many times the service should be completed
    private String frequencyUnit; // e.g., "monthly", "weekly", etc.
    private int completions; // How many times the service has been completed
    private String completionStatus; // Could be "In Progress", "Completed", etc.
    private boolean isAlaCarte;

    // Define the JSON format to return only the date part of the LocalDateTime
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime completionDate;

    private LocalDate requestedDate; // Date requested by the subscriber
    private LocalTime requestedTime; // Time requested by the subscriber

    private int frequencyCount; // Calculated based on frequency and frequencyUnit
    private int pending; // Calculated as frequencyCount - completions

    // Constructor
    public ServiceReport(int serviceID, String serviceName, String packageName, int frequency, 
                         String frequencyUnit, int completions, String completionStatus, LocalDateTime completionDate, 
                         boolean isAlaCarte, int packageServiceID, LocalDate requestedDate, LocalTime requestedTime) {
        this.serviceID = serviceID;
        this.serviceName = serviceName;
        this.packageName = packageName;
        this.frequency = frequency;
        this.frequencyUnit = frequencyUnit;
        this.completions = completions;
        this.completionStatus = completionStatus;
        this.completionDate = completionDate;
        this.isAlaCarte = isAlaCarte;
        this.packageServiceID = packageServiceID;
        this.requestedDate = requestedDate;
        this.requestedTime = requestedTime;
        this.frequencyCount = calculateFrequencyCount(); // Initialize frequencyCount
        this.pending = calculatePending(); // Initialize pending count
    }

    // Default constructor
    public ServiceReport() {
        super();
    }

    // Method to calculate frequencyCount based on frequency and frequencyUnit
    private int calculateFrequencyCount() {
        switch (frequencyUnit.toLowerCase()) {
            case "weekly":
                return frequency * 4; // Assuming 4 weeks in a month
            case "monthly":
                return frequency * 1; // Monthly means once per month
            case "daily":
                return frequency * 30; // Assuming 30 days in a month
            default:
                return frequency; // Default to raw frequency
        }
    }

    // Method to calculate pending services as frequencyCount - completions
    private int calculatePending() {
        return frequencyCount - completions;
    }

    // Getters and setters
    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
        this.frequencyCount = calculateFrequencyCount(); // Recalculate frequencyCount
        this.pending = calculatePending(); // Recalculate pending
    }

    public String getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(String frequencyUnit) {
        this.frequencyUnit = frequencyUnit;
        this.frequencyCount = calculateFrequencyCount(); // Recalculate frequencyCount
        this.pending = calculatePending(); // Recalculate pending
    }

    public int getCompletions() {
        return completions;
    }

    public void setCompletions(int completions) {
        this.completions = completions;
        this.pending = calculatePending(); // Recalculate pending
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public String getCompletionDate() {
        if (completionDate != null) {
            return completionDate.toLocalDate().toString();  // Convert to date format
        }
        return null;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public boolean isAlaCarte() {
        return isAlaCarte;
    }

    public void setAlaCarte(boolean isAlaCarte) {
        this.isAlaCarte = isAlaCarte;
    }

    public int getPackageServiceID() {
        return packageServiceID;
    }

    public void setPackageServiceID(int packageServiceID) {
        this.packageServiceID = packageServiceID;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public LocalTime getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(LocalTime requestedTime) {
        this.requestedTime = requestedTime;
    }

    public int getFrequencyCount() {
        return frequencyCount;
    }

    public int getPending() {
        return pending;
    }

    // Override toString method for printing the details of ServiceReport
    @Override
    public String toString() {
        return "ServiceReport{" +
                "serviceID=" + serviceID +
                ", serviceName='" + serviceName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", packageServiceID=" + packageServiceID +
                ", frequency=" + frequency +
                ", frequencyUnit='" + frequencyUnit + '\'' +
                ", completions=" + completions +
                ", completionStatus='" + completionStatus + '\'' +
                ", completionDate=" + getCompletionDate() +
                ", requestedDate=" + requestedDate +
                ", requestedTime=" + requestedTime +
                ", frequencyCount=" + frequencyCount +
                ", pending=" + pending +
                ", isAlaCarte=" + isAlaCarte +
                '}';
    }

	public void setFrequencyCount(int frequencyCount) {
		this.frequencyCount = frequencyCount;
	}

	public void setPending(int pending) {
		this.pending = pending;
	}
    
    
}
