package com.atlas.filter;

import java.io.ObjectInputFilter.Config;

import org.springframework.http.HttpStatus;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
    
    public JwtAuthenticationFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            System.out.println("GATEWAY FILTER HIT!"); // <--- Add this
            // 1. Get the "Authorization" header from the incoming request
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            
            // 2. Check if the header exists
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // 3. Valdate the Token here (Optional)
            String token = authHeader.substring(7);

            // If validation fails, return 410 unauthorized
            // Otherwise, continue to the microservice

            return chain.filter(exchange);

        };
    }

    public static class Config {}
}
