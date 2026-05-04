package com.duoc.seguridadcalidad;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JWTAuthenticationConfigTest {

    private final JWTAuthenticationConfig jwtConfig = new JWTAuthenticationConfig();

    @Test
    void testGetJWTToken() {
        String token = jwtConfig.getJWTToken("testuser");

        assertNotNull(token);
        assertTrue(token.startsWith("Bearer "));
        assertTrue(token.length() > "Bearer ".length());
    }
}
