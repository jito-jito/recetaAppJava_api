package com.duoc.seguridadcalidad.controladores;

import com.duoc.seguridadcalidad.modelos.Receta;
import com.duoc.seguridadcalidad.modelos.Usuario;
import com.duoc.seguridadcalidad.repositorios.RecetaRepository;
import com.duoc.seguridadcalidad.repositorios.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioControllerTest {

    private UsuarioController usuarioController;
    private Usuario usuarioFake;
    private Receta recetaFake;

    @BeforeEach
    void setUp() {
        // 1. Proxy para UserRepository
        UserRepository stubUserRepo = (UserRepository) Proxy.newProxyInstance(
                UserRepository.class.getClassLoader(),
                new Class<?>[]{UserRepository.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("findByUsername")) return usuarioFake;
                    if (method.getName().equals("save")) return args[0];
                    return null;
                }
        );

        // 2. Proxy para RecetaRepository
        RecetaRepository stubRecetaRepo = (RecetaRepository) Proxy.newProxyInstance(
                RecetaRepository.class.getClassLoader(),
                new Class<?>[]{RecetaRepository.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("findById")) return Optional.ofNullable(recetaFake);
                    return null;
                }
        );

        // 3. Stub manual para PasswordEncoder (sin lógica de encriptación real)
        PasswordEncoder stubEncoder = new PasswordEncoder() {
            @Override public String encode(CharSequence rawPassword) { return "encoded_" + rawPassword; }
            @Override public boolean matches(CharSequence raw, String encoded) { return encoded.endsWith(raw.toString()); }
        };

        usuarioController = new UsuarioController(stubUserRepo, stubRecetaRepo, stubEncoder);
    }

    @Test
    @DisplayName("Debería registrar un usuario exitosamente")
    void testRegisterSuccess() {
        usuarioFake = null; // Simulamos que el username está disponible
        UsuarioController.RegisterRequest req = new UsuarioController.RegisterRequest();
        req.setUsername("nuevoUser");
        req.setPassword("12345");
        req.setEmail("test@duoc.cl");

        ResponseEntity<Map<String, String>> response = usuarioController.register(req);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User registered successfully", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Debería fallar registro si el usuario ya existe")
    void testRegisterConflict() {
        usuarioFake = new Usuario(); // Simulamos que el usuario ya existe
        UsuarioController.RegisterRequest req = new UsuarioController.RegisterRequest();
        req.setUsername("existente");

        ResponseEntity<Map<String, String>> response = usuarioController.register(req);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DisplayName("Debería agregar receta a favoritos correctamente")
    void testAgregarFavorito() {
        usuarioFake = new Usuario();
        usuarioFake.setUsername("userTest");
        recetaFake = new Receta();
        recetaFake.setIdReceta(100);

        // Simulamos el objeto Authentication que Spring Security pasaría al método
        Authentication auth = new UsernamePasswordAuthenticationToken("userTest", null);

        ResponseEntity<Map<String, String>> response = usuarioController.agregarRecetaFavorita(100, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Receta agregada a favoritos exitosamente", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Debería dar 404 al agregar favorito si la receta no existe")
    void testAgregarFavoritoNotFound() {
        usuarioFake = new Usuario();
        recetaFake = null; // Receta no encontrada
        Authentication auth = new UsernamePasswordAuthenticationToken("userTest", null);

        ResponseEntity<Map<String, String>> response = usuarioController.agregarRecetaFavorita(999, auth);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Debería dar 404 al agregar favorito si el usuario no existe")
    void testAgregarFavoritoUserNotFound() {
        usuarioFake = null; // Usuario no encontrado
        recetaFake = new Receta();
        Authentication auth = new UsernamePasswordAuthenticationToken("userTest", null);

        ResponseEntity<Map<String, String>> response = usuarioController.agregarRecetaFavorita(100, auth);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usuario no encontrado", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Debería obtener perfil de usuario correctamente")
    void testGetProfileSuccess() {
        usuarioFake = new Usuario();
        usuarioFake.setIdUsuario(1);
        usuarioFake.setUsername("testUser");
        usuarioFake.setEmail("test@example.com");
        usuarioFake.setEstaAutenticado(true);

        ResponseEntity<Usuario> response = usuarioController.getProfile("testUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Usuario body = response.getBody();
        assertNotNull(body);
        assertEquals("testUser", body.getUsername());
        assertEquals("test@example.com", body.getEmail());
        assertTrue(body.getEstaAutenticado());
        // Password should not be included
        assertNull(body.getPassword());
    }

    @Test
    @DisplayName("Debería retornar 404 si el usuario no existe en getProfile")
    void testGetProfileNotFound() {
        usuarioFake = null; // Usuario no encontrado

        ResponseEntity<Usuario> response = usuarioController.getProfile("nonexistent");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Debería obtener recetas favoritas correctamente")
    void testObtenerRecetasFavoritas() {
        usuarioFake = new Usuario();
        usuarioFake.setUsername("userTest");
        Receta fav1 = new Receta();
        fav1.setIdReceta(1);
        Receta fav2 = new Receta();
        fav2.setIdReceta(2);
        usuarioFake.agregarRecetaFavorita(fav1);
        usuarioFake.agregarRecetaFavorita(fav2);

        Authentication auth = new UsernamePasswordAuthenticationToken("userTest", null);

        ResponseEntity<Set<Receta>> response = usuarioController.obtenerRecetasFavoritas(auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Set<Receta> body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
    }

    @Test
    @DisplayName("Debería retornar 404 si el usuario no existe en obtenerRecetasFavoritas")
    void testObtenerRecetasFavoritasUserNotFound() {
        usuarioFake = null; // Usuario no encontrado

        Authentication auth = new UsernamePasswordAuthenticationToken("nonexistent", null);

        ResponseEntity<Set<Receta>> response = usuarioController.obtenerRecetasFavoritas(auth);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Debería quitar receta de favoritos correctamente")
    void testQuitarFavorito() {
        usuarioFake = new Usuario();
        usuarioFake.setUsername("userTest");
        recetaFake = new Receta();
        recetaFake.setIdReceta(100);
        usuarioFake.agregarRecetaFavorita(recetaFake); // Agregamos primero

        Authentication auth = new UsernamePasswordAuthenticationToken("userTest", null);

        ResponseEntity<Map<String, String>> response = usuarioController.quitarRecetaFavorita(100, auth);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Receta quitada de favoritos exitosamente", response.getBody().get("message"));
    }

    @Test
    @DisplayName("Debería dar 404 al quitar favorito si la receta no existe")
    void testQuitarFavoritoNotFound() {
        usuarioFake = new Usuario();
        recetaFake = null; // Receta no encontrada
        Authentication auth = new UsernamePasswordAuthenticationToken("userTest", null);

        ResponseEntity<Map<String, String>> response = usuarioController.quitarRecetaFavorita(999, auth);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Debería dar 404 al quitar favorito si el usuario no existe")
    void testQuitarFavoritoUserNotFound() {
        usuarioFake = null; // Usuario no encontrado
        recetaFake = new Receta();
        Authentication auth = new UsernamePasswordAuthenticationToken("userTest", null);

        ResponseEntity<Map<String, String>> response = usuarioController.quitarRecetaFavorita(100, auth);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usuario no encontrado", response.getBody().get("message"));
    }
}