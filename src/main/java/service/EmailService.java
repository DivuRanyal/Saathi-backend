package service;

import org.springframework.beans.factory.annotation.Autowired;
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
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration freemarkerConfig;

    public void sendSaathiAssignedEmail(String to, Map<String, Object> model) throws MessagingException, TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("You Have Been Assigned a New Subscriber!");

        Template t = freemarkerConfig.getTemplate("saathi-assigned.ftlh");
        String text = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

        helper.setText(text, true);

        mailSender.send(message);
    }
}
