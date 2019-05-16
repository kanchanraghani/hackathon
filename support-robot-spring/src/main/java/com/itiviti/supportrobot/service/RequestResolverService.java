package com.itiviti.supportrobot.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public Path getLogs(String startDate, String endDate, String sessionName) throws RequestResolverException
    {
        Path outputPath = Paths.get(sessionName + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + ".txt");
        try
        {
            List<Path> paths = getPaths(startDate, endDate);
            List<String> fullLogs = new ArrayList<>();
            for (Path path : paths)
            {
                List<String> dayLogs = Files.lines(path)
                    .filter(getLinesForTargetCompId(sessionName))
                    .filter(getTradingMessages())
                    .map(RequestResolverService::removeLogPrefix)
                    .collect(Collectors.toList());
                fullLogs.addAll(dayLogs);
            }
            Files.write(outputPath, fullLogs);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RequestResolverException();
        }
        return outputPath;
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

        logger.info("Paths are: " + paths);
        return paths;
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

    private static String removeLogPrefix(String line)
    {
        return line.substring(line.indexOf(" : ") + 3);
    }

    private Predicate<String> getLinesForTargetCompId(String targetCompId)
    {
        return line -> line.contains("56=" + targetCompId + "|");
    }

    private Path getPath(String date)
    {
        return Paths.get(date + ".txt");
    }

    private Predicate<String> getTradingMessages()
    {
        return line -> line.matches(".*\\|35=[DFG8]\\|.*");
    }
}
