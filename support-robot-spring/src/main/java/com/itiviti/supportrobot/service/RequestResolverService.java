package com.itiviti.supportrobot.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import org.springframework.stereotype.Service;

@Service
public class RequestResolverService
{
    private static Logger logger = Logger.getLogger("RequestResolverService");

    private static DateTimeFormatter logFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static String removeLogPrefix(String line)
    {
        return line.substring(line.indexOf(" : ") + 3);
    }

    public Path getLogs(String startDate, String endDate, String sessionName, String[] messageTypes) throws RequestResolverException
    {
        Path outputPath = Paths.get(sessionName + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + ".txt");
        logger.info("Output path is " + outputPath);
        try
        {
            List<Path> paths = getPaths(startDate, endDate);
            logger.info("Paths are: " + paths);
            List<String> fullLogs = getFullLogsBySessionName(sessionName, messageTypes, paths);
            fullLogs = fullLogs.stream().map(RequestResolverService::removeLogPrefix).collect(Collectors.toList());
            Files.write(outputPath, fullLogs);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RequestResolverException();
        }
        return outputPath;
    }

    private List<String> getFullLogsBySessionName(String sessionName, String[] messageTypes, List<Path> paths) throws IOException
    {
        List<String> fullLogs = new ArrayList<>();
        for (Path path : paths)
        {
            if (!Files.exists(path))
            {
                continue;
            }
            List<String> dayLogs = Files.lines(path)
                .filter(bySessionName(sessionName))
                .filter(byMessageTypes(messageTypesAsString(messageTypes)))
                .collect(Collectors.toList());
            fullLogs.addAll(dayLogs);
        }
        return fullLogs;
    }
    private List<String> getFullLogsByPluginName(String pluginName, List<Path> paths) throws IOException
    {
        List<String> fullLogs = new ArrayList<>();
        for (Path path : paths)
        {
            if (!Files.exists(path))
            {
                continue;
            }
            List<String> dayLogs = Files.lines(path)
                .filter(line -> line.contains(pluginName))
                .map(RequestResolverService::removeLogPrefix)
                .collect(Collectors.toList());
            fullLogs.addAll(dayLogs);
        }
        return fullLogs;
    }

    private List<Path> getPaths(String startDate, String endDate)
    {
        List<Path> paths = new ArrayList<>();
        LocalDate start = LocalDate.parse(startDate.substring(0, 10));
        LocalDate end = LocalDate.parse(endDate.substring(0, 10));

        do
        {
            paths.add(getPath(start.format(logFormat)));
            start = start.plusDays(1);
        }
        while (start.isBefore(end) || start.isEqual(end));

        return paths;
    }

    private Predicate<String> bySessionName(String sessionName)
    {
        String[] compIds = sessionName.split("~");
        return line -> line.contains(compIds[0]) && line.contains(compIds[1]);
    }

    private Predicate<String> byMessageTypes(String messageTypes)
    {

        return line -> line.matches(".*\\|35=[" + messageTypes + "]{1,2}\\|.*");
    }

    private String messageTypesAsString(String[] messageTypes)
    {
        StringBuilder msgs = new StringBuilder();
        for (String msg : messageTypes)
        {
            msgs.append(msg);
        }
        return msgs.toString();
    }

    private Path getPath(String date)
    {
        return Paths.get(date + ".txt");
    }

    public String getSessionInfo(String targetCompId) throws RequestResolverException
    {
        Path path = Paths.get(targetCompId + ".ini");
        try
        {
            Ini ini = new Ini(path.toFile());
            Preferences prefs = new IniPreferences(ini);
            return populateSessionInfo(prefs).toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RequestResolverException("Could not access ini file.");
        }
    }

    private StringBuilder populateSessionInfo(Preferences prefs)
    {
        StringBuilder result = new StringBuilder();
        result.append("SenderCompID: ").append(prefs.node("session").get("SenderCompID", "")).append(System.lineSeparator());
        result.append("TargetCompID: ").append(prefs.node("session").get("TargetCompID", "")).append(System.lineSeparator());
        result.append("Version: ").append(prefs.node("session").get("Version", "")).append(System.lineSeparator());
        result.append(System.lineSeparator());
        result.append("timers/doStart:").append(System.lineSeparator());
        result.append("trigger: ").append(prefs.node("timers/doStart").get("trigger", "")).append(System.lineSeparator());
        result.append("timezone: ").append(prefs.node("timers/doStart").get("timezone", "")).append(System.lineSeparator());
        result.append(System.lineSeparator()).append(System.lineSeparator());
        result.append("timers/doStopdoReset:").append(System.lineSeparator());
        result.append("trigger: ").append(prefs.node("timers/doStopdoReset").get("trigger", "")).append(System.lineSeparator());
        result.append("timezone: ").append(prefs.node("timers/doStopdoReset").get("timezone", "")).append(System.lineSeparator());
        return result;
    }

    public String getDisconnectionReason(String sessionName, String startDate, String endDate) throws RequestResolverException
    {
        List<Path> paths = getPaths(startDate, endDate);
        try
        {
            List<String> logs = getFullLogsBySessionName(sessionName, new String[]{"A-Z0-9"}, paths);    // we first get all log lines for these session details
            logger.info("" + Arrays.asList(logs.get(0).split(" ")));
            String pluginName = logs.get(0).split(" ")[3];                                         // then we get the 4th element in the line split by spaces (plugin name between[])
            pluginName = pluginName.substring(1, pluginName.length() - 1);                                // we remove the []
            logs = getFullLogsByPluginName(pluginName, paths);                                           // now we get all log lines that match the plugin name since the lines above only had FIX messages, not all ULBridge logs

            if (noResponse(logs))
                return "Disconnection Reason: We are trying to connect but no response is received.";

        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RequestResolverException("Could not get logs.");
        }
        return null;
    }

    private boolean noResponse(List<String> logs)
    {
        return logs.stream().anyMatch(line -> line.matches(".*Connection unsuccessful after .{1,2} attempts - stopping adapter"));
    }
}
