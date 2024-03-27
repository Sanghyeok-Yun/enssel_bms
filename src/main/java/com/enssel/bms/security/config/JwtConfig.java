package com.enssel.bms.security.config;

import com.enssel.bms.security.config.entryPoint.CustomEntryPoint;
import com.enssel.bms.security.config.converter.SimpleJwtAuthenticationConverter;
import com.enssel.bms.security.config.handler.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class JwtConfig {
    @Bean
    public SecurityFilterChain jwtChain(HttpSecurity http, SimpleJwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        httpRequest ->
                                //해당 url에 권한 부여
                                httpRequest
                                        .requestMatchers("/api/v1/members").hasAnyAuthority("ROLE_admin")
                                        .requestMatchers("/api/v1/members/**").hasAnyAuthority("ROLE_admin")
                                        .requestMatchers("/api/v1/admin").hasAnyAuthority("ROLE_admin", "ROLE_manager")
                                        .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ROLE_admin", "ROLE_manager")
                                        .requestMatchers("/api/v1/bi/**").permitAll()
                                        .requestMatchers("/error").permitAll()
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
