package controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/cashfree")
public class CashfreeController {

    @Value("${CASHFREE_APP_ID}")
    private String clientId;

    @Value("${CASHFREE_SECRET_KEY}")
    private String clientSecret;

    private static final String BASE_URL = "https://sandbox.cashfree.com/pg/orders/";
    private static final String API_VERSION = "2023-08-01";

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/order/{orderID}")
    public ResponseEntity<String> getOrderDetails(@PathVariable("orderID") String orderID) {
        // Construct the URL with the provided orderID
        String url = BASE_URL + orderID;

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-version", API_VERSION);
        headers.set("x-client-id", clientId);
        headers.set("x-client-secret", clientSecret);

        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Send GET request using RestTemplate
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Failed to fetch data from Cashfree");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while calling Cashfree API: " + e.getMessage());
        }
    }
}
