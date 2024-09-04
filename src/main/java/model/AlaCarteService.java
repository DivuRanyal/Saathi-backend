package model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "Services")
public class AlaCarteService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ServiceID")
    private Integer serviceID;

    @Column(name = "ServiceName")
    private String serviceName;

    @Column(name = "ServiceDesciption", columnDefinition = "TEXT")
    private String serviceDescription;

    @Column(name = "FrequencyInHours")
    private Integer frequencyInHours;

    @Column(name = "Price")
    private BigDecimal price;

    @Column(name = "BusinessHoursStart")
    private Time businessHoursStart;

    @Column(name = "BusinessHoursEnd")
    private Time businessHoursEnd;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedDate")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LastUpdatedDate")
    private Date lastUpdatedDate;

    @Column(name = "Status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "CreatedBy", referencedColumnName = "AdminUserID")
    private AdminUser createdBy;

    @ManyToOne
    @JoinColumn(name = "UpdatedBy", referencedColumnName = "AdminUserID")
    private AdminUser updatedBy;

    // Constructors
    public AlaCarteService() {}

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public AdminUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AdminUser createdBy) {
        this.createdBy = createdBy;
    }

    public AdminUser getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(AdminUser updatedBy) {
        this.updatedBy = updatedBy;
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
