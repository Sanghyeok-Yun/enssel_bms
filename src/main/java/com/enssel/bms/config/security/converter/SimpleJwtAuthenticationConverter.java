package com.enssel.bms.config.security.converter;

import com.enssel.bms.config.security.token.CustomJwtAuthenticationToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SimpleJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

    @Override
    public final AbstractAuthenticationToken convert(Jwt jwt) { // 3
        CustomJwtAuthenticationToken token = new CustomJwtAuthenticationToken(jwt); // 4
        Collection<GrantedAuthority> roles = token.getroles();
        return convert(jwt, roles); // 5
    }

    public AbstractAuthenticationToken convert(Jwt jwt, Collection<GrantedAuthority> roles) {
        return new JwtAuthenticationToken(jwt, roles); // 2
    }
}
