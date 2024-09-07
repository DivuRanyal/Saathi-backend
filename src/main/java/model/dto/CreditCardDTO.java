package model.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreditCardDTO {

    private int creditCardID;
    private String cardNumber;
    private String expiryDate;  // String for formatted date
    private Integer cvv;
    private Integer status;
    private int subscriberID;  // Assuming subscriber ID is Long

    // Getters and Setters
    public int getCreditCardID() {
        return creditCardID;
    }

    public void setCreditCardID(int creditCardID) {
        this.creditCardID = creditCardID;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(int i) {
        this.subscriberID = i;
    }
}
