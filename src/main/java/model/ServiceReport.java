package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

 // Constructor
    public ServiceReport(int serviceID, String serviceName, String packageName, int frequency, 
                         String frequencyUnit, int completions, String completionStatus, LocalDateTime completionDate, 
                         boolean isAlaCarte, int packageServiceID) {
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
    }


    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    // Getters and setters
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

    // Return only the date part for completionDate
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

	@Override
    public String toString() {
        return "ServiceReport{" +
                "serviceID=" + serviceID +
                ", serviceName='" + serviceName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", frequency=" + frequency +
                ", frequencyUnit='" + frequencyUnit + '\'' +
                ", completions=" + completions +
                ", completionStatus='" + completionStatus + '\'' +
                ", completionDate=" + getCompletionDate() +
                ", isAlaCarte=" + isAlaCarte +
                '}';
    }
}
