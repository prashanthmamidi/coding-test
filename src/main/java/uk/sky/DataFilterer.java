package uk.sky;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;

public class DataFilterer {
    public static Collection<?> filterByCountry(Reader source, String country) {
        return getLogExtractsFrom(source).stream()
            .filter(logExtract -> isCountryCodeMatches(country, logExtract))
            .collect(Collectors.toList());
    }

    public static Collection<?> filterByCountryWithResponseTimeAboveLimit(Reader source, String country, long limit) {
        return getLogExtractsFrom(source).stream()
            .filter(logExtract -> isCountryCodeMatches(country, logExtract))
            .filter(logExtract -> logExtract.getResponseTime() > limit)
            .collect(Collectors.toList());
    }

    public static Collection<?> filterByResponseTimeAboveAverage(Reader source) {
        List<LogExtract> logExtracts = getLogExtractsFrom(source);
        double average = logExtracts.stream()
            .mapToLong(rt -> rt.getResponseTime())
            .average().getAsDouble();

        return logExtracts.stream()
            .filter(logExtract -> logExtract.getResponseTime() > average)
            .collect(Collectors.toList());

    }

    private static List<LogExtract> getLogExtractsFrom(Reader source) {
        List<LogExtract> logExtracts = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(source)) { // used try-with-resources to avoid closing file handles explicitly
            bufferedReader.readLine(); // skip the first line, which is the header
            String line;
            while (null != (line = bufferedReader.readLine())) {
                String[] split = line.split(",");
                logExtracts.add(new LogExtract(parseLong(split[0]), split[1], parseLong(split[2])));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return logExtracts;
    }

    private static boolean isCountryCodeMatches(String country, LogExtract logExtract) {
        return logExtract.getCountryCode().equals(country);
    }
}