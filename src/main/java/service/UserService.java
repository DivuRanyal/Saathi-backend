package service;

import model.Users;
import repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
@Transactional

public class UserService {

    @Autowired
    private UserRepository userRepository;


    public Users registerUser(Users user) {
        // Hash the password before saving
        user.setPassword((user.getPassword()));
        return userRepository.save(user);
    }

    public Users findByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber);
    }

    public Users findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Users findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public Users save(Users user) {
        return userRepository.save(user);
    }
}
