package com.gabrielf.loganalyzer.parser;

import com.gabrielf.loganalyzer.model.LogEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogParserTest {

    private final LogParser parser = new LogParser();


    @Test
    @DisplayName("Deve parsear corretamente uma linha de log válida")
    void deveParsearLinhaValida() {
        String linha = "127.0.0.1 - - [10/Oct/2023:13:55:36 +0000] \"GET /index.html HTTP/1.1\" 200 2326";

        Optional<LogEntry> resultado = parser.parse(linha);

        assertTrue(resultado.isPresent());

        LogEntry entry = resultado.get();
        assertEquals("127.0.0.1", entry.ipAddress());
        assertEquals("GET", entry.httpMethod());
        assertEquals("/index.html", entry.url());
        assertEquals(200, entry.statusCode());
        assertEquals(2326L, entry.responseSize());
        assertEquals(LocalDateTime.of(2023, 10, 10, 13, 55, 36), entry.timestamp());

    }

    @Test
    @DisplayName("Deve retornar Optional vazio para linha em formato inválido")
    void deveRetornarVazioParaLinhaInvalida() {
        String linhaQuebrada = "isso nao e um log valido";

        Optional<LogEntry> resultado = parser.parse(linhaQuebrada);

        assertTrue(resultado.isEmpty());

    }

    @Test
    @DisplayName("Deve tratar responseSize '-' como zero")
    void deveTratarTracoComoZero() {
        String linha = "10.0.0.5 - - [01/Jan/2024:00:00:00 +0000] \"POST /login HTTP/1.1\" 401 -";

        Optional<LogEntry> resultado = parser.parse(linha);

        assertTrue(resultado.isPresent());
        assertEquals(0L, resultado.get().responseSize());

    }

    @Test
    @DisplayName("Deve retornar vazio quando a linha está incompleta")
    void deveRetornarVazioParaLinhaIncompleta() {
        String linhaIncompleta = "127.0.0.1 - - [10/Oct/2023:13:55:36 +0000] \"GET /index.html HTTP/1.1\" 200";

        Optional<LogEntry> resultado = parser.parse(linhaIncompleta);

        assertTrue(resultado.isEmpty());

    }

    @Test
    @DisplayName("Deve parsear corretamente diferentes métodos HTTP e status codes")
    void deveParsearDiferentesMetodosEStatus() {
        String linha = "192.168.0.10 - - [15/Mar/2024:09:30:00 +0000] \"DELETE /api/users/42 HTTP/1.1\" 204 0";

        Optional<LogEntry> resultado = parser.parse(linha);

        assertTrue(resultado.isPresent());
        assertEquals("DELETE", resultado.get().httpMethod());
        assertEquals(204, resultado.get().statusCode());
    }

}
