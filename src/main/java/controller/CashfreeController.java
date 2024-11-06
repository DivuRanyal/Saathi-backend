package controller;

import java.util.Date;
import java.util.Optional;

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

import model.Order;
import model.Subscriber;
import model.SubscriptionPackage;
import repository.OrderRepository;
import repository.SubscriberRepository;
import repository.SubscriptionPackageRepository;

@Controller
@RequestMapping("/cashfree")
public class CashfreeController {

	@Autowired
    private OrderRepository orderRepository;
	
	@Autowired
    private SubscriberRepository subscriberRepository;
	
	@Autowired
    private SubscriptionPackageRepository subscriptionPackageRepository;

	private final ObjectMapper objectMapper = new ObjectMapper();
    @Value("${CASHFREE_APP_ID}")
    private String clientId;

    @Value("${CASHFREE_SECRET_KEY}")
    private String clientSecret;

    private static final String BASE_URL = "https://sandbox.cashfree.com/pg/orders/";
    private static final String API_VERSION = "2023-08-01";

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/order/{orderID}")
    public ResponseEntity<String> fetchAndUpdateOrderDetails(@PathVariable("orderID") Integer orderID) {
        // Step 1: Fetch order details from Cashfree API
        String url = BASE_URL + orderID;
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-version", API_VERSION);
        headers.set("x-client-id", clientId);
        headers.set("x-client-secret", clientSecret);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response;
        Integer packageID=null;

        try {
            response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(response.getStatusCode()).body("Failed to fetch data from Cashfree");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while calling Cashfree API: " + e.getMessage());
        }

        // Step 2: Parse and update the order in the database
        String responseBody = response.getBody();
        String fetchedOrderStatus = parseOrderStatus(responseBody);  // Implement this method to extract orderStatus from response
        String fetchedPaymentSessionID = parsePaymentSessionID(responseBody);  // Implement this method to extract paymentSessionID from response

        Optional<Order> existingOrderOptional = orderRepository.findById(orderID);
        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();
            packageID=existingOrder.getPackageID();
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

            // Step 3: Update billing status if order status is "PAID"
            if ("PAID".equalsIgnoreCase(fetchedOrderStatus)) {
                Integer subscriberID = existingOrder.getSubscriberID();
                SubscriptionPackage subscriptionPackage = subscriptionPackageRepository.findById(packageID)
                        .orElseThrow(() -> new RuntimeException("SubscriptionPackage not found with this ID "));
           
                Optional<Subscriber> subscriberOptional = subscriberRepository.findById(subscriberID);
                if (subscriberOptional.isPresent()) {
                    Subscriber subscriber = subscriberOptional.get();
                    subscriber.setBillingStatus(1);  // Set billing status to 1
                    subscriber.setSubscriptionPackage(subscriptionPackage);
                    subscriberRepository.save(subscriber);  // Save updated subscriber
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subscriber not found.");
                }
            }
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found in the database.");
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
    public ResponseEntity<String> updateOrder(@PathVariable Integer orderID, @RequestBody Order updatedOrder) {
        Optional<Order> existingOrderOptional = orderRepository.findById(orderID);

        if (existingOrderOptional.isPresent()) {
            Order existingOrder = existingOrderOptional.get();

            
            if (updatedOrder.getOrderStatus() != null) existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
            if (updatedOrder.getPaymentSessionID() != null) existingOrder.setPaymentSessionID(updatedOrder.getPaymentSessionID());
            existingOrder.setUpdatedAt(new Date());

            // Save the updated order
            orderRepository.save(existingOrder);

            // Check if order status is "PAID" and update billing status of the subscriber
            if ("PAID".equalsIgnoreCase(updatedOrder.getOrderStatus())) {
                Integer subscriberID = existingOrder.getSubscriberID();
                Optional<Subscriber> subscriberOptional = subscriberRepository.findById(subscriberID);
                if (subscriberOptional.isPresent()) {
                    Subscriber subscriber = subscriberOptional.get();
                    subscriber.setBillingStatus(1);  // Set billing status to 1
                    subscriberRepository.save(subscriber);  // Save updated subscriber
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Subscriber not found.");
                }
            }

            return ResponseEntity.ok("Order and billing status updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found.");
        }
    }
    
}
