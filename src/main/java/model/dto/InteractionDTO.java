package model.dto;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InteractionDTO {

    private Integer interactionID;
    private Integer subscriberID;
    private Integer saathiID;
    private String interactionType;
    private String documents;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date lastUpdatedDate;
    private Integer completionStatus;
    private Integer packageServicesID;
    private String description;
    private Integer packageID;
    // Add this new field
    private Integer subscriberAlaCarteServicesID;

    // Getters and Setters for all fields including subscriberAlaCarteServicesID

 // Date format to be used for date fields
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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

    public String getCreatedDate() {
        return createdDate != null ? DATE_FORMAT.format(createdDate) : null;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public String getLastUpdatedDate() {
    	return lastUpdatedDate != null ? DATE_FORMAT.format(lastUpdatedDate) : null;
       
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
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

	public Integer getPackageID() {
		return packageID;
	}

	public void setPackageID(Integer packageID) {
		this.packageID = packageID;
	}
    
    
}
