package com.klai.stl.config;

import static com.klai.stl.security.AuthoritiesConstants.ADMIN;
import static com.klai.stl.security.AuthoritiesConstants.MANAGER;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.klai.stl.security.jwt.JWTConfigurer;
import com.klai.stl.security.jwt.TokenProvider;
import com.klai.stl.security.token.ClientTokenConfigurer;
import com.klai.stl.security.token.ClientTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import tech.jhipster.config.JHipsterProperties;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration {

    @Configuration
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private final JHipsterProperties jHipsterProperties;

        private final TokenProvider tokenProvider;

        private final CorsFilter corsFilter;
        private final SecurityProblemSupport problemSupport;

        public ApiWebSecurityConfigurationAdapter(
            TokenProvider tokenProvider,
            CorsFilter corsFilter,
            JHipsterProperties jHipsterProperties,
            SecurityProblemSupport problemSupport
        ) {
            this.tokenProvider = tokenProvider;
            this.corsFilter = corsFilter;
            this.problemSupport = problemSupport;
            this.jHipsterProperties = jHipsterProperties;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        public void configure(WebSecurity web) {
            web
                .ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/i18n/**")
                .antMatchers("/content/**")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/test/**");
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .csrf()
                .disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .headers()
                .contentSecurityPolicy(jHipsterProperties.getSecurity().getContentSecurityPolicy())
                .and()
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                .and()
                .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; fullscreen 'self'; payment 'none'")
                .and()
                .frameOptions()
                .deny()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/activate").permitAll()
                .antMatchers("/api/account/reset-password/init").permitAll()
                .antMatchers("/api/account/reset-password/finish").permitAll()
                .antMatchers("/api/admin/**").hasAuthority(ADMIN)
                .antMatchers("/api/spaces").hasAuthority(ADMIN)
                .antMatchers("/api/spaces/**").authenticated()
                .antMatchers("/api/companies/**/billing").hasAnyAuthority(ADMIN, MANAGER)
                .antMatchers("/api/companies/**/subscriptions").hasAnyAuthority(ADMIN)
                .antMatchers("/api/preferences/**").hasAnyAuthority(ADMIN, MANAGER)
                .antMatchers("/api/employees/**").hasAnyAuthority(ADMIN, MANAGER)
                .antMatchers("/api/**").authenticated()
                .antMatchers("/management/health").permitAll()
                .antMatchers("/management/health/**").permitAll()
                .antMatchers("/management/info").permitAll()
                .antMatchers("/management/prometheus").permitAll()
                .antMatchers("/management/**").hasAuthority(ADMIN)
                .and()
                .httpBasic()
                .and()
                .apply(securityConfigurerAdapter());
            // @formatter:on
        }

        private JWTConfigurer securityConfigurerAdapter() {
            return new JWTConfigurer(tokenProvider);
        }
    }

    @Configuration
    @Order(HIGHEST_PRECEDENCE)
    public static class ClientSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private final SecurityProblemSupport problemSupport;

        private final ClientTokenProvider clientTokenProvider;

        private final CorsFilter corsFilter;

        public ClientSecurityConfigurationAdapter(
            SecurityProblemSupport problemSupport,
            ClientTokenProvider clientTokenProvider,
            CorsFilter corsFilter
        ) {
            this.problemSupport = problemSupport;
            this.clientTokenProvider = clientTokenProvider;
            this.corsFilter = corsFilter;
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .csrf()
                .disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(problemSupport)
                .accessDeniedHandler(problemSupport)
                .and()
                .requestMatchers()
                .antMatchers("/client/api/**")
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .apply(clientSecurityConfigurerAdapter());
            // @formatter:on
        }

        private ClientTokenConfigurer clientSecurityConfigurerAdapter() {
            return new ClientTokenConfigurer(clientTokenProvider);
        }
    }
}
