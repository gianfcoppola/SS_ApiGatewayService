package com.gianfcop.ss.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, ServerLogoutSuccessHandler handler)
    {
        http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/index.html", "/images/**", "/prenotazioni/index", "prenotazioni/index.html").permitAll()
                .anyExchange()
                .authenticated()
                .and().oauth2Login()
                .and().logout().logoutSuccessHandler(handler)
                .and().oauth2ResourceServer().jwt();
        return http.build();
    }

    @Bean
    public ServerLogoutSuccessHandler keycloakLogoutSuccessHandler(ReactiveClientRegistrationRepository repository){
        OidcClientInitiatedServerLogoutSuccessHandler oidcLogoutSuccessHandler = new OidcClientInitiatedServerLogoutSuccessHandler(repository);
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/prenotazioni/index");

        return oidcLogoutSuccessHandler;
    }
}