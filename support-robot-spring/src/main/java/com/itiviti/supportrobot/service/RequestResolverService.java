package com.itiviti.supportrobot.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RequestResolverService
{
    public Path getAttachment(String date, String time, String targetCompId) throws RequestResolverException
    {
        Path path = Paths.get("logs-" + date + ".txt");
        try
        {
            List<String> logs = Files.lines(getPath(date))
                .filter(getLinesForTime(date, time))
                .filter(getLinesForTargetCompId(targetCompId))
                .filter(getTradingMessages())
                .collect(Collectors.toList());
            Files.write(path, logs);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RequestResolverException();
        }
        return path;
    }

    public Path getSessionInfo(String targetCompId) throws RequestResolverException
    {
        Path path = Paths.get(targetCompId + ".ini");
        try
        {
            Files.readAllLines(path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RequestResolverException("Could not access ini file.");
        }
        return path;
    }

    private Predicate<String> getLinesForTime(String date, String time)
    {
        return line -> line.matches(getISODate(date) + " " + time + ".*");
    }

    private String getISODate(String date)
    {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd")).format(DateTimeFormatter.ISO_LOCAL_DATE);
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
