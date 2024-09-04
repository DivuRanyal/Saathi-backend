package model.dto;

import java.sql.Time;
import java.util.Date;

public class SubscriberServiceDetailsDTO {

    private int serviceID;
    private Date serviceDate;
    private Time serviceTime;
    private int completionStatus;
    private String interactionType;
    private String documents;

    // Constructor
    public SubscriberServiceDetailsDTO(int serviceID, Date serviceDate, Time serviceTime, int completionStatus, String interactionType, String documents) {
        this.serviceID = serviceID;
        this.serviceDate = serviceDate;
        this.serviceTime = serviceTime;
        this.completionStatus = completionStatus;
        this.interactionType = interactionType;
        this.documents = documents;
    }

    // Getters and setters
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

    public Time getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Time serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(int completionStatus) {
        this.completionStatus = completionStatus;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public String getDocuments() {
        return documents;
    }

    public void setDocuments(String documents) {
        this.documents = documents;
    }
}
