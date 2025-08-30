package com.esfile.security;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
/**
 * JWT认证提供者
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // TODO: JWT认证实现
        return null;
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
