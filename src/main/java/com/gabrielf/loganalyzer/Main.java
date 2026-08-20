package com.gabrielf.loganalyzer;

import com.gabrielf.loganalyzer.model.LogEntry;
import com.gabrielf.loganalyzer.parser.LogParser;

import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        String sampleLine = "127.0.0.1 - - [10/Oct/2023:13:55:36 +0000] \"GET /index.html HTTP/1.1\" 200 2326";

        LogParser parser = new LogParser();
        Optional<LogEntry> result = parser.parse(sampleLine);

        if (result.isPresent()) {
            System.out.println("Parse bem-sucedido!");
            System.out.println(result.get());
        } else {
            System.out.println("Falha ao parsear a linha.");
        }
    }

}
