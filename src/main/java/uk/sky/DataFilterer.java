package uk.sky;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.Long.parseLong;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.averagingLong;
import static java.util.stream.Collectors.toList;

public class DataFilterer {
    public static Collection<?> filterByCountry(Reader source, String country) {
        return getLogExtractsFrom(source).stream()
            .filter(logExtract -> isCountryCodeMatches(country, logExtract))
                .collect(toList());
    }

    public static Collection<?> filterByCountryWithResponseTimeAboveLimit(Reader source, String country, long limit) {
        return getLogExtractsFrom(source).stream()
            .filter(logExtract -> isCountryCodeMatches(country, logExtract))
            .filter(logExtract -> logExtract.getResponseTime() > limit)
                .collect(toList());
    }

    public static Collection<?> filterByResponseTimeAboveAverage(Reader source) {
        List<LogExtract> logExtracts = getLogExtractsFrom(source);
        if (logExtracts.isEmpty()) {
            return emptyList();
        }
        return logExtracts.stream()
                .filter(logExtract -> logExtract.getResponseTime() > averageResponseTime(logExtracts))
                .collect(toList());
    }

    private static List<LogExtract> getLogExtractsFrom(Reader source) {
        List<LogExtract> logExtracts = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(source)) { // used try-with-resources
            bufferedReader.readLine(); // skip the first line, which is the header
            String line;
            while (null != (line = bufferedReader.readLine())) {
                String[] data = line.split(",");
                logExtracts.add(new LogExtract(parseLong(data[0]), data[1], parseLong(data[2])));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return logExtracts;
    }

    private static boolean isCountryCodeMatches(String country, LogExtract logExtract) {
        return logExtract.getCountryCode().equals(country);
    }

    private static Double averageResponseTime(List<LogExtract> logExtracts) {
        return logExtracts.stream()
                .collect(averagingLong(LogExtract::getResponseTime));
    }
}