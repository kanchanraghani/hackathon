package com.itiviti.supportrobot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itiviti.supportrobot.domain.User;
import com.itiviti.supportrobot.service.NotificationService;

@RestController
public class SupportRobotController
{
    @Autowired
    private NotificationService notificationService;

    @RequestMapping("/request-logs")
    public String requestLogs() {
        return "The logs have been emailed to you.";
    }

    @RequestMapping("/request-session-info")
    public String requestSessionInfo() {
        // create user
        User user = new User("dfilimon", "dfilimon@itiviti.com");

        // send an email
        try {
            notificationService.sendNotification(user);
        }
        catch (MailException e)
        {

        }
        return "We'll get you your session info";
    }
}
