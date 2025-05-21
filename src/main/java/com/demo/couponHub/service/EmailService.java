package com.demo.couponHub.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;

    public void sendWelcomeEmail(String toEmail, String firstName) throws MessagingException, IOException, TemplateException {
        String subject = processTemplate("register_user/subject.ftl", firstName);
        String content = processTemplate("register_user/content.ftl", firstName);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(content, true);
        helper.setFrom("your-email@gmail.com");

        mailSender.send(message);
    }

    private String processTemplate(String templateName, String firstName) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate(templateName);
        Map<String, Object> model = new HashMap<>();
        model.put("firstName", firstName);

        StringWriter writer = new StringWriter();
        template.process(model, writer);
        return writer.toString();
    }
}

