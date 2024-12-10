package com.enigma.jobConnector.services;

import jakarta.mail.MessagingException;

import java.io.IOException;


public interface EmailService {
    void sendEmail(String to, String subject, String url) throws MessagingException, IOException;
}
