package com.itiviti.supportrobot.domain;

public class User
{
    private String login;
    private String emailAddress;

    public User()
    {
    }

    public User(String login, String emailAddress)
    {
        this.login = login;
        this.emailAddress = emailAddress;
    }

    public String getLogin()
    {
        return login;
    }

    public String getEmailAddress()
    {
        return emailAddress;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }
}
