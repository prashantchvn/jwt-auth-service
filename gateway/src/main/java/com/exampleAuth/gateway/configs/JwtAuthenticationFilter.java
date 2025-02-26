package com.exampleAuth.gateway.configs;

import com.exampleAuth.gateway.service.CustomUserDetailsService;
import com.exampleAuth.gateway.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config){
        return ((exchange, chain) -> {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);
            String username = jwtUtils.extractUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(!jwtUtils.validateToken(token, userDetails)){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            exchange.getRequest().mutate().header("X-Authenticated-User", username).build();

            return chain.filter(exchange);

        });
    }

    public JwtAuthenticationFilter(Class<Config> configClass) {
        super(configClass);
        // TODO Auto-generated constructor stub
    }

    public static class Config {

    }
}
