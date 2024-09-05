package model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "SubscriptionPackages")
public class SubscriptionPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PackageID")
    private int packageID;

    @Column(name = "PackageName")
    private String packageName;

    @Column(name = "PackageDescription")
    private String packageDescription;

    // Setting default values for price fields to 0.00
    @Column(name = "PriceUSD")
    private BigDecimal priceUSD = BigDecimal.valueOf(0.00);

    @Column(name = "PriceINR")
    private BigDecimal priceINR = BigDecimal.valueOf(0.00);


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedDate", nullable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LastUpdatedDate")
    private Date lastUpdatedDate;

    @Column(name = "Status", nullable = false)
    private int status;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "UpdatedBy")
    private Integer updatedBy;

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
 // Date format to be used for date fields
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @PrePersist
    protected void onCreate() {
        createdDate = new Date();  // Set the current timestamp for createdDate
        
        // Ensure default values for price fields
        if (priceUSD == null) {
            priceUSD = BigDecimal.valueOf(0.00);
        }
        if (priceINR == null) {
            priceINR = BigDecimal.valueOf(0.00);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedDate = new Date();  // Set the current timestamp for lastUpdatedDate
        
        // Ensure default values for price fields
        if (priceUSD == null) {
            priceUSD = BigDecimal.valueOf(0.00);
        }
        if (priceINR == null) {
            priceINR = BigDecimal.valueOf(0.00);
        }
    }

    // Constructors
    public SubscriptionPackage() {}

    public SubscriptionPackage(String packageName, String packageDescription, BigDecimal priceUSD, BigDecimal priceINR, Date createdDate, Date lastUpdatedDate, int status) {
        this.packageName = packageName;
        this.packageDescription = packageDescription;
        this.priceUSD = priceUSD;
        this.priceINR = priceINR;
        this.createdDate = createdDate;
        this.lastUpdatedDate = lastUpdatedDate;
        this.status = status;
    }

    // Getters and Setters
    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public BigDecimal getPriceUSD() {
        return priceUSD;
    }

    public void setPriceUSD(BigDecimal priceUSD) {
        this.priceUSD = priceUSD;
    }

    public BigDecimal getPriceINR() {
        return priceINR;
    }

    public void setPriceINR(BigDecimal priceINR) {
        this.priceINR = priceINR;
    }

 /*   public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
*/
    public String getCreatedDate() {
        return createdDate != null ? DATE_FORMAT.format(createdDate) : null;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public String getLastUpdatedDate() {
    	return lastUpdatedDate != null ? DATE_FORMAT.format(lastUpdatedDate) : null;
       
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // toString method for easier debugging
    @Override
    public String toString() {
        return "SubscriptionPackage{" +
                "packageID=" + packageID +
                ", packageName='" + packageName + '\'' +
                ", packageDescription='" + packageDescription + '\'' +
                ", priceUSD=" + priceUSD +
                ", priceINR=" + priceINR +
                ", createdDate=" + createdDate +
                ", lastUpdatedDate=" + lastUpdatedDate +
                ", status=" + status +
                '}';
    }
}