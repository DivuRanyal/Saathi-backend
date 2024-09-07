package model;

import javax.persistence.*;
import java.util.Date;

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
    @JoinColumn(name = "OverrideSaathiID", referencedColumnName = "AdminUserID")
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
    private PackageServices packageService;

    @ManyToOne
    @JoinColumn(name = "SubscriberAlaCarteServicesID", referencedColumnName = "SubscriberAlaCarteServicesID")
    private SubscriberAlaCarteServices subscriberAlaCarteServices;

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

    public PackageServices getPackageService() {
        return packageService;
    }

    public void setPackageService(PackageServices packageService) {
        this.packageService = packageService;
    }

    public SubscriberAlaCarteServices getSubscriberAlaCarteServices() {
        return subscriberAlaCarteServices;
    }

    public void setSubscriberAlaCarteServices(SubscriberAlaCarteServices subscriberAlaCarteServices) {
        this.subscriberAlaCarteServices = subscriberAlaCarteServices;
    }
}
