package com.gianfcop.ss.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class GatewayAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
    private ServerRedirectStrategy redirectStrategy = new DefaultServerRedirectStrategy();
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        URI url = URI.create("/prenotazioni/new");
        return this.redirectStrategy.sendRedirect(webFilterExchange.getExchange(), url);
    }
}
