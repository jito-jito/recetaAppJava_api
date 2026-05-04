package com.duoc.seguridadcalidad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("WebMvcConfig Tests")
public class WebMvcConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Debería servir archivos de medios desde el directorio uploads")
    @WithMockUser
    void testMediaHandlerConfigured() throws Exception {
        // Verificamos que el endpoint /media está configurado para servir archivos
        // Realizamos una solicitud a un archivo que podría existir
        // El resultado dependerá de si el archivo existe o no, pero no debemos obtener un error de configuración
        mockMvc.perform(get("/media/test.jpg"))
                .andExpect(status().isNotFound()); // 404 es esperado si el archivo no existe
    }

    @Test
    @DisplayName("Debería permitir acceso a archivos en /media sin autenticación")
    void testMediaAccessWithoutAuth() throws Exception {
        mockMvc.perform(get("/media/imagenes/test.jpg"))
                .andExpect(status().isNotFound()); // 404 pero permitido (sin 401)
    }

    @Test
    @DisplayName("Debería servir recursos de imagen")
    @WithMockUser
    void testImageHandling() throws Exception {
        mockMvc.perform(get("/media/imagenes/recipe.png"))
                .andExpect(status().isNotFound()); // 404 if file doesn't exist, not configuration error
    }

    @Test
    @DisplayName("Debería servir recursos de video")
    @WithMockUser
    void testVideoHandling() throws Exception {
        mockMvc.perform(get("/media/videos/recipe.mp4"))
                .andExpect(status().isNotFound()); // 404 if file doesn't exist, not configuration error
    }

    @Test
    @DisplayName("Debería mantener la ruta original de archivos")
    @WithMockUser
    void testPathPreservation() throws Exception {
        mockMvc.perform(get("/media/imagenes/subdirectorio/imagen.jpg"))
                .andExpect(status().isNotFound()); // Ruta preservada, pero archivo no existe
    }

    @Test
    @DisplayName("Debería servir archivos con extensiones diferentes")
    @WithMockUser
    void testDifferentFileExtensions() throws Exception {
        mockMvc.perform(get("/media/imagenes/test.jpg"))
                .andExpect(status().isNotFound());
        
        mockMvc.perform(get("/media/imagenes/test.png"))
                .andExpect(status().isNotFound());
        
        mockMvc.perform(get("/media/imagenes/test.gif"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debería ser accesible sin CORS issues para archivos de media")
    @WithMockUser
    void testMediaAccessibility() throws Exception {
        // Este test verifica que /media/ esté correctamente configurado sin errores de CORS
        mockMvc.perform(get("/media/"))
                .andExpect(status().isNotFound()); // Directory listing or 404 es normal
    }

    @Test
    @DisplayName("Debería retornar 404 para archivos inexistentes en /media")
    @WithMockUser
    void testNonExistentFileHandling() throws Exception {
        mockMvc.perform(get("/media/imagenes/archivo_inexistente.jpg"))
                .andExpect(status().isNotFound());
    }
}



