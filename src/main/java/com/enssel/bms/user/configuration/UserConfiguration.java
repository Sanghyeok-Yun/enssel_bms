package com.enssel.bms.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class UserConfiguration {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsManager getUserDetailsManager(JdbcTemplate jdbcTemplate) {
        return new JdbcUserDetailsManager(jdbcTemplate.getDataSource()){
            @Override
            public void changePassword(String oldPassword, String newPassword) throws AuthenticationException {
                super.changePassword(oldPassword, passwordEncoder().encode(newPassword));
            }
        };
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider(JdbcTemplate jdbcTemplate){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(getUserDetailsManager(jdbcTemplate));
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager providerManager(JdbcTemplate jdbcTemplate){
        DaoAuthenticationProvider authenticationProvider = (DaoAuthenticationProvider) daoAuthenticationProvider(jdbcTemplate);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);

        JdbcUserDetailsManager jdbcUserDetailsManager = (JdbcUserDetailsManager) getUserDetailsManager(jdbcTemplate);
        jdbcUserDetailsManager.setAuthenticationManager(providerManager);

        return providerManager;
    }
}
