package model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Interactions")
public class Interaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InteractionID")
    private Integer interactionID;

    @Column(name = "SubscriberID")
    private Integer subscriberID;

    @Column(name = "PackageID")
    private Integer packageID;

    @Column(name = "SaathiID")
    private Integer saathiID;

    @Column(name = "InteractionType", length = 255)
    private String interactionType;

    @Column(name = "Documents", length = 255)
    private String documents;

    @Column(name = "CreatedDate")
    private Date createdDate;

    @Column(name = "LastUpdatedDate")
    private Date lastUpdatedDate;

    @Column(name = "CompletionStatus")
    private Integer completionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PackageServicesID", referencedColumnName = "PackageServicesID", nullable = true)
    private PackageServices packageServices;


    @Column(name = "Subscriber", length = 45)
    private String subscriber;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "ServiceRating")
    private Integer serviceRating;

    @Column(name = "FrequencyInstance")
    private Integer frequencyInstance;

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
	
    public PackageServices getPackageServices() {
		return packageServices;
	}

	public void setPackageServices(PackageServices packageServices) {
		this.packageServices = packageServices;
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

	public Integer getPackageID() {
		return packageID;
	}

	public void setPackageID(Integer packageID) {
		this.packageID = packageID;
	}

	
	 public Integer getServiceRating() {
		return serviceRating;
	}

	public void setServiceRating(Integer serviceRating) {
		this.serviceRating = serviceRating;
	}

	public Integer getFrequencyInstance() {
		return frequencyInstance;
	}

	public void setFrequencyInstance(Integer frequencyInstance) {
		this.frequencyInstance = frequencyInstance;
	}

	@PrePersist
	    protected void onCreate() {
	        createdDate = new Date();  // Set the current timestamp for createdDate
	        
	    }

	 @PreUpdate
	    protected void onUpdate() {
	        lastUpdatedDate = new Date();  // Set the current timestamp for lastUpdatedDate
	    }
}
