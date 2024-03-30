package com.redartis.expense.config;

import com.redartis.expense.config.filter.AdminPageFilter;
import com.redartis.expense.config.filter.InternalKeyAuthenticationFilter;
import com.redartis.expense.config.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AdminPageFilter adminPageFilter;
    private final InternalKeyAuthenticationFilter internalKeyAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(
                                        "/templates/**", "/scripts/**", "/css/**",
                                        "/auth/**",
                                        "/login/**",
                                        "/error",
                                        "/actuator/health", "/actuator/prometheus"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .formLogin(httpFormLogin -> httpFormLogin.loginPage("/login"))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAfter(
                        internalKeyAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterAfter(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(adminPageFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
