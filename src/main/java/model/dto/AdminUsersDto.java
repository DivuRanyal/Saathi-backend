package model.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.web.multipart.MultipartFile;

public class AdminUsersDto {
    
    private String firstName;
    private String lastName;
    private String email;
    
    private Date dob; // Or use java.util.Date if preferred
    private String contactNo;
    private String countryCode;
    private String briefBio;
    private String userType;
    private String password;
    private Integer status;
    private Integer createdBy;
    private Integer updatedBy;
    private MultipartFile picture;
    
 // Date format to be used for date fields
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

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
	public MultipartFile getPicture() {
		return picture;
	}
	public void setPicture(MultipartFile picture) {
		this.picture = picture;
	}

    
    // Getters and Setters
    // Include all fields here
}
