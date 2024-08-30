package model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "AdminUsers")
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AdminUserID")
    private int adminUserID;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "Email")
    private String email;

    @Temporal(TemporalType.DATE)
    @Column(name = "DOB")
    private Date dob; 

    @Column(name = "ContactNo")
    private String contactNo;

    @Column(name = "CountryCode")
    private String countryCode;

    @Column(name = "BriefBio")
    private String briefBio;

    @Column(name = "Picture")
    private String picture;

    @Column(name = "UserType")
    private String userType;

    @Column(name = "Password")
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CreatedDate")
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LastUpdatedDate")
    private Date lastUpdatedDate;

    @Column(name = "Status")
    private Integer status;

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "UpdatedBy")
    private Integer updatedBy;

    @OneToMany(mappedBy = "saathi")
    @JsonIgnore
    private List<Subscriber> subscribers = new ArrayList<>();

    // Date format to be used for date fields
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Getters and Setters

    public int getAdminUserID() {
        return adminUserID;
    }

    public void setAdminUserID(int adminUserID) {
        this.adminUserID = adminUserID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Custom getter for DOB to return a formatted date string
    public String getDob() {
        return dob != null ? DATE_FORMAT.format(dob) : null;
    }

    // Custom setter for DOB to parse a date string and set the Date object
    public void setDob(String dobStr) {
        try {
            this.dob = dobStr != null ? DATE_FORMAT.parse(dobStr) : null;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getBriefBio() {
        return briefBio;
    }

    public void setBriefBio(String briefBio) {
        this.briefBio = briefBio;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Custom getter for CreatedDate to return a formatted date string
    public String getCreatedDate() {
        return createdDate != null ? DATE_FORMAT.format(createdDate) : null;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    // Custom getter for LastUpdatedDate to return a formatted date string
    public String getLastUpdatedDate() {
        return lastUpdatedDate != null ? DATE_FORMAT.format(lastUpdatedDate) : null;
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

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Subscriber> subscribers) {
        this.subscribers = subscribers;
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
