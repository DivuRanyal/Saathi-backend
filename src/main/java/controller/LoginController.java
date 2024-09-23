package controller;

import model.AdminUser;
import model.Subscriber;
import service.AdminUsersService;
import service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AdminUsersService adminUserService;

    @Autowired
    private SubscriberService subscriberService;

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdminUser(@RequestParam String email, @RequestParam String password) {
        AdminUser adminUser = adminUserService.findByEmailAndPassword(email, password);

        if (adminUser != null) {
            return ResponseEntity.ok(adminUser);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials for Admin");
        }
    }

    @PostMapping("/subscribers/login")
    public ResponseEntity<?> loginSubscriber(@RequestParam String email, @RequestParam String password) {
        Subscriber subscriber = subscriberService.findByEmailAndPassword(email, password);
      
        if (subscriber != null) {
            return ResponseEntity.ok(subscriber);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials for Subscriber");
        }
    }
}