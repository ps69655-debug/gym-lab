package edu.uws.ii.lab1.services;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}