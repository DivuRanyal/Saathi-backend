package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import model.AdminUser;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration freemarkerConfig;

    public void sendSaathiAssignedEmail(String saathiEmail, Map<String, Object> model) throws MessagingException, IOException, TemplateException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Set the From, To, and Subject headers
        helper.setFrom("info@etheriumtech.com");  // Set a valid "From" email
        helper.setTo(saathiEmail);
        helper.setSubject("You Have Been Assigned a New Subscriber!");

        // Process the email template with the provided model
        String emailContent = processTemplateIntoString("saathi-assigned.ftlh", model);  // Process Freemarker template

        // Set the email content
        helper.setText(emailContent, true);  // true for HTML content

        // Send the email
        mailSender.send(message);
    }

    private String processTemplateIntoString(String templateName, Map<String, Object> model) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate(templateName);  // Get the template
        StringWriter stringWriter = new StringWriter();  // Create a StringWriter to capture the output
        template.process(model, stringWriter);  // Process the template with the model and output to the StringWriter
        return stringWriter.toString();  // Return the resulting string
    }

    public void sendEmail(String to, String subject, String templateName, Map<String, Object> model) throws MessagingException, IOException, TemplateException {
        // Create a MimeMessage
        MimeMessage message = mailSender.createMimeMessage();
        
        // Use MimeMessageHelper to construct the email message
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");
        
        // Set the email's recipient, subject, and from address
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("info@etheriumtech.com");  // Replace with your from address
        
        // Get the Freemarker template and process it with the model
        Template t = freemarkerConfig.getTemplate(templateName);
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        
        // Set the processed template as the email content
        helper.setText(text, true); // true indicates that this is an HTML email

        // Send the email
        mailSender.send(message);
    }
    
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
 //       message.setFrom("divyasharma@etheriumtech.com"); // Set your sender email address
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    public void sendAdminEmail(String password,AdminUser adminUser) throws MessagingException, IOException, TemplateException {
        // Create a MimeMessage
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Set the From, To, and Subject headers
        helper.setFrom("info@etheriumtech.com", "Saathi-INFO"); // Proper email with display name
         // Set a valid "From" email address
        helper.setTo(adminUser.getEmail());
        helper.setSubject("Your Admin Account Credentials");
       
        // Create a model to pass data to the FreeMarker template
        Map<String, Object> model = new HashMap<>();
        model.put("adminFirstName", adminUser.getFirstName());
        model.put("adminLastName", adminUser.getLastName() != null ? adminUser.getLastName() : ""); // Handle null last name
        model.put("adminEmail", adminUser.getEmail());
        model.put("adminPassword", password); // Optionally a reset link
        model.put("adminLoginLink", "https://saathi.etheriumtech.com/admin/login");

        // Process the FreeMarker template and generate email content
        String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(
            freemarkerConfig.getTemplate("admin_template.ftlh"), model
        );

        // Set the email content (HTML)
        helper.setText(emailContent, true);

        // Send the email
        mailSender.send(message);
    }
    
    public void sendSaathiEmail(String password, AdminUser saathiUser) throws MessagingException, IOException, TemplateException {
        // Create a MimeMessage
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Set the From, To, and Subject headers
     // Set the From header with a custom display name and email address
        try {
           helper.setFrom(new InternetAddress("info@etheriumtech.com", "Saathi-INFO"));
        	// Ensure display name is applied
        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Failed to set display name", e);
        }
         helper.setTo(saathiUser.getEmail());
        helper.setSubject("Your Saathi Account Credentials");
      
        // Create a model to pass data to the FreeMarker template
        Map<String, Object> model = new HashMap<>();
        model.put("saathiFirstName", saathiUser.getFirstName());
        model.put("saathiLastName", saathiUser.getLastName() != null ? saathiUser.getLastName() : ""); // Handle null last name
        model.put("saathiEmail", saathiUser.getEmail());
        model.put("saathiPassword", password); // Optionally a reset link
        model.put("saathiLoginLink", "https://saathi.etheriumtech.com/saathi/login");

        // Process the FreeMarker template and generate email content
        String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(
            freemarkerConfig.getTemplate("saathi_template.ftlh"), model
        );

        // Set the email content (HTML)
        helper.setText(emailContent, true);

        // Send the email
        mailSender.send(message);
    }

}
