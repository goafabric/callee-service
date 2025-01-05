package org.goafabric.calleeservice.mail;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MyMailSender {
    @Autowired
    private JavaMailSender emailSender;
    
    @PostConstruct
    public void init() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo("amautsch@gmail.com");
        message.setSubject("test subject");
        message.setText("hello world");
        emailSender.send(message);
    }
}
