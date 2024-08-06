package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("chat-service", r -> r.path("/chat/**")
                        .filters(f -> f.rewritePath("/chat/(?<segment>.*)", "/api/${segment}"))
                        .uri("http://localhost:8085"))
                .route("history-service", r -> r.path("/history/**")
                        .filters(f -> f.rewritePath("/history/(?<segment>.*)", "/api/${segment}"))
                        .uri("http://localhost:8084"))
                .route("recommendation-service", r -> r.path("/recommendation/**")
                        .filters(f -> f.rewritePath("/recommendation/(?<segment>.*)", "/api/${segment}"))
                        .uri("http://localhost:8083"))
                .route("subscription-service", r -> r.path("/subscription/**")
                        .filters(f -> f.rewritePath("/subscription/(?<segment>.*)", "/api/${segment}"))
                        .uri("http://localhost:8082"))
                .route("user-service", r -> r.path("/user/**")
                        .filters(f -> f.rewritePath("/user/(?<segment>.*)", "/api/${segment}"))
                        .uri("http://localhost:8081"))
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000"); // Allow all origins
        corsConfiguration.addAllowedMethod("*"); // Allow all HTTP methods
        corsConfiguration.addAllowedHeader("*"); // Allow all headers
        corsConfiguration.setAllowCredentials(true); // Allow credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(source);
    }
}
