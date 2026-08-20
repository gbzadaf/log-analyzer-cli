package com.gabrielf.loganalyzer.report;

import com.gabrielf.loganalyzer.model.LogEntry;
import com.gabrielf.loganalyzer.model.LogReport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportGeneratorTest {

    private final ReportGenerator generator = new ReportGenerator();

    private LogEntry entry(String ip, int statusCode) {
        return new LogEntry(ip, LocalDateTime.now(), "GET", "/test", statusCode, 100);

    }

    @Test
    @DisplayName("Deve contar o total de requisicoes corretamente")
    void deveContarTotalDeRequisicoes() {
        List<LogEntry> entries = List.of(
                entry("127.0.0.1", 200),
                entry("127.0.0.1", 200),
                entry("192.168.0.1", 404)
        );

        LogReport report = generator.generate(entries);

        assertEquals(3, report.totalRequests());

    }

    @Test
    @DisplayName("Deve agrupar corretamente por status code")
    void deveAgruparPorStatusCode() {
        List<LogEntry> entries = List.of(
                entry("127.0.0.1", 200),
                entry("127.0.0.1", 200),
                entry("192.168.0.1", 404),
                entry("192.168.0.1", 500)
        );

        LogReport report = generator.generate(entries);
        Map<Integer, Long> byStatus = report.requestsByStatusCode();

        assertEquals(2L, byStatus.get(200));
        assertEquals(1L, byStatus.get(404));
        assertEquals(1L, byStatus.get(500));

    }

    @Test
    @DisplayName("Deve retornar os top IPs ordenados do mais frequente para o menos frequente")
    void deveOrdenarTopIpsPorFrequencia() {
        List<LogEntry> entries = List.of(
                entry("1.1.1.1", 200),
                entry("2.2.2.2", 200),
                entry("2.2.2.2", 200),
                entry("3.3.3.3", 200),
                entry("3.3.3.3", 200),
                entry("3.3.3.3", 200)
        );

        LogReport report = generator.generate(entries);
        List<String> ipsEmOrdem = new ArrayList<>(report.requestsByIp().keySet());

        assertEquals("3.3.3.3", ipsEmOrdem.get(0)); // 3 requisições - mais frequente
        assertEquals("2.2.2.2", ipsEmOrdem.get(1)); // 2 requisições
        assertEquals("1.1.1.1", ipsEmOrdem.get(2)); // 1 requisição - menos frequente

    }

    @Test
    @DisplayName("Deve limitar o top de IPs a no maximo 5, mesmo com mais IPs distintos")
    void deveLimitarTopIpsACinco() {
        List<LogEntry> entries = List.of(
                entry("1.1.1.1", 200),
                entry("2.2.2.2", 200),
                entry("3.3.3.3", 200),
                entry("4.4.4.4", 200),
                entry("5.5.5.5", 200),
                entry("6.6.6.6", 200),
                entry("7.7.7.7", 200)
        );

        LogReport report = generator.generate(entries);

        assertEquals(5, report.requestsByIp().size());

    }

    @Test
    @DisplayName("Deve lidar com lista vazia sem lancar excecao")
    void deveLidarComListaVazia() {
        LogReport report = generator.generate(List.of());

        assertEquals(0, report.totalRequests());
        assertTrue(report.requestsByIp().isEmpty());
        assertTrue(report.requestsByStatusCode().isEmpty());

    }


}
