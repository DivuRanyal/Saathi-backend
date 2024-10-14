package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ServiceReport implements Serializable {
    private int serviceID;
    private String serviceName;
    private String packageName;
    private Integer packageServiceID;
    private int frequency;
    private String frequencyUnit;
    private int completions;
    private String completionStatus;
    private boolean isAlaCarte;
    private String color;
    private Integer subscriberAlaCarteServicesID;
    private Integer frequencyInstance;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completionDate;

    // Field to store multiple requestedDate and requestedTime values for each frequency
    private List<PreferredDateTime> preferredDateTimes;

    private int frequencyCount;
    private int pending;
    private Integer serviceRating;
    // Constructor
    public ServiceReport(int serviceID, String serviceName, String packageName, int frequency, 
                         String frequencyUnit, int completions, String completionStatus, LocalDateTime completionDate, 
                         boolean isAlaCarte, Integer packageServiceID, List<PreferredDateTime> preferredDateTimes, Integer subscriberAlaCarteServicesID,
                         Integer frequencyInstance,Integer serviceRating) {
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
        this.preferredDateTimes = preferredDateTimes != null ? preferredDateTimes : new ArrayList<>(); // Initialize if null
        this.frequencyCount = frequency;
        this.pending = calculatePending(); // Initialize pending count
        this.subscriberAlaCarteServicesID = subscriberAlaCarteServicesID;
        this.frequencyInstance=frequencyInstance;
        this.serviceRating=serviceRating;
    }

    // Default constructor
    public ServiceReport() {
        this.preferredDateTimes = new ArrayList<>();
    }

    // Method to calculate pending services as frequencyCount - completions
    private int calculatePending() {
        return pending - completions;
    }

    public void addPreferredDateTime(PreferredDateTime preferredDateTime) {
        if (preferredDateTimes == null) {
        	preferredDateTimes = new ArrayList<>();
        }
        preferredDateTimes.add(preferredDateTime);
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
  //      this.frequencyCount = frequency;
 //       this.pending = calculatePending(); // Recalculate pending
    }

    public String getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(String frequencyUnit) {
        this.frequencyUnit = frequencyUnit;
 //       this.frequencyCount = frequency;
 //       this.pending = calculatePending(); // Recalculate pending
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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return completionDate.format(formatter);  // Format to "yyyy-MM-dd HH:mm:ss"
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

    public Integer getPackageServiceID() {
        return packageServiceID;
    }

    public void setPackageServiceID(Integer packageServiceID) {
        this.packageServiceID = packageServiceID;
    }

    public List<PreferredDateTime> getPreferredDateTimes() {
        return preferredDateTimes;
    }

    public void setPreferredDateTimes(List<PreferredDateTime> preferredDateTimes) {
        this.preferredDateTimes = preferredDateTimes;
    }

    public int getFrequencyCount() {
        return frequencyCount;
    }

    public void setFrequencyCount(int frequencyCount) {
        this.frequencyCount = frequencyCount;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public Integer getSubscriberAlaCarteServicesID() {
        return subscriberAlaCarteServicesID;
    }

    public void setSubscriberAlaCarteServicesID(Integer subscriberAlaCarteServicesID) {
        this.subscriberAlaCarteServicesID = subscriberAlaCarteServicesID;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    
    public Integer getFrequencyInstance() {
		return frequencyInstance;
	}

	public void setFrequencyInstance(Integer frequencyInstance) {
		this.frequencyInstance = frequencyInstance;
	}

	public Integer getServiceRating() {
		return serviceRating;
	}

	public void setServiceRating(Integer serviceRating) {
		this.serviceRating = serviceRating;
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
                ", preferredDateTimes=" + preferredDateTimes +
                ", frequencyCount=" + frequency +
                ", pending=" + pending +
                ", isAlaCarte=" + isAlaCarte +
                ", subscriberAlaCarteServicesID=" + subscriberAlaCarteServicesID +
                ", frequencyInstance=" + frequencyInstance +
                ", serviceRating=" + serviceRating +
                '}';
    }
    
    
}
