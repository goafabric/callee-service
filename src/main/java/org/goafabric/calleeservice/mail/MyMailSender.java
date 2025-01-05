package org.goafabric.calleeservice.mail;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MyMailSender {
    @Autowired
    private EmailAdapter emailAdapter;
    
    @PostConstruct
    public void init() {
        emailAdapter.sendMail(
                "amautsch@gmx.de", "amautsch@gmx.de",
                "test subject " + System.currentTimeMillis(), "hello world");
    }

    @Component
    //Todo: Use @Circuitbreaker from resilience4j here, to enable background sending and timeouts
    static class EmailAdapter {
        private final JavaMailSender mailSender;

        public EmailAdapter(JavaMailSender mailSender) {
            this.mailSender = mailSender;
        }

        public void sendMail(String from, String to, String subject, String text) {
            var message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject + System.currentTimeMillis());
            message.setText("hello world");

            mailSender.send(message);
        }
    }
}
