package com.itiviti.supportrobot.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @RequestMapping(value = "/submitrequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity process(@RequestBody SupportRequest supportRequest)
    {
        switch (supportRequest.getRequestType())
        {
            case 1:
                logsRequest(supportRequest);
                break;
            case 2:
                disconnectReasonRequest(supportRequest);
                break;
            case 3:
                sessionDetailsRequest(supportRequest);
                break;
        }
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    private void logsRequest(SupportRequest supportRequest)
    {
        try
        {
            String startDate = supportRequest.getStartDate() != null ? supportRequest.getStartDate() : LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            String endDate = supportRequest.getEndDate() != null ? supportRequest.getEndDate() : LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            Path path = requestResolverService.getLogs(startDate, endDate, supportRequest.getFixSession(), supportRequest.getMsgTypes());
            emailService.sendReplyWithAttachment(email, path, "Your logs request", "Please find the request information attached.");
            Files.delete(path);
        }
        catch (RequestResolverException | MessagingException | IOException e)
        {
            e.printStackTrace();
        }
    }

    private void disconnectReasonRequest(SupportRequest supportRequest)
    {
        String reason;
        try
        {
            String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            reason = requestResolverService.getDisconnectionReason(supportRequest.getFixSession(), date, date);
            if (reason == null)
            {
                emailService.sendReply("support@itiviti.com", supportRequest.getFixSession(), "Manual Disconnection Reason Request",
                    "Please check disconnection reason between " + supportRequest.getStartDate() + " and " + supportRequest.getEndDate());
            }
            else
            {
                emailService.sendReply(email, supportRequest.getFixSession(), "Session Disconnection Reason", reason);
            }
        }
        catch (RequestResolverException | MessagingException e)
        {
            e.printStackTrace();
        }
    }

    private void sessionDetailsRequest(SupportRequest supportRequest)
    {
        try
        {
            String sessionInfo = requestResolverService.getSessionInfo(supportRequest.getFixSession());
            emailService.sendReply(email, sessionInfo, "Your session info request", sessionInfo);
        }
        catch (RequestResolverException | MessagingException e)
        {
            e.printStackTrace();
        }
    }
}
