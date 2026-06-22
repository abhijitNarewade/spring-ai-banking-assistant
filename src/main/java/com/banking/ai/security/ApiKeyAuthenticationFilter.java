package com.banking.ai.security;

import com.banking.ai.config.BankingAiProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";
    private static final Set<String> PUBLIC_PREFIXES = Set.of("/actuator/health", "/swagger-ui", "/v3/api-docs");

    private final BankingAiProperties properties;

    public ApiKeyAuthenticationFilter(BankingAiProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (PUBLIC_PREFIXES.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        String suppliedKey = request.getHeader(API_KEY_HEADER);
        if (!properties.getSecurity().getApiKey().equals(suppliedKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "ApiKey");
            response.getWriter().write("{\"error\":\"Missing or invalid API key\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
