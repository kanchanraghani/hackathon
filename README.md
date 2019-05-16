Support Robot:

- The idea is to develop a support robot that will automatically reply to the standard client queries received for the MFS platform.
- Currently, there are lot of standard L1 queries received that are replied manually by the L1 team.


Ideally the L1 responses to the standard queries should be automated as it saves time & effort for the organization and gives additional bandwidth to focus on other responsibilities. Also, nowadays, automatic replies is a new trend that is being adapted in most of the IT streams.

Some of standard queries received from the clients - for example below:
  1) Client requesting the logs for a particular session for a given time or tag value pair.
  2) Client requesting session information - for example session start/stop/reset time that is currently setup in production.
  3) Publishing news etc.
  4) FIX Session Disconnection reason.

Responses to these queries can be easily generated using the FIX session logs and FIX session configurations which will be Input to the Support Robot.

In Ship it, we will be developing a POC demonstrating the vision of having the Support Robot.
This Support Robot should integrate or developed as part of the MFS Portal Webapp because Webapp as of now already has the FIX logs of the sessions in the database, along with the session information, client information and clients are already using the Webapp to submit a limit type of Support request.

If the support robot is integrated in the webapp, the L1 workflow will be improved to the below:
Client -> submit request to Webapp -> webapp replies.

Please note: Queries that are submitted outside of the Webapp will not be replied by the Support Robot.
