package com.gabrielf.loganalyzer.parser;

import com.gabrielf.loganalyzer.model.LogEntry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Responsável por transformar uma linha de texto no formato
 * "combined log" (Apache/Nginx) em um objeto LogEntry.
 */
public class LogParser {

    // Regex que captura os grupos: IP, timestamp, metodo, URL, status, tamanho
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "^(\\S+) \\S+ \\S+ \\[([^\\]]+)\\] \"(\\S+) (\\S+) \\S+\" (\\d{3}) (\\d+|-)$"
    );

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);


    /**
     * Tenta parsear uma linha de log. Retorna Optional.empty() se a linha
     * não bater com o formato esperado (em vez de lançar exceção) —
     * assim conseguimos pular linhas inválidas sem quebrar o processamento
     * de um arquivo inteiro.
     */
    public Optional<LogEntry> parse(String line) {
        Matcher matcher = LOG_PATTERN.matcher(line);

        if (!matcher.matches()) {
            return Optional.empty();
        }

        try {
            String ip = matcher.group(1);
            LocalDateTime timestamp = LocalDateTime.parse(matcher.group(2), TIMESTAMP_FORMAT);
            String method = matcher.group(3);
            String url = matcher.group(4);
            int statusCode = Integer.parseInt(matcher.group(5));
            long size = matcher.group(6).equals("-") ? 0 : Long.parseLong(matcher.group(6));

            return Optional.of(new LogEntry(ip, timestamp, method, url, statusCode, size));

        } catch (Exception e) {
            // Linha bateu com o regex mas algum campo não converteu
            // (ex: data corrompida) — também trata como invalida
            return Optional.empty();
        }

    }

}
