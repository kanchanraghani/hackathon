package com.itiviti.supportrobot.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itiviti.supportrobot.domain.SupportRequest;
import com.itiviti.supportrobot.service.EmailService;
import com.itiviti.supportrobot.service.RequestResolverException;
import com.itiviti.supportrobot.service.RequestResolverService;

@RestController
public class SupportRobotController
{
    private String email = "dfilimon@itiviti.com";

    @Autowired
    private RequestResolverService requestResolverService;

    @Autowired
    private EmailService emailService;

    @RequestMapping("/submitrequest")
    public String process(@RequestBody SupportRequest supportRequest)
    {
        switch (supportRequest.getRequestType())
        {
            case 1:
                return logsRequest(supportRequest);
            case 2:
                return disconnectReasonRequest(supportRequest);
            case 3:
                return sessionDetailsRequest(supportRequest);
        }
        return "We could not process your request.";
    }

    private String logsRequest(SupportRequest supportRequest)
    {
        String status = "The logs have been emailed successfully.";
        try
        {
            Path path = requestResolverService.getLogs(supportRequest.getStartDate(), supportRequest.getEndDate(), supportRequest.getFixSession(), supportRequest.getMsgTypes());
            emailService.sendReplyWithAttachment(email, path, "Your logs request", "Please find the request information attached.");
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

    private String disconnectReasonRequest(SupportRequest supportRequest)
    {
        String reason = "The disconnection reason could not be resolved.";
        try
        {
            reason = requestResolverService.getDisconnectionReason(supportRequest.getFixSession(), supportRequest.getStartDate(), supportRequest.getEndDate());
        }
        catch (RequestResolverException e)
        {
            e.printStackTrace();
        }
        return reason;
    }

    private String sessionDetailsRequest(SupportRequest supportRequest)
    {
        String status = "The session info has been emailed successfully.";
        try
        {
            String sessionInfo = requestResolverService.getSessionInfo(supportRequest.getFixSession());
            emailService.sendReply(email, sessionInfo, "Your session info request", sessionInfo);
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
        return status;
    }
}
