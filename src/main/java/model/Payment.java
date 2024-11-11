package model;

import java.time.LocalDateTime;


import javax.persistence.*;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String cfPaymentID;
    private String entity;
    private Boolean isCaptured;
    private Double orderAmount;
    private String orderID;
    private Double paymentAmount;
    private LocalDateTime paymentCompletionTime;
    private String paymentCurrency;
    private String paymentGroup;
    private String paymentStatus;
    private LocalDateTime paymentTime;

    @Embedded
    private PaymentGatewayDetails paymentGatewayDetails;

    @Embedded
    private PaymentMethod paymentMethod;

    // Additional fields with nullable settings
    private String authID;
    private String authorization;
    private String bankReference;
    private String errorDetails;
    private String paymentMessage;

    
    // Getters and Setters
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCfPaymentID() {
		return cfPaymentID;
	}

	public void setCfPaymentID(String cfPaymentID) {
		this.cfPaymentID = cfPaymentID;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Boolean getIsCaptured() {
		return isCaptured;
	}

	public void setIsCaptured(Boolean isCaptured) {
		this.isCaptured = isCaptured;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public Double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public LocalDateTime getPaymentCompletionTime() {
		return paymentCompletionTime;
	}

	public void setPaymentCompletionTime(LocalDateTime paymentCompletionTime) {
		this.paymentCompletionTime = paymentCompletionTime;
	}

	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}

	public String getPaymentGroup() {
		return paymentGroup;
	}

	public void setPaymentGroup(String paymentGroup) {
		this.paymentGroup = paymentGroup;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(LocalDateTime paymentTime) {
		this.paymentTime = paymentTime;
	}

	public PaymentGatewayDetails getPaymentGatewayDetails() {
		return paymentGatewayDetails;
	}

	public void setPaymentGatewayDetails(PaymentGatewayDetails paymentGatewayDetails) {
		this.paymentGatewayDetails = paymentGatewayDetails;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getAuthID() {
		return authID;
	}

	public void setAuthID(String authID) {
		this.authID = authID;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public String getBankReference() {
		return bankReference;
	}

	public void setBankReference(String bankReference) {
		this.bankReference = bankReference;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public String getPaymentMessage() {
		return paymentMessage;
	}

	public void setPaymentMessage(String paymentMessage) {
		this.paymentMessage = paymentMessage;
	}

	// Embedded Classes for Nested JSON
    @Embeddable
    public static class PaymentGatewayDetails {
        public String getGatewayName() {
			return gatewayName;
		}
		public void setGatewayName(String gatewayName) {
			this.gatewayName = gatewayName;
		}
		
		public String getGatewayOrderID() {
			return gatewayOrderID;
		}
		public void setGatewayOrderID(String gatewayOrderID) {
			this.gatewayOrderID = gatewayOrderID;
		}
		public String getGatewayPaymentID() {
			return gatewayPaymentID;
		}
		public void setGatewayPaymentID(String gatewayPaymentID) {
			this.gatewayPaymentID = gatewayPaymentID;
		}
		public String getGatewayOrderReferenceID() {
			return gatewayOrderReferenceID;
		}
		public void setGatewayOrderReferenceID(String gatewayOrderReferenceID) {
			this.gatewayOrderReferenceID = gatewayOrderReferenceID;
		}
		public String getGatewayStatusCode() {
			return gatewayStatusCode;
		}
		public void setGatewayStatusCode(String gatewayStatusCode) {
			this.gatewayStatusCode = gatewayStatusCode;
		}
		public String getGatewaySettlement() {
			return gatewaySettlement;
		}
		public void setGatewaySettlement(String gatewaySettlement) {
			this.gatewaySettlement = gatewaySettlement;
		}
		private String gatewayName;
        private String gatewayOrderID;
        private String gatewayPaymentID;
        private String gatewayOrderReferenceID;
        private String gatewayStatusCode;
        private String gatewaySettlement;
        
        // Getters and Setters
    }

    @Embeddable
    public static class PaymentMethod {
        private String channel;

		public String getChannel() {
			return channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}
        
        
        // Getters and Setters
    }
}
