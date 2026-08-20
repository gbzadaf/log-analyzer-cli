package com.gabrielf.loganalyzer;

import com.gabrielf.loganalyzer.export.CsvExporter;
import com.gabrielf.loganalyzer.export.JsonExporter;
import com.gabrielf.loganalyzer.model.LogEntry;
import com.gabrielf.loganalyzer.model.LogReport;
import com.gabrielf.loganalyzer.parser.LogParser;
import com.gabrielf.loganalyzer.report.ReportGenerator;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Stream;


@CommandLine.Command(
        name = "log-analyzer",
        mixinStandardHelpOptions = true,
        version = "log-analyzer 1.0",
        description = "Analisa arquivos de log no formato combined (Apache/Nginx) e gera estatisticas."
)
public class LogAnalyzerCommand implements Callable<Integer> {

    @CommandLine.Parameters(
            index = "0",
            description = "Caminho do arquivo de log a ser analisado."
    )
    private Path logFilePath;

    @CommandLine.Option(
            names = {"-v", "--verbose"},
            description = "Exibe detalhes adicionais durante o processamento."
    )
    private boolean verbose;

    @CommandLine.Option(
            names = {"-e", "--export"},
            description = "Formato de exportacao do relatorio: csv ou json."
    )
    private String exportFormat;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "Caminho do arquivo de saida (usado junto com --export)."
    )
    private Path outputPath;


    @Override
    public Integer call() throws Exception {
        if (!Files.exists(logFilePath)) {
            System.err.println("Erro: arquivo nao encontrado: " + logFilePath);
            return 1;
        }

        LogParser parser = new LogParser();
        ReportGenerator reportGenerator = new ReportGenerator();

        List<LogEntry> entries = readAndParseLogFile(logFilePath, parser);

        if (verbose) {
            System.out.println("Linhas parseadas com sucesso: " + entries.size());
        }

        if (entries.isEmpty()) {
            System.out.println("Nenhuma entrada valida encontrada no arquivo.");
            return 0;
        }

        LogReport report = reportGenerator.generate(entries);
        printReport(report);

        if (exportFormat != null) {
            return handleExport(report);
        }

        return 0;

    }

    private Integer handleExport(LogReport report) {
        if (outputPath == null) {
            System.err.println("Erro: --output e obrigatorio quando --export e usado.");
            return 1;
        }

        try {
            switch (exportFormat.toLowerCase()) {
                case "csv" -> new CsvExporter().export(report, outputPath);
                case "json" -> new JsonExporter().export(report, outputPath);
                default -> {
                    System.err.println("Erro: formato de exportacao invalido: " + exportFormat + " (use csv ou json)");
                    return 1;
                }
            }
            System.out.println("Relatorio exportado para: " + outputPath);
            return 0;
        } catch (IOException e) {
            System.err.println("Erro ao exportar relatorio: " + e.getMessage());
            return 1;
        }

    }

    private List<LogEntry> readAndParseLogFile(Path path, LogParser parser) {
        try (Stream<String> lines = Files.lines(path)) {
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

    private void printReport(LogReport report) {
        System.out.println("\n=== RELATORIO DE LOG ===");
        System.out.println("Total de requisicoes: " + report.totalRequests());

        System.out.println("\nTop IPs por frequencia:");
        for (Map.Entry<String, Long> entry : report.requestsByIp().entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue() + " requisicoes");
        }

        System.out.println("\nRequisicoes por status code:");
        for (Map.Entry<Integer, Long> entry : report.requestsByStatusCode().entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

    }

}
