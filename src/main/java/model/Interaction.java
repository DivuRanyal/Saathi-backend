package model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Interactions")
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InteractionID")
    private Integer interactionID;

    @Column(name = "SubscriberID")
    private Integer subscriberID;

    @Column(name = "SaathiID")
    private Integer saathiID;

    @Column(name = "InteractionType", length = 255)
    private String interactionType;

    @Column(name = "Documents", length = 255)
    private String documents;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    @Column(name = "LastUpdatedDate")
    private LocalDateTime lastUpdatedDate;

    @Column(name = "CompletionStatus")
    private Integer completionStatus;

    @Column(name = "PackageServicesID")
    private Integer packageServicesID;

    @Column(name = "Subscriber", length = 45)
    private String subscriber;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "SubscriberAlaCarteServicesID", referencedColumnName = "SubscriberAlaCarteServicesID")
    private SubscriberAlaCarteServices subscriberAlaCarteServices;

    // Getters and setters

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

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public SubscriberAlaCarteServices getSubscriberAlaCarteServices() {
		return subscriberAlaCarteServices;
	}

	public void setSubscriberAlaCarteServices(SubscriberAlaCarteServices subscriberAlaCarteServices) {
		this.subscriberAlaCarteServices = subscriberAlaCarteServices;
	}

   
}