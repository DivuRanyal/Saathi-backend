package controller;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
public class CashfreeWebhookController {

    @PostMapping("/cashfree")
    public HashMap handleCashfreeWebhook(@RequestBody HashMap payload) {
    	
    	System.out.println(payload);
		return payload;
        // Verify signature to ensure the request is from Cashfree
    	
    }
}
