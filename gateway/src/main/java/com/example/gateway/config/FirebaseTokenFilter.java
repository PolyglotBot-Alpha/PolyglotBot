package com.example.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class FirebaseTokenFilter implements GlobalFilter, Ordered {
    private static final Set<String> paths = new HashSet<>();
    private static final Logger logger = LoggerFactory.getLogger(FirebaseTokenFilter.class);

    static {
        paths.add("/user/users");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        logger.debug("Request path: {}", path);

        if (paths.contains(path)) {
            logger.debug("Path is in the whitelist, proceeding without authentication");
            return chain.filter(exchange);
        }

        HttpHeaders headers = exchange.getRequest().getHeaders();
        String authToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
        logger.debug("Authorization header: {}", authToken);

        if (authToken == null || !authToken.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        authToken = authToken.replace("Bearer ", "");
        logger.debug("Bearer token extracted: {}", authToken);

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(authToken);
            logger.debug("Token successfully verified. User ID: {}", decodedToken.getUid());
            exchange.getRequest().mutate().header("user-id", decodedToken.getUid()).build();
        } catch (Exception e) {
            logger.error("Token verification failed", e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
