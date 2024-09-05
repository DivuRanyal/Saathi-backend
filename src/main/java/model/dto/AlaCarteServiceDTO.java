package model.dto;

import java.math.BigDecimal;
import java.sql.Time;

public class AlaCarteServiceDTO {

    private Integer serviceID;
    private String serviceName;
    private String serviceDescription;
    private Integer frequencyInHours;
    private BigDecimal price;
    private Time businessHoursStart;
    private Time businessHoursEnd;
    private Integer status;
    private Integer createdBy;
    private Integer updatedBy;

    // Constructors
    public AlaCarteServiceDTO() {}

    // Getters and Setters
    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public Integer getFrequencyInHours() {
        return frequencyInHours;
    }

    public void setFrequencyInHours(Integer frequencyInHours) {
        this.frequencyInHours = frequencyInHours;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Time getBusinessHoursStart() {
        return businessHoursStart;
    }

    public void setBusinessHoursStart(Time businessHoursStart) {
        this.businessHoursStart = businessHoursStart;
    }

    public Time getBusinessHoursEnd() {
        return businessHoursEnd;
    }

    public void setBusinessHoursEnd(Time businessHoursEnd) {
        this.businessHoursEnd = businessHoursEnd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        if (createdBy != null) {
            this.createdBy = createdBy;
        } else {
            this.createdBy = -1;  // or handle null case based on your business needs
        }
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        if (updatedBy != null) {
            this.updatedBy = updatedBy;
        } else {
            this.updatedBy = -1;  // Handle null case
        }
    }
}
