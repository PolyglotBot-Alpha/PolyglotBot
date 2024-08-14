package com.example.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class GatewayApplication {

    @Value("${chat.service.uri}")
    private String chatServiceUri;

    @Value("${history.service.uri}")
    private String historyServiceUri;

    @Value("${recommendation.service.uri}")
    private String recommendationServiceUri;

    @Value("${subscription.service.uri}")
    private String subscriptionServiceUri;

    @Value("${user.service.uri}")
    private String userServiceUri;

    @Value("${cors.allowed.origins}")
    private String[] allowedOrigins;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("chat-service", r -> r.path("/chat/**")
                        .filters(f -> f.rewritePath("/chat/(?<segment>.*)", "/api/${segment}"))
                        .uri(chatServiceUri))
                .route("history-service", r -> r.path("/history/**")
                        .filters(f -> f.rewritePath("/history/(?<segment>.*)", "/api/${segment}"))
                        .uri(historyServiceUri))
                .route("recommendation-service", r -> r.path("/recommendation/**")
                        .filters(f -> f.rewritePath("/recommendation/(?<segment>.*)", "/api/${segment}"))
                        .uri(recommendationServiceUri))
                .route("subscription-service", r -> r.path("/subscription/**")
                        .filters(f -> f.rewritePath("/subscription/(?<segment>.*)", "/api/${segment}"))
                        .uri(subscriptionServiceUri))
                .route("user-service", r -> r.path("/user/**")
                        .filters(f -> f.rewritePath("/user/(?<segment>.*)", "/api/${segment}"))
                        .uri(userServiceUri))
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();


        // Allow all origins
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        corsConfiguration.addAllowedMethod("*"); // Allow all HTTP methods
        corsConfiguration.addAllowedHeader("*"); // Allow all headers
        corsConfiguration.setAllowCredentials(true); // Allow credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(source);
    }
}
