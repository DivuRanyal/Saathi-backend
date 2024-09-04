package service;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class DecryptionService {

    public String decrypt(String encryptedPassword) {
        // Assuming it's Base64-encoded for simplicity, use your actual decryption logic here
        return new String(Base64.getDecoder().decode(encryptedPassword));
    }
}
