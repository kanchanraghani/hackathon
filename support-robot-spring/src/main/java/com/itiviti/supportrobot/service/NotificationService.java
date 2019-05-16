package com.itiviti.supportrobot.service;

import java.nio.file.Path;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService
{
    private JavaMailSender emailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender)
    {
        this.emailSender = javaMailSender;
    }

    public void sendNotification(String email)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("support@itiviti.com");
        message.setSubject("Your Logs Request");
        message.setText("Here's your request:...");

        emailSender.send(message);
    }

    public void sendReplyWithAttachment(String email, Path path) throws MessagingException
    {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("support@itiviti.com");
        helper.setTo(email);
        helper.setSubject("Your Session Information");
        helper.setText("Please find the requested information attached.");

        FileSystemResource file = new FileSystemResource(path.toFile());
        helper.addAttachment(file.getFilename(), file);

        emailSender.send(message);
    }
}
