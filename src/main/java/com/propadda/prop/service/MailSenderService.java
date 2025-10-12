package com.propadda.prop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailSenderService {

    private final JavaMailSender mailSender;

    // @Value("${app.mail.from}")
    // private String from;

    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    private static final Logger log = LoggerFactory.getLogger(MailSenderService.class);

    public void send(String to, String subject, String body) {
        log.info("Sending mail to {} | {} | {}", to, subject, body);
         SimpleMailMessage msg = new SimpleMailMessage();
        // msg.setFrom(from);
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);
    }

    public void sendHtml(String to, String subject, String html) throws MessagingException {
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, "UTF-8");
        // helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true); // true -> HTML
        mailSender.send(mime);
    }
}