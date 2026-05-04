package com.duoc.seguridadcalidad.controladores;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Debería retornar información general de la API en el root (/)")
    @WithMockUser
    void testRootEndpoint() throws Exception {
        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verificamos campos raíz
                .andExpect(jsonPath("$.message", is("API Backend de Recetas - Seguridad y Calidad en el Desarrollo")))
                .andExpect(jsonPath("$.version", is("1.0.0")))
                .andExpect(jsonPath("$.status", is("active")))
                // Verificamos llaves específicas dentro del objeto 'endpoints'
                .andExpect(jsonPath("$.endpoints.recipes").value("/recipes"))
                .andExpect(jsonPath("$.endpoints.login").value("/login"))
                .andExpect(jsonPath("$.endpoints['h2-console']").value("/h2-console (development only)"));
    }

    @Test
    @DisplayName("Debería retornar el estado UP en el endpoint /health")
    @WithMockUser
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("receta-backend-api")));
    }

    @Test
    @DisplayName("Debería requerir autenticación para el root endpoint")
    void testRootEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
