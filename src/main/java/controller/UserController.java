package controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;
import model.Users;
import service.UserService;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

	@PostMapping("/data")
    @ResponseBody
    public String login_User() {
 //       Map<String, String> response = new HashMap();
        return "Hi";
	}

	@PostMapping("/register")
	
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody Users user, BindingResult result) {
      
         Map<String, String> response = new HashMap<>();

  /*      if (result.hasErrors()) {
            result.getFieldErrors().forEach(error -> response.put(error.getField(), error.getDefaultMessage()));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
*/
       
        Users existingUser = userService.findByMobileNumber(user.getMobileNumber());
        if (existingUser != null) {
            response.put("error", "Mobile number already registered");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setName(user.getName());
        user.setEmail(user.getEmail());
        user.setMobileNumber(user.getMobileNumber());
        
        // Save the user to the database
        userService.save(user);

        response.put("message", "User registered successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
	
}
