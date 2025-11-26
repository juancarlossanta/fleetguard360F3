package com.udea.fleetguard360F3.service.impl;

import com.udea.fleetguard360F3.service.EmailService;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("tu_correo@gmail.com");

        mailSender.send(message);
    }

    @Override
    public void sendEmailWithRetry(String to, String subject, String body) {
        int intentos = 0;
        boolean enviado = false;

        while (intentos < 2 && !enviado) {
            try {
                sendEmail(to, subject, body);
                enviado = true;
            } catch (MailException e) {
                intentos++;
                if (intentos == 2) {
                    System.err.println(" Error enviando correo a " + to + ": " + e.getMessage());
                } else {
                    System.out.println(" Reintentando envío de correo...");
                }
            }
        }
    }
}