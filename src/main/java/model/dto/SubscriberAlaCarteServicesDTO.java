package model.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class SubscriberAlaCarteServicesDTO {

    private int subscriberAlaCarteServicesID;
    private Integer serviceID;
    private LocalDate serviceDate;
    private LocalTime serviceTime;
    private Integer billingStatus;
    private Date createdDate;
    private Date lastUpdatedDate;
    private Boolean isAccepted;
    private Boolean isPackageService;
    private Integer subscriberID; // To hold subscriber ID from the Subscriber entity
    private String serviceName;   // To hold the service name from the AlaCarteService entity

    // Constructor
    public SubscriberAlaCarteServicesDTO(int subscriberAlaCarteServicesID, Integer serviceID, LocalDate serviceDate, LocalTime serviceTime,
                                         Integer billingStatus, Date createdDate, Date lastUpdatedDate, Boolean isAccepted,
                                         Boolean isPackageService, Integer subscriberID, String serviceName) {
        this.subscriberAlaCarteServicesID = subscriberAlaCarteServicesID;
        this.serviceID = serviceID;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.billingStatus = billingStatus;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.isAccepted = isAccepted;
        this.isPackageService = isPackageService;
        this.subscriberID = subscriberID;
        this.serviceName = serviceName;
    }

    // Getters and Setters
    public int getSubscriberAlaCarteServicesID() {
        return subscriberAlaCarteServicesID;
    }

    public void setSubscriberAlaCarteServicesID(int subscriberAlaCarteServicesID) {
        this.subscriberAlaCarteServicesID = subscriberAlaCarteServicesID;
    }

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
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

    public Boolean getIsPackageService() {
        return isPackageService;
    }

    public void setIsPackageService(Boolean isPackageService) {
        this.isPackageService = isPackageService;
    }

    public Integer getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(Integer subscriberID) {
        this.subscriberID = subscriberID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    // Default constructor
    public SubscriberAlaCarteServicesDTO() {
    }
}
