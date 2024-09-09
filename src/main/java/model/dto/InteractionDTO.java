package model.dto;

import java.time.LocalDateTime;

public class InteractionDTO {

    private Integer interactionID;
    private Integer subscriberID;
    private Integer saathiID;
    private String interactionType;
    private String documents;
    private LocalDateTime createdDate;
    private LocalDateTime lastUpdatedDate;
    private Integer completionStatus;
    private Integer packageServicesID;
    private String description;
    
    // Add this new field
    private Integer subscriberAlaCarteServicesID;

    // Getters and Setters for all fields including subscriberAlaCarteServicesID

    public Integer getInteractionID() {
        return interactionID;
    }

    public void setInteractionID(Integer interactionID) {
        this.interactionID = interactionID;
    }

    public Integer getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(Integer subscriberID) {
        this.subscriberID = subscriberID;
    }

    public Integer getSaathiID() {
        return saathiID;
    }

    public void setSaathiID(Integer saathiID) {
        this.saathiID = saathiID;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    
    public Integer getCompletionStatus() {
		return completionStatus;
	}

	public void setCompletionStatus(Integer completionStatus) {
		this.completionStatus = completionStatus;
	}

	public Integer getPackageServicesID() {
        return packageServicesID;
    }

    public void setPackageServicesID(Integer packageServicesID) {
        this.packageServicesID = packageServicesID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSubscriberAlaCarteServicesID() {
        return subscriberAlaCarteServicesID;
    }

    public void setSubscriberAlaCarteServicesID(Integer subscriberAlaCarteServicesID) {
        this.subscriberAlaCarteServicesID = subscriberAlaCarteServicesID;
    }
}
