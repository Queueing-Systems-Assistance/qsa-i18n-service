package com.unideb.qsa.i18n.server.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter for putting the application version into each request.
 */
@Component
@Order(1)
public class VersionFilter extends OncePerRequestFilter {

    private static final String VERSION = "version";

    private final BuildProperties buildProperties;

    public VersionFilter(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        MDC.put(VERSION, buildProperties.getVersion());
        try {
            super.doFilter(request, response, filterChain);
        } finally {
            MDC.remove(VERSION);
        }
    }
}
