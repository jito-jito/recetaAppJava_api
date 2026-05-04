package com.duoc.seguridadcalidad.modelos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Valoracion Model Tests")
public class ValoracionTest {

    @Test
    @DisplayName("Debería crear una Valoracion con el constructor vacío")
    void testConstructorVacio() {
        Valoracion valoracion = new Valoracion();
        assertNull(valoracion.getIdValoracion());
        assertNull(valoracion.getPuntaje());
        assertNull(valoracion.getAutor());
    }

    @Test
    @DisplayName("Debería crear una Valoracion con parámetros")
    void testConstructorConParametros() {
        Receta receta = new Receta();
        receta.setIdReceta(1);
        
        Valoracion valoracion = new Valoracion(5, "Usuario1", receta);
        
        assertEquals(5, valoracion.getPuntaje());
        assertEquals("Usuario1", valoracion.getAutor());
        assertEquals(receta, valoracion.getReceta());
    }

    @Test
    @DisplayName("Debería obtener y establecer el ID de valoración")
    void testIdValoracionGettersSetters() {
        Valoracion valoracion = new Valoracion();
        valoracion.setIdValoracion(10);
        assertEquals(10, valoracion.getIdValoracion());
    }

    @Test
    @DisplayName("Debería obtener y establecer el puntaje")
    void testPuntajeGettersSetters() {
        Valoracion valoracion = new Valoracion();
        valoracion.setPuntaje(4);
        assertEquals(4, valoracion.getPuntaje());
    }

    @Test
    @DisplayName("Debería obtener y establecer el autor")
    void testAutorGettersSetters() {
        Valoracion valoracion = new Valoracion();
        valoracion.setAutor("María");
        assertEquals("María", valoracion.getAutor());
    }

    @Test
    @DisplayName("Debería obtener y establecer la receta")
    void testRecetaGettersSetters() {
        Valoracion valoracion = new Valoracion();
        Receta receta = new Receta();
        receta.setIdReceta(5);
        valoracion.setReceta(receta);
        assertEquals(receta, valoracion.getReceta());
        assertEquals(5, valoracion.getReceta().getIdReceta());
    }

    @Test
    @DisplayName("Debería validar puntaje con diferentes valores")
    void testPuntajeConDiferentesValores() {
        Valoracion valoracion = new Valoracion();
        
        valoracion.setPuntaje(1);
        assertEquals(1, valoracion.getPuntaje());
        
        valoracion.setPuntaje(3);
        assertEquals(3, valoracion.getPuntaje());
        
        valoracion.setPuntaje(5);
        assertEquals(5, valoracion.getPuntaje());
    }

    @Test
    @DisplayName("Debería mantener referencia a la receta después de establecerla")
    void testRefereniaRecetaIntacta() {
        Receta receta = new Receta();
        receta.setIdReceta(7);
        receta.setTitulo("Ensalada");
        
        Valoracion valoracion = new Valoracion(4, "Juan", receta);
        
        assertEquals("Ensalada", valoracion.getReceta().getTitulo());
        assertEquals(7, valoracion.getReceta().getIdReceta());
    }

    @Test
    @DisplayName("Debería actualizar el autor de una valoración")
    void testActualizarAutor() {
        Valoracion valoracion = new Valoracion(5, "UsuarioOriginal", new Receta());
        assertEquals("UsuarioOriginal", valoracion.getAutor());
        
        valoracion.setAutor("UsuarioNuevo");
        assertEquals("UsuarioNuevo", valoracion.getAutor());
    }
}

