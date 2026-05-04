package com.duoc.seguridadcalidad.modelos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Comentario Model Tests")
public class ComentarioTest {

    @Test
    @DisplayName("Debería crear un Comentario con el constructor vacío")
    void testConstructorVacio() {
        Comentario comentario = new Comentario();
        assertNotNull(comentario.getFecha());
    }

    @Test
    @DisplayName("Debería crear un Comentario con parámetros")
    void testConstructorConParametros() {
        Receta receta = new Receta();
        receta.setIdReceta(1);
        
        Comentario comentario = new Comentario("Excelente receta", "Juan", receta);
        
        assertEquals("Excelente receta", comentario.getTexto());
        assertEquals("Juan", comentario.getAutor());
        assertEquals(receta, comentario.getReceta());
        assertNotNull(comentario.getFecha());
    }

    @Test
    @DisplayName("Debería obtener y establecer el ID del comentario")
    void testIdComentarioGettersSetters() {
        Comentario comentario = new Comentario();
        comentario.setIdComentario(5);
        assertEquals(5, comentario.getIdComentario());
    }

    @Test
    @DisplayName("Debería obtener y establecer el texto")
    void testTextoGettersSetters() {
        Comentario comentario = new Comentario();
        comentario.setTexto("Muy sabroso");
        assertEquals("Muy sabroso", comentario.getTexto());
    }

    @Test
    @DisplayName("Debería obtener y establecer el autor")
    void testAutorGettersSetters() {
        Comentario comentario = new Comentario();
        comentario.setAutor("María");
        assertEquals("María", comentario.getAutor());
    }

    @Test
    @DisplayName("Debería obtener y establecer la fecha")
    void testFechaGettersSetters() {
        Comentario comentario = new Comentario();
        LocalDateTime ahora = LocalDateTime.now();
        comentario.setFecha(ahora);
        assertEquals(ahora, comentario.getFecha());
    }

    @Test
    @DisplayName("Debería obtener y establecer la receta")
    void testRecetaGettersSetters() {
        Comentario comentario = new Comentario();
        Receta receta = new Receta();
        receta.setIdReceta(3);
        comentario.setReceta(receta);
        assertEquals(receta, comentario.getReceta());
        assertEquals(3, comentario.getReceta().getIdReceta());
    }

    @Test
    @DisplayName("Debería asignar la fecha actual en el constructor vacío")
    void testFechaActualEnConstructor() {
        LocalDateTime antes = LocalDateTime.now();
        Comentario comentario = new Comentario();
        LocalDateTime despues = LocalDateTime.now();
        
        assertNotNull(comentario.getFecha());
        assertTrue(comentario.getFecha().isAfter(antes.minusSeconds(1)));
        assertTrue(comentario.getFecha().isBefore(despues.plusSeconds(1)));
    }

    @Test
    @DisplayName("Debería asignar la fecha actual en el constructor con parámetros")
    void testFechaActualEnConstructorConParametros() {
        Receta receta = new Receta();
        LocalDateTime antes = LocalDateTime.now();
        Comentario comentario = new Comentario("Test", "User", receta);
        LocalDateTime despues = LocalDateTime.now();
        
        assertNotNull(comentario.getFecha());
        assertTrue(comentario.getFecha().isAfter(antes.minusSeconds(1)));
        assertTrue(comentario.getFecha().isBefore(despues.plusSeconds(1)));
    }

    @Test
    @DisplayName("Debería actualizar el autor correctamente")
    void testActualizarAutor() {
        Comentario comentario = new Comentario("Original", "UsuarioOriginal", new Receta());
        assertEquals("UsuarioOriginal", comentario.getAutor());
        
        comentario.setAutor("UsuarioNuevo");
        assertEquals("UsuarioNuevo", comentario.getAutor());
    }

    @Test
    @DisplayName("Debería actualizar el texto correctamente")
    void testActualizarTexto() {
        Comentario comentario = new Comentario("Texto original", "User", new Receta());
        assertEquals("Texto original", comentario.getTexto());
        
        comentario.setTexto("Texto actualizado");
        assertEquals("Texto actualizado", comentario.getTexto());
    }
}

