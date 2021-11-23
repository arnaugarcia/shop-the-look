package com.klai.stl.config;

import static java.util.List.of;
import static org.springframework.web.cors.CorsUtils.isPreFlightRequest;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class ClientCorsFilter extends OncePerRequestFilter {

    private final CorsProcessor processor = new DefaultCorsProcessor();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        config.setAllowedOriginPatterns(of("*"));

        config.setAllowedMethods(of("GET", "OPTIONS"));

        config.addAllowedHeader("STL-Token");

        CorsConfiguration corsConfiguration = source.getCorsConfiguration(request);

        boolean isValid = this.processor.processRequest(corsConfiguration, request, response);

        if (!isValid || isPreFlightRequest(request)) {
            return;
        }

        filterChain.doFilter(request, response);
    }
}
