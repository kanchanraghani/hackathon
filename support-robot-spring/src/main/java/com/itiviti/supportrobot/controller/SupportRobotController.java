package com.itiviti.supportrobot.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itiviti.supportrobot.service.NotificationService;
import com.itiviti.supportrobot.service.RequestResolverException;
import com.itiviti.supportrobot.service.RequestResolverService;

@RestController
public class SupportRobotController
{
    private String email = "dfilimon@itiviti.com";

    @Autowired
    private RequestResolverService requestResolverService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/request-logs")
    public String requestLogs()
    {
        String status = "The logs have been emailed successfully.";
        try
        {
            Path path = requestResolverService.getLogs("2019-05-08T18:00:00.000Z", "2019-05-08T18:00:00.000Z", "SOCGENCRD");
            notificationService.sendReplyWithAttachment(email, path, "Your session info request");
            Files.delete(path);
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
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return status;
    }

    @RequestMapping("/request-disconnection-reason")
    public String requestDisconnectionReason()
    {
        String status = "The disconnection reason has been emailed successfully.";
        return status;
    }

    @RequestMapping("/request-session-info")
    public String requestSessionInfo()
    {
        String status = "The session info has been emailed successfully.";
        try
        {
            Path path = requestResolverService.getSessionInfo("I_CLIENT_FIX44");
            notificationService.sendReplyWithAttachment(email, path, "Your logs request");
            Files.delete(path);
        }
        catch (RequestResolverException e)
        {
            status = "The session info could not be retrieved.";
            e.printStackTrace();
        }
        catch (MessagingException e)
        {
            status = "The email could not be sent.";
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return status;
    }
}
