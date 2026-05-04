package com.duoc.seguridadcalidad.modelos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RecetaRequestDto Tests")
public class RecetaRequestDtoTest {

    @Test
    @DisplayName("Debería crear un RecetaRequestDto con valores nulos por defecto")
    void testConstructorDefault() {
        RecetaRequestDto dto = new RecetaRequestDto();
        assertNull(dto.getTitulo());
        assertNull(dto.getTipoCocina());
        assertNull(dto.getPaisOrigen());
        assertNull(dto.getDificultad());
        assertNull(dto.getTiempoCoccion());
        assertNull(dto.getInstrucciones());
        assertNull(dto.getPopularidad());
        assertNull(dto.getFechaPublicacion());
    }

    @Test
    @DisplayName("Debería establecer y obtener el título")
    void testTituloGettersSetters() {
        RecetaRequestDto dto = new RecetaRequestDto();
        dto.setTitulo("Pasta al Dente");
        assertEquals("Pasta al Dente", dto.getTitulo());
    }

    @Test
    @DisplayName("Debería establecer y obtener el tipo de cocina")
    void testTipoCocinaGettersSetters() {
        RecetaRequestDto dto = new RecetaRequestDto();
        dto.setTipoCocina("Italiana");
        assertEquals("Italiana", dto.getTipoCocina());
    }

    @Test
    @DisplayName("Debería establecer y obtener el país de origen")
    void testPaisOrigenGettersSetters() {
        RecetaRequestDto dto = new RecetaRequestDto();
        dto.setPaisOrigen("Italia");
        assertEquals("Italia", dto.getPaisOrigen());
    }

    @Test
    @DisplayName("Debería establecer y obtener la dificultad")
    void testDificultadGettersSetters() {
        RecetaRequestDto dto = new RecetaRequestDto();
        dto.setDificultad(Dificultad.MEDIA);
        assertEquals(Dificultad.MEDIA, dto.getDificultad());
    }

    @Test
    @DisplayName("Debería establecer y obtener el tiempo de cocción")
    void testTiempoCoccionGettersSetters() {
        RecetaRequestDto dto = new RecetaRequestDto();
        dto.setTiempoCoccion(30);
        assertEquals(30, dto.getTiempoCoccion());
    }

    @Test
    @DisplayName("Debería establecer y obtener las instrucciones")
    void testInstruccionesGettersSetters() {
        RecetaRequestDto dto = new RecetaRequestDto();
        String instrucciones = "Hervir agua. Agregar pasta. Cocinar 10 minutos.";
        dto.setInstrucciones(instrucciones);
        assertEquals(instrucciones, dto.getInstrucciones());
    }

    @Test
    @DisplayName("Debería establecer y obtener la popularidad")
    void testPopularidadGettersSetters() {
        RecetaRequestDto dto = new RecetaRequestDto();
        dto.setPopularidad(4.8);
        assertEquals(4.8, dto.getPopularidad());
    }

    @Test
    @DisplayName("Debería establecer y obtener la fecha de publicación")
    void testFechaPublicacionGettersSetters() {
        RecetaRequestDto dto = new RecetaRequestDto();
        LocalDate fecha = LocalDate.of(2024, 5, 15);
        dto.setFechaPublicacion(fecha);
        assertEquals(fecha, dto.getFechaPublicacion());
    }

    @Test
    @DisplayName("Debería permitir establecer todos los campos simultáneamente")
    void testTodosCamposSimultaneamente() {
        RecetaRequestDto dto = new RecetaRequestDto();
        LocalDate fecha = LocalDate.of(2024, 5, 15);
        
        dto.setTitulo("Tacos");
        dto.setTipoCocina("Mexicana");
        dto.setPaisOrigen("México");
        dto.setDificultad(Dificultad.BAJA);
        dto.setTiempoCoccion(20);
        dto.setInstrucciones("Preparar tortillas y rellenar");
        dto.setPopularidad(4.5);
        dto.setFechaPublicacion(fecha);
        
        assertEquals("Tacos", dto.getTitulo());
        assertEquals("Mexicana", dto.getTipoCocina());
        assertEquals("México", dto.getPaisOrigen());
        assertEquals(Dificultad.BAJA, dto.getDificultad());
        assertEquals(20, dto.getTiempoCoccion());
        assertEquals("Preparar tortillas y rellenar", dto.getInstrucciones());
        assertEquals(4.5, dto.getPopularidad());
        assertEquals(fecha, dto.getFechaPublicacion());
    }

    @Test
    @DisplayName("Debería permitir actualizar valores null a valores válidos")
    void testActualizarDesdeNull() {
        RecetaRequestDto dto = new RecetaRequestDto();
        assertNull(dto.getTitulo());
        
        dto.setTitulo("Nuevo Título");
        assertEquals("Nuevo Título", dto.getTitulo());
    }

    @Test
    @DisplayName("Debería permitir actualizar valores válidos a null")
    void testActualizarANull() {
        RecetaRequestDto dto = new RecetaRequestDto();
        dto.setTitulo("Título");
        assertEquals("Título", dto.getTitulo());
        
        dto.setTitulo(null);
        assertNull(dto.getTitulo());
    }

    @Test
    @DisplayName("Debería mantener independencia entre instancias")
    void testIndependenciaInstancias() {
        RecetaRequestDto dto1 = new RecetaRequestDto();
        RecetaRequestDto dto2 = new RecetaRequestDto();
        
        dto1.setTitulo("Receta 1");
        dto2.setTitulo("Receta 2");
        
        assertEquals("Receta 1", dto1.getTitulo());
        assertEquals("Receta 2", dto2.getTitulo());
        assertNotEquals(dto1.getTitulo(), dto2.getTitulo());
    }

    @Test
    @DisplayName("Debería permitir diferentes dificultades")
    void testDiferentesDificultades() {
        RecetaRequestDto dto1 = new RecetaRequestDto();
        RecetaRequestDto dto2 = new RecetaRequestDto();
        RecetaRequestDto dto3 = new RecetaRequestDto();
        
        dto1.setDificultad(Dificultad.BAJA);
        dto2.setDificultad(Dificultad.MEDIA);
        dto3.setDificultad(Dificultad.ALTA);
        
        assertEquals(Dificultad.BAJA, dto1.getDificultad());
        assertEquals(Dificultad.MEDIA, dto2.getDificultad());
        assertEquals(Dificultad.ALTA, dto3.getDificultad());
    }
}

