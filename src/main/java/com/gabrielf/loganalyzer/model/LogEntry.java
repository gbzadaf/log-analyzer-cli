package com.gabrielf.loganalyzer.model;

import java.time.LocalDateTime;

/**
 * Representa uma única entrada de log HTTP já parseada
 * (formato combined log do Apache/Nginx).
 */
public record LogEntry(
        String ipAddress,
        LocalDateTime timestamp,
        String httpMethod,
        String url,
        int statusCode,
        long responseSize

) {
}
