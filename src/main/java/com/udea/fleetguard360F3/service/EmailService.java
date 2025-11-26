package com.udea.fleetguard360F3.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendEmailWithRetry(String to, String subject, String body);
}
