package com.klai.stl.security.token;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class ClientTokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final ClientTokenProvider clientTokenProvider;

    public ClientTokenConfigurer(ClientTokenProvider clientTokenProvider) {
        this.clientTokenProvider = clientTokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) {
        ClientTokenFilter clientTokenFilter = new ClientTokenFilter(clientTokenProvider);
        http.addFilterBefore(clientTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
