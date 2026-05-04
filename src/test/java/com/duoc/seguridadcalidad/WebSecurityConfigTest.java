package com.duoc.seguridadcalidad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.AdditionalMatchers.not;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("WebSecurityConfig Tests")
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Debería permitir acceso GET a /recipes sin autenticación")
    void testGetRecipesPermittedWithoutAuth() throws Exception {
        mockMvc.perform(get("/recipes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // 200 OK porque GET /recipes es público
    }

    @Test
    @DisplayName("Debería permitir acceso GET a /recipes/{id} sin autenticación")
    void testGetRecipeByIdPermittedWithoutAuth() throws Exception {
        mockMvc.perform(get("/recipes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // receta sí existe
    }

    @Test
    @DisplayName("Debería permitir acceso GET a /recipes/search sin autenticación")
    void testSearchRecipesPermittedWithoutAuth() throws Exception {
        mockMvc.perform(get("/recipes/search?titulo=test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // 200 OK porque búsqueda es pública
    }

    @Test
    @DisplayName("Debería requerir autenticación para POST a /recipes (crear receta)")
    void testPostRecipesRequiresAuth() throws Exception {
        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isForbidden()); // Cambiado a 403 (comportamiento por defecto de Spring Security)
    }

    @Test
    @DisplayName("Debería requerir autenticación para PUT a /recipes/{id}")
    void testPutRecipesRequiresAuth() throws Exception {
        mockMvc.perform(put("/recipes/1")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isForbidden()); // Cambiado a 403
    }

    @Test
    @DisplayName("Debería requerir autenticación para POST a /recipes/{id}/comentarios")
    void testPostComentarioRequiresAuth() throws Exception {
        mockMvc.perform(post("/recipes/1/comentarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden()); // Cambiado a 403
    }

    @Test
    @DisplayName("Debería requerir autenticación para GET /api/usuarios/profile")
    void testProfileRequiresAuth() throws Exception {
        mockMvc.perform(get("/api/usuarios/profile")
                        .param("username", "testuser"))
                .andExpect(status().isForbidden()); // Cambiado a 403
    }

    @Test
    @DisplayName("Debería requerir autenticación para POST a /recipes/{id}/valoraciones")
    void testPostValoracionRequiresAuth() throws Exception {
        mockMvc.perform(post("/recipes/1/valoraciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden()); // Cambiado a 403
    }

    @Test
    @DisplayName("Debería requerir autenticación para DELETE /recipes/{id}")
    void testDeleteRecipeRequiresAuth() throws Exception {
        mockMvc.perform(delete("/recipes/1"))
                .andExpect(status().isForbidden()); // Cambiado a 403
    }

    @Test
    @DisplayName("Debería permitir GET /recipes/mis-recetas sin autenticación (devuelve lista vacía para anonymous)")
    void testMisRecetasPermittedWithoutAuth() throws Exception {
        mockMvc.perform(get("/recipes/mis-recetas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // 200 OK porque GET /recipes/** es permitido
    }
}

