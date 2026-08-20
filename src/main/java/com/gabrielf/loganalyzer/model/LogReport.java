package com.gabrielf.loganalyzer.model;

import java.util.Map;

/**
 * Representa o resultado agregado da análise de um conjunto de logs.
 */
public record LogReport(
        long totalRequests,
        Map<String, Long> requestsByIp,
        Map<Integer, Long> requestsByStatusCode

) {
}
