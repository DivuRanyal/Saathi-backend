package model;

import javax.persistence.*;

import service.SubscriberService;

import java.util.Date;
/*
@Entity
@Table(name = "Interactions")
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InteractionID")
    private Integer interactionID;

    @ManyToOne
    @JoinColumn(name = "SubscriberID", referencedColumnName = "SubscriberID")
    private Subscriber subscriber;

    @ManyToOne
    @JoinColumn(name = "OverrideSaathiID", referencedColumnName = "UserID")
    private AdminUser overrideSaathi;

    @Column(name = "InteractionType")
    private String interactionType;

    @Column(name = "Documents")
    private String documents;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedDate")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LastUpdatedDate")
    private Date lastUpdatedDate;

    @Column(name = "CompletionStatus")
    private Integer completionStatus;

    @ManyToOne
    @JoinColumn(name = "PackageServicesID", referencedColumnName = "PackageServicesID")
    private PackageService packageService;

    @ManyToOne
    @JoinColumn(name = "SubscriberAlaCarteServicesID", referencedColumnName = "SubscriberAlaCarteServicesID")
    private SubscriberService subscriberService;

    // Default constructor
    public Interaction() {}

    // Constructor with all fields
    public Interaction(Subscriber subscriber, AdminUser overrideSaathi, String interactionType, String documents, Date createdDate, Date lastUpdatedDate, Integer completionStatus, PackageService packageService, SubscriberService subscriberService) {
        this.subscriber = subscriber;
        this.overrideSaathi = overrideSaathi;
        this.interactionType = interactionType;
        this.documents = documents;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.completionStatus = completionStatus;
        this.packageService = packageService;
        this.subscriberService = subscriberService;
    }

    // Getters and Setters
    public Integer getInteractionID() {
        return interactionID;
    }

    public void setInteractionID(Integer interactionID) {
        this.interactionID = interactionID;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public AdminUser getOverrideSaathi() {
        return overrideSaathi;
    }

    public void setOverrideSaathi(AdminUser overrideSaathi) {
        this.overrideSaathi = overrideSaathi;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
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

    public PackageService getPackageService() {
        return packageService;
    }

    public void setPackageService(PackageService packageService) {
        this.packageService = packageService;
    }

	public SubscriberService getSubscriberService() {
		return subscriberService;
	}

	public void setSubscriberService(SubscriberService subscriberService) {
		this.subscriberService = subscriberService;
	}

  
}
*/
