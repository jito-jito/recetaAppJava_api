package com.duoc.seguridadcalidad;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    private final CorsConfig corsConfig = new CorsConfig();

    @Test
    void testCorsConfigurationSource() {
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();

        assertNotNull(source);

        // Crear un mock de HttpServletRequest para cualquier ruta
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");

        CorsConfiguration config = source.getCorsConfiguration(request);
        assertNotNull(config);

        assertNotNull(config.getAllowedOrigins());
        assertTrue(config.getAllowedOrigins().contains("http://localhost:8081"));
        assertNotNull(config.getAllowedMethods());
        assertTrue(config.getAllowedMethods().contains("GET"));
        assertTrue(config.getAllowedMethods().contains("POST"));
        assertTrue(config.getAllowedMethods().contains("PUT"));
        assertTrue(config.getAllowedMethods().contains("DELETE"));
        assertTrue(config.getAllowedMethods().contains("OPTIONS"));
        assertNotNull(config.getAllowedHeaders());
        assertTrue(config.getAllowedHeaders().contains("*"));
        assertEquals(Boolean.TRUE, config.getAllowCredentials());
    }
}
