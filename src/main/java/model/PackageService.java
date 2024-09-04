package model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "PackageServices")
public class PackageService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PackageServicesID")
    private Integer packageServicesID;

    @ManyToOne
    @JoinColumn(name = "PackageID", referencedColumnName = "PackageID")
    private SubscriptionPackage subscriptionPackage;

    @ManyToOne
    @JoinColumn(name = "ServiceID", referencedColumnName = "ServiceID")
    private AlaCarteService service;

    @Column(name = "Frequency")
    private Integer frequency;

    @Column(name = "Price")
    private BigDecimal price;

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
    public PackageService() {}

    // Getters and Setters
    public Integer getPackageServicesID() {
        return packageServicesID;
    }

    public void setPackageServicesID(Integer packageServicesID) {
        this.packageServicesID = packageServicesID;
    }

    public SubscriptionPackage getSubscriptionPackage() {
        return subscriptionPackage;
    }

    public void setSubscriptionPackage(SubscriptionPackage subscriptionPackage) {
        this.subscriptionPackage = subscriptionPackage;
    }

    public AlaCarteService getService() {
        return service;
    }

    public void setService(AlaCarteService service) {
        this.service = service;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
}
