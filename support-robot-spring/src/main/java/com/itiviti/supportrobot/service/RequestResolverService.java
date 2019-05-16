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
import java.util.stream.Collectors;
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

        logger.fine("Paths are: " + paths);
        return paths;
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
