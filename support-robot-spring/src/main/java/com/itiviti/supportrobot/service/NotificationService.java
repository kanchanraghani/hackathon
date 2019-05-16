package com.itiviti.supportrobot.service;

import java.nio.file.Path;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
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

    public void sendReplyWithAttachment(String email, Path path, String subject) throws MessagingException
    {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("support@itiviti.com");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText("Please find the requested information attached.");

        FileSystemResource file = new FileSystemResource(path.toFile());
        helper.addAttachment(file.getFilename(), file);

        emailSender.send(message);
    }
}
