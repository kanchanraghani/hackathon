package com.itiviti.supportrobot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.itiviti.supportrobot.domain.User;

@Service
public class NotificationService
{
    private JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender)
    {
        this.javaMailSender = javaMailSender;
    }

    public void sendNotification(User user) throws MailException
    {
        // send email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmailAddress());
        mailMessage.setFrom("support@itiviti.com");
        mailMessage.setSubject("Your Support Request");
        mailMessage.setText("Here's your request:...");

        javaMailSender.send(mailMessage);
    }
}
