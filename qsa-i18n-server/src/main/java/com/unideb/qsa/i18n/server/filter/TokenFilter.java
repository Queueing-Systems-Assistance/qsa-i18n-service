package com.unideb.qsa.i18n.server.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.unideb.qsa.i18n.domain.exception.QSAInvalidTokenException;

/**
 * Filter for validating token in the header.
 */
@Profile(value = "lab")
@Order(3)
@Component
public class TokenFilter extends OncePerRequestFilter {

    private static final String ERROR_NOT_VALID_TOKEN = "Given [%s] token is not valid!";
    private static final String TOKEN_ID = "X-QSA-Token";
    private static final String ENDPOINT = "/keys/update";
    private static final Object NO_HANDLER = null;
    private final HandlerExceptionResolver exceptionResolver;

    @Value("${qsa.token}")
    private String token;

    public TokenFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (shouldValidateEndpoint(request)) {
            validateToken(request, response, filterChain);
        } else {
            super.doFilter(request, response, filterChain);
        }
    }

    private void validateToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (Optional.ofNullable(request.getHeader(TOKEN_ID)).filter(token -> token.equals(this.token)).isPresent()) {
            super.doFilter(request, response, filterChain);
        } else {
            exceptionResolver.resolveException(request, response, NO_HANDLER, new QSAInvalidTokenException(String.format(ERROR_NOT_VALID_TOKEN, TOKEN_ID)));
        }
    }

    private boolean shouldValidateEndpoint(HttpServletRequest request) {
        return request.getRequestURI().contains(ENDPOINT);
    }
}
