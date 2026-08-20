package com.gabrielf.loganalyzer.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gabrielf.loganalyzer.model.LogReport;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Exporta um LogReport para o formato JSON
 */
public class JsonExporter {

    private final ObjectMapper objectMapper;

    public JsonExporter() {
        this.objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void export(LogReport report, Path outputPath) throws IOException {
        objectMapper.writeValue(outputPath.toFile(), report);

    }

}
