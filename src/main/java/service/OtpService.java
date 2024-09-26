package service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freemarker.template.TemplateException;

@Service
public class OtpService {

    @Autowired
    private EmailService emailService;

    private static final int OTP_VALID_DURATION = 5 * 60 * 1000; // 5 minutes

    public Integer generateOtp() {
        Random random = new Random();
        Integer otp = 100000 + random.nextInt(900000); // Generate a 6-digit OTP
       return otp;
    }

    public boolean isOtpExpired(Date otpGeneratedTime) {
        long currentTimeInMillis = System.currentTimeMillis();
        long otpTimeInMillis = otpGeneratedTime.getTime();
        return (currentTimeInMillis - otpTimeInMillis) > OTP_VALID_DURATION;
    }

    public void sendOtpEmail(String subscriberEmail, Integer otp, String firstName) throws MessagingException, IOException, TemplateException {
        // Create a model for the email template
        Map<String, Object> model = new HashMap<>();
        model.put("firstName", firstName);
        model.put("otp", otp);

        // Use the emailService to send the OTP email using the template
        emailService.sendEmail(subscriberEmail, "Your OTP Code", "otp-email.ftlh", model);
    }
}
