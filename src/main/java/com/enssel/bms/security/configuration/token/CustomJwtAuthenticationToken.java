package com.enssel.bms.security.configuration.token;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {
    public CustomJwtAuthenticationToken(Jwt jwt) {
        super(jwt);
    }

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String name) {
        super(jwt, authorities, name);
    }

    public Collection<GrantedAuthority> getroles() {
        List<String> roles = ((ArrayList<LinkedTreeMap<String, String>>)((Jwt)this.getPrincipal()).getClaim("roles")).stream()
                .map(treeMap -> treeMap.get("role"))
                .toList();

        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
