package com.duoc.seguridadcalidad.controladores;

import com.duoc.seguridadcalidad.modelos.Comentario;
import com.duoc.seguridadcalidad.modelos.Receta;
import com.duoc.seguridadcalidad.modelos.Valoracion;
import com.duoc.seguridadcalidad.repositorios.ComentarioRepository;
import com.duoc.seguridadcalidad.repositorios.RecetaRepository;
import com.duoc.seguridadcalidad.repositorios.ValoracionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Proxy;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RecipeControllerTest {

    private RecipeController recipeController;
    private Receta recetaFake;
    private Valoracion valoracionFake;

    @BeforeEach
    void setUp() {
        // Configuramos seguridad sin mocks
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("usuarioTest", null)
        );

        // PROXY DINÁMICO para RecetaRepository
        RecetaRepository stubRecetaRepo = (RecetaRepository) Proxy.newProxyInstance(
                RecetaRepository.class.getClassLoader(),
                new Class<?>[]{RecetaRepository.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("findByPublicadaTrue")) return Collections.singletonList(new Receta());
                    if (method.getName().equals("findById")) return Optional.ofNullable(recetaFake);
                    if (method.getName().equals("findByAutor")) return Collections.emptyList();
                    if (method.getName().equals("existsById")) return recetaFake != null;
                    if (method.getName().equals("findByTituloContainingIgnoreCaseAndPublicadaTrue")) return Collections.singletonList(new Receta());
                    if (method.getName().equals("findByTipoCocinaContainingIgnoreCaseAndPublicadaTrue")) return Collections.singletonList(new Receta());
                    if (method.getName().equals("findByPaisOrigenContainingIgnoreCaseAndPublicadaTrue")) return Collections.singletonList(new Receta());
                    if (method.getName().equals("findByTiempoCoccionLessThanEqualAndPublicadaTrue")) return Collections.singletonList(new Receta());
                    if (method.getName().equals("findByPopularidadGreaterThanEqualAndPublicadaTrue")) return Collections.singletonList(new Receta());
                    if (method.getName().equals("findAllByPublicadaTrueOrderByPopularidadDesc")) return Collections.singletonList(new Receta());
                    if (method.getName().equals("findAllByPublicadaTrueOrderByFechaPublicacionDesc")) return Collections.singletonList(new Receta());
                    if (method.getName().equals("save")) return args[0];
                    if (method.getName().equals("deleteById")) return null;
                    return null;
                }
        );

        // PROXY DINÁMICO para ComentarioRepository
        ComentarioRepository stubComentarioRepo = (ComentarioRepository) Proxy.newProxyInstance(
                ComentarioRepository.class.getClassLoader(),
                new Class<?>[]{ComentarioRepository.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("findByRecetaOrderByFechaDesc")) return Collections.singletonList(new Comentario());
                    return null;
                }
        );

        // PROXY DINÁMICO para ValoracionRepository
        ValoracionRepository stubValoracionRepo = (ValoracionRepository) Proxy.newProxyInstance(
                ValoracionRepository.class.getClassLoader(),
                new Class<?>[]{ValoracionRepository.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("findByRecetaAndAutor")) return Optional.ofNullable(valoracionFake);
                    if (method.getName().equals("findByReceta")) return Collections.singletonList(new Valoracion());
                    return null;
                }
        );

        recipeController = new RecipeController(stubRecetaRepo, stubComentarioRepo, stubValoracionRepo, new ObjectMapper(), null);
    }

    // ===== GET TESTS =====

    @Test
    @DisplayName("Should return all published recipes")
    void testGetAllRecipes() {
        assertNotNull(recipeController.getAllRecipes());
    }

    @Test
    @DisplayName("Should return recipe when found by ID")
    void testGetRecipeByIdFound() {
        recetaFake = new Receta();
        recetaFake.setIdReceta(1);
        recetaFake.setTitulo("Test Recipe");

        ResponseEntity<Receta> response = recipeController.getRecipeById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Recipe", response.getBody().getTitulo());
    }

    @Test
    @DisplayName("Should return 404 when recipe not found by ID")
    void testGetRecipeByIdNotFound() {
        recetaFake = null;
        assertEquals(HttpStatus.NOT_FOUND, recipeController.getRecipeById(1).getStatusCode());
    }

    @Test
    @DisplayName("Should search by titulo when provided")
    void testSearchRecipesByTitle() {
        Iterable<Receta> result = recipeController.searchRecipes("pasta", null, null, null, null);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should search by empty titulo string returns nothing")
    void testSearchRecipesByBlankTitle() {
        Iterable<Receta> result = recipeController.searchRecipes("  ", null, null, null, null);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should search by tipoCocina when provided")
    void testSearchRecipesByTipoCocina() {
        Iterable<Receta> result = recipeController.searchRecipes(null, "italiana", null, null, null);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return default search when tipoCocina is blank")
    void testSearchRecipesByBlankTipoCocina() {
        Iterable<Receta> result = recipeController.searchRecipes(null, "  ", null, null, null);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should search by paisOrigen when provided")
    void testSearchRecipesByPaisOrigen() {
        Iterable<Receta> result = recipeController.searchRecipes(null, null, "Italia", null, null);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return default search when paisOrigen is blank")
    void testSearchRecipesByBlankPaisOrigen() {
        Iterable<Receta> result = recipeController.searchRecipes(null, null, "  ", null, null);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should search by tiempoMaximo when provided")
    void testSearchRecipesByTiempoMaximo() {
        Iterable<Receta> result = recipeController.searchRecipes(null, null, null, 30, null);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should search by popularidadMinima when provided")
    void testSearchRecipesByPopularidadMinima() {
        Iterable<Receta> result = recipeController.searchRecipes(null, null, null, null, 4.0);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should return all published recipes when no search params")
    void testSearchRecipesWithoutParams() {
        Iterable<Receta> result = recipeController.searchRecipes(null, null, null, null, null);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should get recipes by popularity")
    void testGetRecipesByPopularity() {
        List<Receta> result = recipeController.getRecipesByPopularity();
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should get recent recipes")
    void testGetRecentRecipes() {
        List<Receta> result = recipeController.getRecentRecipes();
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should get my recipes")
    void testGetMisRecetas() {
        Iterable<Receta> result = recipeController.getMisRecetas();
        assertNotNull(result);
    }

    // ===== COMENTARIOS TESTS =====

    @Test
    @DisplayName("Should add comment successfully")
    void testAddComentarioSuccess() {
        recetaFake = new Receta();
        recetaFake.setIdReceta(1);

        Comentario comentario = new Comentario();
        comentario.setTexto("Delicioso!");

        ResponseEntity<Receta> response = recipeController.addComentario(1, comentario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should return 404 when recipe not found for comment")
    void testAddComentarioRecipeNotFound() {
        recetaFake = null;

        Comentario comentario = new Comentario();
        comentario.setTexto("Delicioso!");

        ResponseEntity<Receta> response = recipeController.addComentario(1, comentario);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return 400 when comment text is null")
    void testAddComentarioTextNull() {
        recetaFake = new Receta();

        Comentario comentario = new Comentario();
        comentario.setTexto(null);

        ResponseEntity<Receta> response = recipeController.addComentario(1, comentario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return 400 when comment text is blank")
    void testAddComentarioTextBlank() {
        recetaFake = new Receta();

        Comentario comentario = new Comentario();
        comentario.setTexto("   ");

        ResponseEntity<Receta> response = recipeController.addComentario(1, comentario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should get comments for recipe")
    void testGetComentariosSuccess() {
        recetaFake = new Receta();

        ResponseEntity<Iterable<Comentario>> response = recipeController.getComentarios(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should return 404 when getting comments for non-existent recipe")
    void testGetComentariosRecipeNotFound() {
        recetaFake = null;

        ResponseEntity<Iterable<Comentario>> response = recipeController.getComentarios(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ===== VALORACIONES TESTS =====

    @Test
    @DisplayName("Should add rating successfully")
    void testAddValoracionSuccess() {
        recetaFake = new Receta();
        recetaFake.setIdReceta(1);
        recetaFake.setAutor("otherAuthor");
        valoracionFake = null;

        Valoracion valoracion = new Valoracion();
        valoracion.setPuntaje(5);

        ResponseEntity<Map<String, Object>> response = recipeController.addValoracion(1, valoracion);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should return 404 when recipe not found for rating")
    void testAddValoracionRecipeNotFound() {
        recetaFake = null;

        Valoracion valoracion = new Valoracion();
        valoracion.setPuntaje(5);

        ResponseEntity<Map<String, Object>> response = recipeController.addValoracion(1, valoracion);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return 403 when author tries to rate own recipe")
    void testAddValoracionAutorPropia() {
        recetaFake = new Receta();
        recetaFake.setAutor("usuarioTest");

        Valoracion valoracion = new Valoracion();
        valoracion.setPuntaje(5);

        ResponseEntity<Map<String, Object>> response = recipeController.addValoracion(1, valoracion);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("message"));
    }

    @Test
    @DisplayName("Should return 400 when rating score is null")
    void testAddValoracionPuntajeNull() {
        recetaFake = new Receta();
        recetaFake.setAutor("otherAuthor");

        Valoracion valoracion = new Valoracion();
        valoracion.setPuntaje(null);

        ResponseEntity<Map<String, Object>> response = recipeController.addValoracion(1, valoracion);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("message"));
    }

    @Test
    @DisplayName("Should return 400 when rating score is negative")
    void testAddValoracionPuntajeNegativo() {
        recetaFake = new Receta();
        recetaFake.setAutor("otherAuthor");

        Valoracion valoracion = new Valoracion();
        valoracion.setPuntaje(-1);

        ResponseEntity<Map<String, Object>> response = recipeController.addValoracion(1, valoracion);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return 400 when rating score is greater than 5")
    void testAddValoracionPuntajeMayora5() {
        recetaFake = new Receta();
        recetaFake.setAutor("otherAuthor");

        Valoracion valoracion = new Valoracion();
        valoracion.setPuntaje(6);

        ResponseEntity<Map<String, Object>> response = recipeController.addValoracion(1, valoracion);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Should update existing rating")
    void testAddValoracionUpdateExisting() {
        recetaFake = new Receta();
        recetaFake.setAutor("otherAuthor");
        valoracionFake = new Valoracion();
        valoracionFake.setPuntaje(3);

        Valoracion valoracion = new Valoracion();
        valoracion.setPuntaje(5);

        ResponseEntity<Map<String, Object>> response = recipeController.addValoracion(1, valoracion);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should get ratings for recipe")
    void testGetValoracionesSuccess() {
        recetaFake = new Receta();

        ResponseEntity<Iterable<Valoracion>> response = recipeController.getValoraciones(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Should return 404 when getting ratings for non-existent recipe")
    void testGetValoracionesRecipeNotFound() {
        recetaFake = null;

        ResponseEntity<Iterable<Valoracion>> response = recipeController.getValoraciones(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ===== PUBLISH STATUS TESTS =====

    @Test
    @DisplayName("Should change publish status successfully")
    void testChangePublishStatusSuccess() {
        recetaFake = new Receta();
        recetaFake.setAutor("usuarioTest");

        ResponseEntity<Receta> response = recipeController.changePublishStatus(1, true);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 when recipe not found for status change")
    void testChangePublishStatusNotFound() {
        recetaFake = null;

        ResponseEntity<Receta> response = recipeController.changePublishStatus(1, true);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return 403 when non-owner tries to change status")
    void testChangeStatusForbidden() {
        recetaFake = new Receta();
        recetaFake.setAutor("otro_autor");

        ResponseEntity<Receta> response = recipeController.changePublishStatus(1, true);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // ===== DELETE TESTS =====

    @Test
    @DisplayName("Should delete recipe successfully")
    void testDeleteRecipeSuccess() {
        recetaFake = new Receta();

        ResponseEntity<Void> response = recipeController.deleteRecipe(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent recipe")
    void testDeleteRecipeNotFound() {
        recetaFake = null;

        ResponseEntity<Void> response = recipeController.deleteRecipe(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}