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
    }

    // Default constructor
    public ServiceReport() {
        super();
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
    }

    public String getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(String frequencyUnit) {
        this.frequencyUnit = frequencyUnit;
    }

    public int getCompletions() {
        return completions;
    }

    public void setCompletions(int completions) {
        this.completions = completions;
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
                ", isAlaCarte=" + isAlaCarte +
                '}';
    }
}
