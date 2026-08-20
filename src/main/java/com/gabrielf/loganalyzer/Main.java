package com.gabrielf.loganalyzer;

import com.gabrielf.loganalyzer.model.LogEntry;
import com.gabrielf.loganalyzer.model.LogReport;
import com.gabrielf.loganalyzer.parser.LogParser;
import com.gabrielf.loganalyzer.report.ReportGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class Main {

    // Caminho fixo por enquanto, trocar pelo Picocli no próximo passo
    private static final String LOG_FILE_PATH = "sample-logs/access.log";

    public static void main(String[] args) {
        LogParser parser = new LogParser();
        ReportGenerator reportGenerator = new ReportGenerator();

        List<LogEntry> entries = readAndParseLogFile(LOG_FILE_PATH, parser);

        System.out.println("Linhas parseadas com sucesso: " + entries.size());

        LogReport report = reportGenerator.generate(entries);

        printReport(report);
    }

    private static List<LogEntry> readAndParseLogFile(String path, LogParser parser) {
        try (Stream<String> lines = Files.lines(Path.of(path))) {
            return lines
                    .map(parser::parse)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de log: " + e.getMessage());
            return List.of();
        }
    }

    private static void printReport(LogReport report) {
        System.out.println("\n=== RELATÓRIO DE LOG ===");
        System.out.println("Total de requisições: " + report.totalRequests());

        System.out.println("\nTop IPs por frequência:");
        for (Map.Entry<String, Long> entry : report.requestsByIp().entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue() + " requisições");
        }

        System.out.println("\nRequisições por status code:");
        for (Map.Entry<Integer, Long> entry : report.requestsByStatusCode().entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }
    }

}
