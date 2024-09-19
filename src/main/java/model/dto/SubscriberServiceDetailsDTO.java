package model.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class SubscriberServiceDetailsDTO {

    private Integer subscriberID;
    private int serviceID;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date serviceDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private Date serviceTime;
    
    private Integer billingStatus;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdatedDate;
    
    private Boolean isAccepted;
    private Boolean isPackageService;
    private Integer subscriberAlaCarteServicesID;
    private Integer completionStatus;
    private String serviceName;
    
    // Constructor with all the necessary parameters
    public SubscriberServiceDetailsDTO(Integer subscriberID, int serviceID, Date serviceDate, Date serviceTime,
                                       Integer billingStatus, Date createdDate, Date lastUpdatedDate,
                                       Boolean isAccepted, Integer subscriberAlaCarteServicesID, Integer completionStatus,
                                       String serviceName, Boolean isPackageService) {
        this.subscriberID = subscriberID;
        this.serviceID = serviceID;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.billingStatus = billingStatus;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.isAccepted = isAccepted;
        this.subscriberAlaCarteServicesID = subscriberAlaCarteServicesID;
        this.completionStatus = completionStatus;
        this.serviceName = serviceName;
        this.isPackageService=isPackageService;
       
    }

    // Getters and Setters
    public Integer getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(Integer subscriberID) {
        this.subscriberID = subscriberID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Date getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Date serviceTime) {
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

    public Integer getSubscriberAlaCarteServicesID() {
        return subscriberAlaCarteServicesID;
    }

    public void setSubscriberAlaCarteServicesID(Integer subscriberAlaCarteServicesID) {
        this.subscriberAlaCarteServicesID = subscriberAlaCarteServicesID;
    }

    public Integer getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(Integer completionStatus) {
        this.completionStatus = completionStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
	public Boolean getIsPackageService() {
		return isPackageService;
	}
	
	public void setIsPackageService(Boolean isPackageService) {
		this.isPackageService = isPackageService;
	}
}

