package com.enssel.bms.security.configuration;

import com.enssel.bms.security.configuration.entryPoint.CustomEntryPoint;
import com.enssel.bms.security.configuration.converter.SimpleJwtAuthenticationConverter;
import com.enssel.bms.security.configuration.handler.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityChain(HttpSecurity http, SimpleJwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        httpRequest ->
                                //해당 url에 권한 부여
                                httpRequest
//                                        .requestMatchers("/api/v1/user").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
//                                        .requestMatchers("/api/v1/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                                        .requestMatchers("/api/v1/user/**").permitAll()
                                        .requestMatchers("/api/v1/user/code/**").permitAll()
                                        .requestMatchers("/api/v1/admin").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                                        .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
                                        .requestMatchers("/api/v1/bi/**").permitAll()
                                        .requestMatchers("/error").permitAll()
                                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(
                        oauth2Request ->
                                oauth2Request.accessDeniedHandler(new CustomAccessDeniedHandler())
                                        .authenticationEntryPoint(new CustomEntryPoint()).jwt(
                                                jwtRequest ->
                                                        jwtRequest
                                                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                                        )
                );

        return http.build();
    }

}
