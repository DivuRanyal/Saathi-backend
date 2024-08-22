package model;

import javax.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Table(name = "Demo2")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "mobile_number", unique = true, nullable = false)
    @Pattern(regexp = "^[56789]\\d{9}$", message = "Invalid mobile number")
    private String mobileNumber;

    

    @Column(name = "name", nullable = false)
    @NotEmpty(message = "Name is required")
    private String name;

    @Column(name = "email", unique = true)
    @Email(message = "Invalid email address")
    private String email;

    @Column(name = "password", nullable = false)
    @NotEmpty(message = "Password is required")
    private String password;


    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

   

 
}
