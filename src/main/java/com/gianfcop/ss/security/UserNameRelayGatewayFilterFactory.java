package com.gianfcop.ss.security;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;


@Component
@Slf4j
public class UserNameRelayGatewayFilterFactory  extends AbstractGatewayFilterFactory<UserNameRelayGatewayFilterFactory .Config>{
    
    

    public UserNameRelayGatewayFilterFactory () {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> exchange.getPrincipal().cast(OAuth2AuthenticationToken.class)
                .map(bearerTokenAuthentication -> {
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    //String userName = (String) bearerTokenAuthentication.getPrincipal().getAttribute("preferred_username");
                    String userName = (String) bearerTokenAuthentication.getPrincipal().getAttribute("sub");
                    String name = (String)bearerTokenAuthentication.getPrincipal().getAttribute("name");
                    log.info("USERNAME CAPTURED: {}", userName);
                    withCustomHeader(exchange, userName, "username_header");
                    withCustomHeader(exchange, name, "name_header");
                    return exchange;
                }).defaultIfEmpty(exchange).flatMap(chain::filter);
    }

    private ServerWebExchange withCustomHeader(ServerWebExchange exchange, String userName, String header) {
        return exchange.mutate()
                .request(r -> r.headers(headers -> headers.add(header, userName))).build();
    }

    public static class Config{
        
    }

    


    
}
