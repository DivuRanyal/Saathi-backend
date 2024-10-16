package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AggregatedServiceReport {

    private String frequencyUnit;
    private Integer packageServiceID;
    private Integer pending;
    private Integer completions;
   
    private String serviceName;
    private Integer frequency;
    private List<Interaction> interactions;
    private boolean alaCarte;
    private List<PreferredDateTime> preferredDatesTimes;
    private LocalDateTime completionDate;
    private String packageName;
    private String completionStatus;
    private Integer serviceID;
    private Integer subscriberAlaCarteServicesID;

    // Constructors, getters, and setters

    public AggregatedServiceReport() {}

    public AggregatedServiceReport(String frequencyUnit, Integer packageServiceID, Integer pending, Integer completions,  String serviceName, Integer frequency, List<Interaction> interactions, boolean alaCarte, List<PreferredDateTime> preferredDatesTimes, LocalDateTime completionDate, String packageName, String completionStatus, Integer serviceID, Integer subscriberAlaCarteServicesID) {
        this.frequencyUnit = frequencyUnit;
        this.packageServiceID = packageServiceID;
        this.pending = pending;
        this.completions = completions;
       
        this.serviceName = serviceName;
        this.frequency = frequency;
        this.interactions = interactions;
        this.alaCarte = alaCarte;
        this.preferredDatesTimes = preferredDatesTimes;
        this.completionDate = completionDate;
        this.packageName = packageName;
        this.completionStatus = completionStatus;
        this.serviceID = serviceID;
        this.subscriberAlaCarteServicesID = subscriberAlaCarteServicesID;
    }

	public String getFrequencyUnit() {
		return frequencyUnit;
	}

	public void setFrequencyUnit(String frequencyUnit) {
		this.frequencyUnit = frequencyUnit;
	}

	public Integer getPackageServiceID() {
		return packageServiceID;
	}

	public void setPackageServiceID(Integer packageServiceID) {
		this.packageServiceID = packageServiceID;
	}

	public Integer getPending() {
		return pending;
	}

	public void setPending(Integer pending) {
		this.pending = pending;
	}

	public Integer getCompletions() {
		return completions;
	}

	public void setCompletions(Integer completions) {
		this.completions = completions;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public List<Interaction> getInteractions() {
		return interactions;
	}

	public void setInteractions(List<Interaction> interactions) {
		this.interactions = interactions;
	}

	public boolean isAlaCarte() {
		return alaCarte;
	}

	public void setAlaCarte(boolean alaCarte) {
		this.alaCarte = alaCarte;
	}

	public List<PreferredDateTime> getPreferredDatesTimes() {
		return preferredDatesTimes;
	}

	public void setPreferredDatesTimes(List<PreferredDateTime> preferredDatesTimes) {
		this.preferredDatesTimes = preferredDatesTimes;
	}

	public String getCompletionDate() {
        if (completionDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return completionDate.format(formatter);  // Format to "yyyy-MM-dd HH:mm:ss"
        }
        return null;
    }

	public void setCompletionDate(String completionDateString) {
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	        // Convert the String to LocalDateTime
	        this.completionDate = LocalDateTime.parse(completionDateString, formatter);
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getCompletionStatus() {
		return completionStatus;
	}

	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}

	public Integer getServiceID() {
		return serviceID;
	}

	public void setServiceID(Integer serviceID) {
		this.serviceID = serviceID;
	}

	public Integer getSubscriberAlaCarteServicesID() {
		return subscriberAlaCarteServicesID;
	}

	public void setSubscriberAlaCarteServicesID(Integer subscriberAlaCarteServicesID) {
		this.subscriberAlaCarteServicesID = subscriberAlaCarteServicesID;
	}

	@Override
	public String toString() {
	    return "AggregatedServiceReport{" +
	            "packageServiceID=" + packageServiceID +
	            ", serviceID=" + serviceID +
	            ", serviceName='" + serviceName + '\'' +
	            ", packageName='" + packageName + '\'' +
	            ", frequencyUnit='" + frequencyUnit + '\'' +
	            ", completions=" + completions +
	            ", pending=" + pending +
	           
	            ", alaCarte=" + alaCarte +
	            ", preferredDatesTimes=" + preferredDatesTimes +
	            ", completionDate=" + completionDate +
	            ", completionStatus='" + completionStatus + '\'' +
	            ", subscriberAlaCarteServicesID=" + subscriberAlaCarteServicesID +
	            '}';
	}

    
    // Getters and Setters
    // ...
}
