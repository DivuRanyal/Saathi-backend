package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderID;

    @Column(name = "subscriberID")
    private Integer subscriberID;

    @Column(name = "cf_order_id")
    private String cfOrderID;

    private double amount;
    private String currency;

    @Column(name = "order_expiry_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderExpiryTime;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "payment_session_id")
    private String paymentSessionID;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "packageID")
    private Integer packageID;

    // Getters and Setters
	public Integer getOrderID() {
		return orderID;
	}

	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}

	public Integer getSubscriberID() {
		return subscriberID;
	}

	public void setSubscriberID(Integer subscriberID) {
		this.subscriberID = subscriberID;
	}

	public String getCfOrderID() {
		return cfOrderID;
	}

	public void setCfOrderID(String cfOrderID) {
		this.cfOrderID = cfOrderID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Date getOrderExpiryTime() {
		return orderExpiryTime;
	}

	public void setOrderExpiryTime(Date orderExpiryTime) {
		this.orderExpiryTime = orderExpiryTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPaymentSessionID() {
		return paymentSessionID;
	}

	public void setPaymentSessionID(String paymentSessionID) {
		this.paymentSessionID = paymentSessionID;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getPackageID() {
		return packageID;
	}

	public void setPackageID(Integer packageID) {
		this.packageID = packageID;
	}

    
    
    
   }
