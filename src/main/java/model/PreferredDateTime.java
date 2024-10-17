package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PreferredDateTime implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private LocalDate preferredDate;          // Date preferred for the service
    private LocalTime preferredTime;          // Time preferred for the service
    private String completionStatus;          // To track completion per frequency
    private LocalDate completionDate;     // Date and time when the service was completed

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Apply the same format for requestedDate
    private LocalDateTime requestedDate;      // Date and time when the request was made

    // Constructor for preferred date and time, requested date is set to system time
    public PreferredDateTime(LocalDate preferredDate, LocalTime preferredTime) {
        this.preferredDate = preferredDate;
        this.preferredTime = preferredTime;
        this.completionStatus = "Pending"; // Default status
        this.requestedDate = LocalDateTime.now(); // Automatically set requested date to current system time
    }

    // Full Constructor including completion details (if needed)
    public PreferredDateTime(LocalDate preferredDate, LocalTime preferredTime, 
                             String completionStatus, LocalDate completionDate) {
        this.preferredDate = preferredDate;
        this.preferredTime = preferredTime;
        this.completionStatus = completionStatus;
        this.completionDate = completionDate;
        this.requestedDate = LocalDateTime.now(); // Automatically set requested date to current system time
    }

    // Getters and Setters
    public LocalDate getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(LocalDate preferredDate) {
        this.preferredDate = preferredDate;
    }

    public LocalTime getPreferredTime() {
        return preferredTime;
    }

    public void setPreferredTime(LocalTime preferredTime) {
        this.preferredTime = preferredTime;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public LocalDateTime getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDateTime requestedDate) {
        this.requestedDate = requestedDate;
    }

    @Override
    public String toString() {
        return "PreferredDateTime{" +
                "preferredDate=" + preferredDate +
                ", preferredTime=" + preferredTime +
                ", completionStatus='" + completionStatus + '\'' +
                ", completionDate=" + completionDate +
                ", requestedDate=" + requestedDate +
                '}';
    }
    
    public PreferredDateTime() {
    	
    }
    
}