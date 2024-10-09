package model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table(name = "SubscriberAlaCarteServices")
public class SubscriberAlaCarteServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SubscriberAlaCarteServicesID")
    private int SubscriberAlaCarteServicesID;

    @Column(name = "ServiceID",nullable=false)
    private Integer serviceID;
    
    // Use LocalDate for date only
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "ServiceDate")
    private LocalDate serviceDate;

    // Use LocalTime for time only
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(name = "ServiceTime")
    private LocalTime serviceTime;

    @Column(name = "BillingStatus")
    private Integer billingStatus;

    @Column(name = "CreatedDate")
    private Date createdDate;

    @Column(name = "LastUpdatedDate")
    private Date lastUpdatedDate;

    @Column(name = "IsAccepted")
    private Boolean isAccepted;

    @Column(name = "IsPackageService")
    private Boolean isPackageService;

 // Correctly map the ManyToOne relationship
    @ManyToOne
    @JoinColumn(name = "SubscriberID")
    private Subscriber subscriber;  // Ensure this field name is 'subscriber', not 'subscriberID'


	public int getSubscriberAlaCarteServicesID() {
		return SubscriberAlaCarteServicesID;
	}

	public void setSubscriberAlaCarteServicesID(int subscriberAlaCarteServicesID) {
		SubscriberAlaCarteServicesID = subscriberAlaCarteServicesID;
	}

	public Integer getServiceID() {
		return serviceID;
	}

	public void setServiceID(Integer serviceID) {
		this.serviceID = serviceID;
	}

	public Integer getBillingStatus() {
		return billingStatus;
	}

	public void setBillingStatus(Integer billingStatus) {
		this.billingStatus = billingStatus;
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

	public Boolean getIsAccepted() {
		return isAccepted;
	}

	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	@PrePersist
    protected void onCreate() {
        createdDate = new Date();  // Set the current timestamp for createdDate
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedDate = new Date();  // Set the current timestamp for lastUpdatedDate
    }
    
    @ManyToOne
    @JoinColumn(name = "ServiceID", referencedColumnName = "ServiceID", insertable = false, updatable = false)
    private AlaCarteService service;
    // Getters and Setters


	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public AlaCarteService getService() {
		return service;
	}

	public void setService(AlaCarteService service) {
		this.service = service;
	}

	public Boolean getIsPackageService() {
		return isPackageService;
	}

	public void setIsPackageService(Boolean isPackageService) {
		this.isPackageService = isPackageService;
	}

	public LocalDate getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(LocalDate serviceDate) {
		this.serviceDate = serviceDate;
	}

	public LocalTime getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(LocalTime serviceTime) {
		this.serviceTime = serviceTime;
	}
    
	
}
