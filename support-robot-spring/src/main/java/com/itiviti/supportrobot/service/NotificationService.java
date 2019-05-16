package com.itiviti.supportrobot.service;

import java.io.File;
import java.nio.file.Path;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.itiviti.supportrobot.domain.User;

@Service
public class NotificationService
{
    private JavaMailSender emailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender)
    {
        this.emailSender = javaMailSender;
    }

    public void sendNotification(User user)
    {
        // send email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmailAddress());
        mailMessage.setFrom("support@itiviti.com");
        mailMessage.setSubject("Your Support Request");
        mailMessage.setText("Here's your request:...");

        emailSender.send(mailMessage);
    }

    public void sendReplyWithAttachment(User user, Path path) throws MessagingException
    {
        // send email
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("support@itiviti.com");
        helper.setTo(user.getEmailAddress());
        helper.setSubject("Your Support Request");
        helper.setText("Please find the requested information attached.");

        FileSystemResource file = new FileSystemResource(path.toFile());
        helper.addAttachment(file.getFilename(), file);

        emailSender.send(message);
    }
}
