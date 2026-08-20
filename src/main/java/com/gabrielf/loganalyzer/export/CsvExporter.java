package com.gabrielf.loganalyzer.export;

import com.gabrielf.loganalyzer.model.LogReport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Exporta um LogReport para o formato CSV
 */
public class CsvExporter {

    public void export(LogReport report, Path outputPath) throws IOException {
        StringBuilder csv = new StringBuilder();

        csv.append("secao,chave,valor\n");
        csv.append("total,total_requisicoes,").append(report.totalRequests()).append("\n");

        for (Map.Entry<String, Long> entry : report.requestsByIp().entrySet()) {
            csv.append("top_ip,")
                    .append(entry.getKey())
                    .append(",")
                    .append(entry.getValue())
                    .append("\n");
        }

        for (Map.Entry<Integer, Long> entry : report.requestsByStatusCode().entrySet()) {
            csv.append("status_code,")
                    .append(entry.getKey())
                    .append(",")
                    .append(entry.getValue())
                    .append("\n");
        }

        Files.writeString(outputPath, csv.toString());

    }

}
