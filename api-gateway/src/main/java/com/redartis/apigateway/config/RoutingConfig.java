package com.redartis.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RoutingConfig {
    private final AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator routeLocatorConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p.path("/auth/**")
                        .filters(f -> f.filter(authenticationFilter))
                        .uri("lb://AUTHENTICATION-SERVICE"))
                .route(p -> p.path("/tracker/**")
                        .filters(f -> f.rewritePath("/tracker/(?<segment>.*)", "/${segment}")
                                .filter(authenticationFilter)
                        ).uri("lb://EXPENSE-SERVICE"))
                .route(p -> p.path("/telegram/**")
                        .filters(f -> f.rewritePath("/telegram/(?<segment>.*)", "/${segment}")
                                .filter(authenticationFilter)
                        ).uri("lb://TELEGRAM-BOT-SERVICE"))
                .build();
    }
}
