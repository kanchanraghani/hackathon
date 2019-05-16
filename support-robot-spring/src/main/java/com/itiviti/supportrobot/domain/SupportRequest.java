package com.itiviti.supportrobot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SupportRequest
{
    private int requestType;
    private String startDate;
    private String endDate;
    private String[] msgTypes;
    private String fixSession;

    public SupportRequest() {}

    public int getRequestType()
    {
        return requestType;
    }

    public void setRequestType(int requestType)
    {
        this.requestType = requestType;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }

    public String[] getMsgTypes()
    {
        return msgTypes;
    }

    public void setMsgTypes(String[] msgTypes)
    {
        this.msgTypes = msgTypes;
    }

    public String getFixSession()
    {
        return fixSession;
    }

    public void setFixSession(String fixSession)
    {
        this.fixSession = fixSession;
    }

}
