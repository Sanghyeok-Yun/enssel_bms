package com.enssel.bms.config.security;

import com.enssel.bms.config.security.entryPoint.CustomEntryPoint;
import com.enssel.bms.config.security.converter.SimpleJwtAuthenticationConverter;
import com.enssel.bms.config.security.handler.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class JwtConfig {
    @Bean
    public SecurityFilterChain jwtChain(HttpSecurity http, SimpleJwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http.authorizeHttpRequests(
                        httpRequest ->
                                //해당 url에 권한 부여
                                httpRequest
                                        .requestMatchers("/api/user/**").hasAnyAuthority("ROLE_admin")
                                        .requestMatchers("/admin/**").hasAnyAuthority("ROLE_admin", "ROLE_manager")
                                        .requestMatchers("/api/**").hasAnyAuthority("ROLE_user")
                                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(
                        oauth2Request ->
                                oauth2Request.accessDeniedHandler(new CustomAccessDeniedHandler())
                                        .authenticationEntryPoint(new CustomEntryPoint()).jwt(
                                                jwtRequest ->
                                                        jwtRequest.jwtAuthenticationConverter(jwtAuthenticationConverter)
                                        )
                );

        return http.build();
    }
}
