package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.template.TemplateException;
import model.Order;
import model.Payment;
import model.Subscriber;
import model.SubscriptionPackage;
import repository.OrderRepository;
import repository.SubscriberRepository;
import repository.SubscriptionPackageRepository;
import service.EmailService;
import service.PaymentService;
import service.ServiceCompletionServiceNew;
import service.SubscriberService;

@Controller
@RequestMapping("/cashfree")
public class CashfreeController {

	 @Autowired
	 private ServiceCompletionServiceNew serviceCompletionService;
	     
	@Autowired
    private OrderRepository orderRepository;
	
	@Autowired
    private SubscriberRepository subscriberRepository;
	
	@Autowired
    private SubscriptionPackageRepository subscriptionPackageRepository;

	@Autowired
    private EmailService emailService;

	@Autowired
    private PaymentService paymentService;
    
	private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${CASHFREE_APP_ID}")
    private String clientId;

    @Value("${CASHFREE_SECRET_KEY}")
    private String clientSecret;

    private static final String BASE_URL = "https://sandbox.cashfree.com/pg/orders/";
    private static final String API_VERSION = "2023-08-01";

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/order/{orderID}")
    public ResponseEntity<Order> fetchAndUpdateOrderDetails(@PathVariable("orderID") Integer orderID) throws MessagingException, IOException, TemplateException {
        // Step 1: Fetch order details from Cashfree API
        String url = BASE_URL + orderID;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-version", API_VERSION);
        headers.set("x-client-id", clientId);
        headers.set("x-client-secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response;
        Integer packageID = null;

        try {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(response.getStatusCode()).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }

        // Step 2: Parse and update the order in the database
        String responseBody = response.getBody();
        String fetchedOrderStatus = parseOrderStatus(responseBody);
        String fetchedPaymentSessionID = parsePaymentSessionID(responseBody);

        Optional<Order> existingOrderOptional = orderRepository.findById(orderID);
        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();
            packageID = existingOrder.getPackageID();
            boolean isUpdated = false;

            if (fetchedOrderStatus != null && !fetchedOrderStatus.equals(existingOrder.getOrderStatus())) {
                existingOrder.setOrderStatus(fetchedOrderStatus);
                isUpdated = true;
            }
            if (fetchedPaymentSessionID != null && !fetchedPaymentSessionID.equals(existingOrder.getPaymentSessionID())) {
                existingOrder.setPaymentSessionID(fetchedPaymentSessionID);
                isUpdated = true;
            }
            if (isUpdated) {
                existingOrder.setUpdatedAt(new Date());
                orderRepository.save(existingOrder);
            }

            String paymentStatus = null;
            Payment payment = fetchAndSaveLatestPaymentDetails(orderID);
            if (payment != null) {
                paymentStatus = payment.getPaymentStatus();
            }
            Integer subscriberID = existingOrder.getSubscriberID();
            
            Optional<Subscriber> subscriberOptional = subscriberRepository.findById(subscriberID);
            
          
            // Check if order status should be updated based on payment status
            if (!"PAID".equalsIgnoreCase(existingOrder.getOrderStatus()) && paymentStatus != null) {
                existingOrder.setOrderStatus(paymentStatus);
                existingOrder.setUpdatedAt(new Date());
                orderRepository.save(existingOrder);
                if (subscriberOptional.isPresent()) {
                    Subscriber subscriber = subscriberOptional.get();
           	 
           	emailService.sendPaymentFailureEmail(
           	        subscriber.getEmail(),
           	        subscriber.getFirstName(),
           	        "suchigupta@etheriumtech.com"
           	    );
            }
            }
            if (!"PAID".equalsIgnoreCase(existingOrder.getOrderStatus()) && paymentStatus == null) {
                existingOrder.setOrderStatus("CANCELLED");
                existingOrder.setUpdatedAt(new Date());
                orderRepository.save(existingOrder);
                if (subscriberOptional.isPresent()) {
                    Subscriber subscriber = subscriberOptional.get();
           	 
           	emailService.sendPaymentFailureEmail(
           	        subscriber.getEmail(),
           	        subscriber.getFirstName(),
           	        "suchigupta@etheriumtech.com"
           	    );
            }
            }
            // Step 3: Update billing status if order status is "PAID"
            if ("PAID".equalsIgnoreCase(fetchedOrderStatus)) {
                SubscriptionPackage subscriptionPackage = subscriptionPackageRepository.findById(packageID)
                        .orElseThrow(() -> new RuntimeException("SubscriptionPackage not found with this ID"));

                if (subscriberOptional.isPresent()) {
                    Subscriber subscriber = subscriberOptional.get();
                    subscriber.setBillingStatus(1);
                    subscriber.setSubscriptionPackage(subscriptionPackage);
                    subscriberRepository.save(subscriber);
                    serviceCompletionService.rebuildAllServices(subscriberID);
                    emailService.sendPaymentSuccessEmail(
                            subscriber.getEmail(),
                            subscriber.getFirstName(),
                            "suchigupta@etheriumtech.com"
                        );
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            }
           
            
            return ResponseEntity.ok(existingOrder);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    

    // Placeholder methods for parsing orderStatus and paymentSessionID from Cashfree API response
    private String parseOrderStatus(String responseBody) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.path("order_status").asText();  // Extract order_status
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private String parsePaymentSessionID(String responseBody) {
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.path("payment_session_id").asText();  // Extract payment_session_id
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @PutMapping("/order/{orderID}")
    public ResponseEntity<String> updateOrder(@PathVariable Integer orderID, @RequestBody Order updatedOrder) throws MessagingException, IOException, TemplateException {
        Optional<Order> existingOrderOptional = orderRepository.findById(orderID);

        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();
            
            if (updatedOrder.getOrderStatus() != null) existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
 //           if (updatedOrder.getPaymentSessionID() != null) existingOrder.setPaymentSessionID(updatedOrder.getPaymentSessionID());
            existingOrder.setUpdatedAt(new Date());

            // Save the updated order
            orderRepository.save(existingOrder);
            Integer subscriberID = existingOrder.getSubscriberID();
            Optional<Subscriber> subscriberOptional = subscriberRepository.findById(subscriberID);
            
          
            // Check if order status is "PAID" and update billing status of the subscriber
            if ("PAID".equalsIgnoreCase(updatedOrder.getOrderStatus())) {
            	
                if (subscriberOptional.isPresent()) {
                    Subscriber subscriber = subscriberOptional.get();
                    subscriber.setBillingStatus(1);  // Set billing status to 1
                    subscriberRepository.save(subscriber);  // Save updated subscriber
                    
                    emailService.sendPaymentSuccessEmail(
                            subscriber.getEmail(),
                            subscriber.getFirstName(),
                            "suchigupta@etheriumtech.com"
                        );
                	System.out.println("Sending email");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subscriber not found.");
                }
            }
            else
            {
            	System.out.println("hello");
            	 if (subscriberOptional.isPresent()) {
                     Subscriber subscriber = subscriberOptional.get();
            	 
            	emailService.sendPaymentFailureEmail(
            	        subscriber.getEmail(),
            	        subscriber.getFirstName(),
            	        "suchigupta@etheriumtech.com"
            	    );
            }
            }
            return ResponseEntity.ok("Order and billing status updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
        }
    }
    
    public Payment fetchAndSaveLatestPaymentDetails(Integer orderID) {
        String url = BASE_URL + orderID + "/payments";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-version", API_VERSION);
        headers.set("x-client-id", clientId);
        headers.set("x-client-secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response;

        try {
            // Fetch payment details from Cashfree API
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to fetch data from Cashfree");
            }

            // Parse JSON array response and map each element to a Payment object
            JsonNode rootArray = objectMapper.readTree(response.getBody());
            List<Payment> payments = new ArrayList<>();

            if (rootArray.isArray() && rootArray.size() > 0) {
                for (JsonNode paymentNode : rootArray) {
                    Payment payment = parsePaymentFromResponse(paymentNode);
                    paymentService.savePayment(payment);  // Save each payment in the database
                    payments.add(payment);  // Add payment to the list
                }
            } else {
                return null;
            }

            // Find the latest payment based on payment_completion_time
            return payments.stream()
                    .max(Comparator.comparing(Payment::getPaymentCompletionTime))
                    .orElse(null); // Return null if no payments are found
        } catch (Exception e) {
            // Improved error handling and logging
            throw new RuntimeException("Error while processing Cashfree API response: " + (e.getMessage() != null ? e.getMessage() : "null"), e);
        }
    }

    private Payment parsePaymentFromResponse(JsonNode paymentNode) throws IOException {
        Payment payment = new Payment();

        // Mapping fields from the payment JSON node
        payment.setCfPaymentID(paymentNode.path("cf_payment_id").asText());
        payment.setEntity(paymentNode.path("entity").asText());
        payment.setIsCaptured(paymentNode.path("is_captured").asBoolean());
        payment.setOrderAmount(paymentNode.path("order_amount").asDouble());
        payment.setOrderID(paymentNode.path("order_id").asText());
        payment.setPaymentAmount(paymentNode.path("payment_amount").asDouble());

        // Parsing date-time fields
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        payment.setPaymentCompletionTime(LocalDateTime.parse(paymentNode.path("payment_completion_time").asText(), formatter));
        payment.setPaymentTime(LocalDateTime.parse(paymentNode.path("payment_time").asText(), formatter));

        payment.setPaymentCurrency(paymentNode.path("payment_currency").asText());
        payment.setPaymentGroup(paymentNode.path("payment_group").asText());
        payment.setPaymentStatus(paymentNode.path("payment_status").asText());

        // PaymentGatewayDetails mapping
        Payment.PaymentGatewayDetails gatewayDetails = new Payment.PaymentGatewayDetails();
        JsonNode gatewayNode = paymentNode.path("payment_gateway_details");
        gatewayDetails.setGatewayName(gatewayNode.path("gateway_name").asText());
        gatewayDetails.setGatewayOrderID(gatewayNode.path("gateway_order_id").asText());
        gatewayDetails.setGatewayPaymentID(gatewayNode.path("gateway_payment_id").asText());
        gatewayDetails.setGatewayOrderReferenceID(gatewayNode.path("gateway_order_reference_id").asText());
        gatewayDetails.setGatewayStatusCode(gatewayNode.path("gateway_status_code").asText());
        gatewayDetails.setGatewaySettlement(gatewayNode.path("gateway_settlement").asText());
        payment.setPaymentGatewayDetails(gatewayDetails);

        // PaymentMethod mapping
        Payment.PaymentMethod paymentMethod = new Payment.PaymentMethod();
        JsonNode upiNode = paymentNode.path("payment_method").path("upi");
        paymentMethod.setChannel(upiNode.path("channel").asText());
        payment.setPaymentMethod(paymentMethod);

        return payment;
    }

}
