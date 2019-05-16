package com.itiviti.supportrobot.controller;

import java.nio.file.Path;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itiviti.supportrobot.domain.User;
import com.itiviti.supportrobot.service.NotificationService;
import com.itiviti.supportrobot.service.RequestResolverException;
import com.itiviti.supportrobot.service.RequestResolverService;

@RestController
public class SupportRobotController
{
    @Autowired
    private RequestResolverService requestResolverService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/request-logs")
    public String requestLogs()
    {
        // create user
        User user = new User("dfilimon", "dfilimon@itiviti.com");

        // send an email
        String status = "The session info has been emailed successfully.";
        try
        {
            Path path = requestResolverService.getAttachment("20190508", "", "SOCGENCRD");
            notificationService.sendReplyWithAttachment(user, path);
        }
        catch (RequestResolverException e)
        {
            status = "The logs could not be retrieved.";
            e.printStackTrace();
        }
        catch (MessagingException e)
        {
            status = "The email could not be sent.";
            e.printStackTrace();
        }
        return status;
    }

    @RequestMapping("/request-session-info")
    public String requestSessionInfo()
    {
        return "The requested information has been emailed to you.";
    }
}
