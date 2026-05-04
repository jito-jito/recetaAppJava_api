package com.duoc.seguridadcalidad.modelos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Receta Model Tests")
public class RecetaTest {

    @Test
    @DisplayName("Debería crear una Receta con el constructor vacío")
    void testConstructorVacio() {
        Receta receta = new Receta();
        assertNull(receta.getIdReceta());
        assertNull(receta.getTitulo());
        assertFalse(receta.isPublicada());
    }

    @Test
    @DisplayName("Debería crear una Receta con todos los parámetros")
    void testConstructorConParametros() {
        Ingrediente ingrediente = new Ingrediente("Harina", 200.0, "gramos");
        List<String> imagenes = List.of("image1.jpg");
        List<String> videos = List.of("video1.mp4");
        
        Receta receta = new Receta(
                1, "Pasta Carbonara", "Italiana", "Italia", Dificultad.MEDIA,
                30, "Mezclar pasta con huevos", 4.5, LocalDate.of(2024, 1, 1),
                imagenes, videos, "Chef Juan", true, ingrediente
        );
        
        assertEquals(1, receta.getIdReceta());
        assertEquals("Pasta Carbonara", receta.getTitulo());
        assertEquals("Italiana", receta.getTipoCocina());
        assertEquals("Italia", receta.getPaisOrigen());
        assertEquals(Dificultad.MEDIA, receta.getDificultad());
        assertEquals(30, receta.getTiempoCoccion());
        assertEquals("Mezclar pasta con huevos", receta.getInstrucciones());
        assertEquals(4.5, receta.getPopularidad());
        assertEquals(LocalDate.of(2024, 1, 1), receta.getFechaPublicacion());
        assertEquals("Chef Juan", receta.getAutor());
        assertTrue(receta.isPublicada());
        assertEquals(1, receta.getIngredientes().size());
    }

    @Test
    @DisplayName("Debería obtener el ID de la receta")
    void testGetIdReceta() {
        Receta receta = new Receta();
        receta.setIdReceta(5);
        assertEquals(5, receta.getIdReceta());
    }

    @Test
    @DisplayName("Debería establecer el ID de la receta")
    void testSetIdReceta() {
        Receta receta = new Receta();
        receta.setIdReceta(10);
        assertEquals(10, receta.getIdReceta());
    }

    @Test
    @DisplayName("Debería obtener y establecer el título")
    void testTituloGettersSetters() {
        Receta receta = new Receta();
        receta.setTitulo("Tacos al Pastor");
        assertEquals("Tacos al Pastor", receta.getTitulo());
    }

    @Test
    @DisplayName("Debería obtener y establecer el tipo de cocina")
    void testTipoCocinaGettersSetters() {
        Receta receta = new Receta();
        receta.setTipoCocina("Mexicana");
        assertEquals("Mexicana", receta.getTipoCocina());
    }

    @Test
    @DisplayName("Debería obtener y establecer el país de origen")
    void testPaisOrigenGettersSetters() {
        Receta receta = new Receta();
        receta.setPaisOrigen("México");
        assertEquals("México", receta.getPaisOrigen());
    }

    @Test
    @DisplayName("Debería obtener y establecer la dificultad")
    void testDificultadGettersSetters() {
        Receta receta = new Receta();
        receta.setDificultad(Dificultad.ALTA);
        assertEquals(Dificultad.ALTA, receta.getDificultad());
    }

    @Test
    @DisplayName("Debería obtener y establecer el tiempo de cocción")
    void testTiempoCoccionGettersSetters() {
        Receta receta = new Receta();
        receta.setTiempoCoccion(45);
        assertEquals(45, receta.getTiempoCoccion());
    }

    @Test
    @DisplayName("Debería obtener y establecer las instrucciones")
    void testInstruccionesGettersSetters() {
        Receta receta = new Receta();
        String instrucciones = "Paso 1: Mezclar. Paso 2: Cocer.";
        receta.setInstrucciones(instrucciones);
        assertEquals(instrucciones, receta.getInstrucciones());
    }

    @Test
    @DisplayName("Debería obtener y establecer la popularidad")
    void testPopularidadGettersSetters() {
        Receta receta = new Receta();
        receta.setPopularidad(8.5);
        assertEquals(8.5, receta.getPopularidad());
    }

    @Test
    @DisplayName("Debería obtener y establecer la fecha de publicación")
    void testFechaPublicacionGettersSetters() {
        Receta receta = new Receta();
        LocalDate fecha = LocalDate.of(2024, 5, 15);
        receta.setFechaPublicacion(fecha);
        assertEquals(fecha, receta.getFechaPublicacion());
    }

