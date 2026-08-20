package com.gabrielf.loganalyzer.report;

import com.gabrielf.loganalyzer.model.LogEntry;
import com.gabrielf.loganalyzer.model.LogReport;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gera estatísticas agregadas a partir de uma lista de LogEntry.
 */
public class ReportGenerator {

    private static final int TOP_IPS_LIMIT = 5;

    public LogReport generate(List<LogEntry> entries) {
        long total = entries.size();

        Map<String, Long> requestsByIp = countTopIpsByFrequency(entries);
        Map<Integer, Long> requestsByStatusCode = countByStatusCode(entries);

        return new LogReport(total, requestsByIp, requestsByStatusCode);

    }

    private Map<String, Long> countTopIpsByFrequency(List<LogEntry> entries) {
        return entries.stream()
                .collect(Collectors.groupingBy(LogEntry::ipAddress, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(TOP_IPS_LIMIT)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new // preserva a ordem de maior pra menor
                ));

    }

    private Map<Integer, Long> countByStatusCode(List<LogEntry> entries) {
        return entries.stream()
                .collect(Collectors.groupingBy(LogEntry::statusCode, Collectors.counting()));

    }


}
