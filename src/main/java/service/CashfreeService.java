package service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import model.Order;

@Service
public class CashfreeService {
/*	@Value("${dotenv.path}")
    private String dotenvPath;

    private Dotenv dotenv;
    private String cashfreeAppId;
    private String cashfreeSecretKey;
    private final String createOrderUrl = "https://sandbox.cashfree.com/pg/orders";

    @PostConstruct
    public void init() {
        if (dotenvPath != null) {
            this.dotenv = Dotenv.configure().directory(dotenvPath).load();
            this.cashfreeAppId = dotenv.get("CASHFREE_APP_ID");
            this.cashfreeSecretKey = dotenv.get("CASHFREE_SECRET_KEY");
        } else {
            throw new RuntimeException("dotenv.path property is not set in application.properties");
        }
    }
 
	
	 private CashfreeConfig cashfreeConfig = new CashfreeConfig();
	 private final String createOrderUrl = "https://sandbox.cashfree.com/pg/orders";
	    @Autowired
	    public CashfreeService(CashfreeConfig cashfreeConfig) {
	        this.cashfreeConfig = cashfreeConfig;
	    }
	    String cashfreeAppId = cashfreeConfig.getCashfreeAppId();
        String cashfreeSecretKey = cashfreeConfig.getCashfreeSecretKey();
	    public void processPayment() {
	       

	        // Use appId and secretKey for your Cashfree payment logic
	    }
*/
	 @Value("${CASHFREE_APP_ID}")
	    private String cashfreeAppId;

	    @Value("${CASHFREE_SECRET_KEY}")
	    private String cashfreeSecretKey;
	 private final String createOrderUrl = "https://sandbox.cashfree.com/pg/orders";
	 
    public Order createOrder(Integer orderId, String orderAmount, Integer customerId, String customerPhone, String customerName) throws JsonMappingException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate(); 
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("content-type", "application/json");
        headers.set("x-api-version", "2023-08-01");
        headers.set("x-client-id", cashfreeAppId);
        headers.set("x-client-secret", cashfreeSecretKey);
        // Create request body
        Map<String, Object> requestBody = new HashMap<>();

        // Create customer details
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("customer_id", customerId.toString());
        customerDetails.put("customer_phone", customerPhone);
        customerDetails.put("customer_name", customerName);

        // Add details to the request body
        requestBody.put("customer_details", customerDetails);
        requestBody.put("order_id", orderId.toString());
        requestBody.put("order_amount", orderAmount);
        requestBody.put("order_currency", "INR");
        requestBody.put("returnURL", "https://saathi.etheriumtech.com");

        // Wrap request body in HttpEntity
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        System.out.println(request);
        // Send POST request to Cashfree API and parse the response
        ResponseEntity<String> response = restTemplate.exchange(createOrderUrl, HttpMethod.POST, request, String.class);

        // Parse the response JSON to populate OrderResponse
        Order order = new Order();
        if (response.getStatusCode().is2xxSuccessful()) {
            // Assuming the response body is in JSON format
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());

            // Populate OrderResponse with details from the response JSON
            order.setCfOrderID(jsonResponse.get("cf_order_id").asText());
            order.setOrderStatus(jsonResponse.get("order_status").asText());
            order.setPaymentSessionID(jsonResponse.get("payment_session_id").asText());
            order.setOrderExpiryTime(objectMapper.convertValue(jsonResponse.get("order_expiry_time"), Date.class));
            order.setCreatedAt(objectMapper.convertValue(jsonResponse.get("created_at"), Date.class));
        } else {
            throw new RuntimeException("Failed to create order with Cashfree");
        }

        return order;
    }

}