    @Test
    @DisplayName("Debería obtener y establecer el autor")
    void testAutorGettersSetters() {
        Receta receta = new Receta();
        receta.setAutor("Chef María");
        assertEquals("Chef María", receta.getAutor());
    }

    @Test
    @DisplayName("Debería obtener y establecer el estado de publicada")
    void testPublicadaGettersSetters() {
        Receta receta = new Receta();
        assertFalse(receta.isPublicada());
        receta.setPublicada(true);
        assertTrue(receta.isPublicada());
    }

    @Test
    @DisplayName("Debería obtener y establecer las imágenes")
    void testImagenesGettersSetters() {
        Receta receta = new Receta();
        List<String> imagenes = new ArrayList<>();
        imagenes.add("img1.jpg");
        imagenes.add("img2.jpg");
        receta.setImagenes(imagenes);
        assertEquals(2, receta.getImagenes().size());
        assertTrue(receta.getImagenes().contains("img1.jpg"));
    }

    @Test
    @DisplayName("Debería obtener y establecer los videos")
    void testVideosGettersSetters() {
        Receta receta = new Receta();
        List<String> videos = new ArrayList<>();
        videos.add("video1.mp4");
        receta.setVideos(videos);
        assertEquals(1, receta.getVideos().size());
        assertTrue(receta.getVideos().contains("video1.mp4"));
    }

    @Test
    @DisplayName("Debería obtener y establecer los ingredientes")
    void testIngredientesGettersSetters() {
        Receta receta = new Receta();
        List<Ingrediente> ingredientes = new ArrayList<>();
        ingredientes.add(new Ingrediente("Sal", 5.0, "gramos"));
        ingredientes.add(new Ingrediente("Agua", 1.0, "litro"));
        receta.setIngredientes(ingredientes);
        assertEquals(2, receta.getIngredientes().size());
    }

    @Test
    @DisplayName("Debería obtener y establecer los comentarios")
    void testComentariosGettersSetters() {
        Receta receta = new Receta();
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(new Comentario("Muy buena", "Juan", receta));
        receta.setComentarios(comentarios);
        assertEquals(1, receta.getComentarios().size());
    }

    @Test
    @DisplayName("Debería obtener y establecer las valoraciones")
    void testValoracionesGettersSetters() {
        Receta receta = new Receta();
        List<Valoracion> valoraciones = new ArrayList<>();
        valoraciones.add(new Valoracion(5, "María", receta));
        receta.setValoraciones(valoraciones);
        assertEquals(1, receta.getValoraciones().size());
    }

    @Test
    @DisplayName("Debería calcular el puntaje promedio correctamente")
    void testGetPuntajePromedio() {
        Receta receta = new Receta();
        receta.setValoraciones(new ArrayList<>());
        
        // Sin valoraciones
        assertEquals(0.0, receta.getPuntajePromedio());
        
        // Con una valoración
        List<Valoracion> valoraciones = new ArrayList<>();
        valoraciones.add(new Valoracion(5, "Usuario1", receta));
        receta.setValoraciones(valoraciones);
        assertEquals(5.0, receta.getPuntajePromedio());
        
        // Con múltiples valoraciones
        valoraciones.add(new Valoracion(4, "Usuario2", receta));
        valoraciones.add(new Valoracion(3, "Usuario3", receta));
        receta.setValoraciones(valoraciones);
        assertEquals(4.0, receta.getPuntajePromedio());
    }

    @Test
    @DisplayName("Debería calcular cantidad de comentarios correctamente")
    void testGetCantidadComentarios() {
        Receta receta = new Receta();
        receta.setComentarios(null);
        assertEquals(0, receta.getCantidadComentarios());
        
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(new Comentario("Excelente", "Juan", receta));
        comentarios.add(new Comentario("Muy sabroso", "María", receta));
        receta.setComentarios(comentarios);
        assertEquals(2, receta.getCantidadComentarios());
    }

    @Test
    @DisplayName("Debería generar el toString correctamente")
    void testToString() {
        Receta receta = new Receta();
        receta.setIdReceta(1);
        receta.setTitulo("Pizza");
        String str = receta.toString();
        assertTrue(str.contains("Receta{"));
        assertTrue(str.contains("idReceta=1"));
        assertTrue(str.contains("titulo='Pizza'"));
    }

    @Test
    @DisplayName("Debería tener listas iniciales vacías por defecto")
    void testListasVaciasDefault() {
        Receta receta = new Receta();
        assertNotNull(receta.getImagenes());
        assertNotNull(receta.getVideos());
        assertNotNull(receta.getIngredientes());
        assertNotNull(receta.getComentarios());
        assertNotNull(receta.getValoraciones());
    }
}

