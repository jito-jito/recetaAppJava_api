package com.duoc.seguridadcalidad.modelos;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testConstructorAndGettersSetters() {
        Usuario usuario = new Usuario(1, "testuser", "test@example.com", "password", true);

        assertEquals(1, usuario.getIdUsuario());
        assertEquals("testuser", usuario.getUsername());
        assertEquals("test@example.com", usuario.getEmail());
        assertEquals("password", usuario.getPassword());
        assertTrue(usuario.getEstaAutenticado());
    }

    @Test
    void testAgregarRecetaFavorita() {
        Usuario usuario = new Usuario();
        Receta receta = new Receta();
        receta.setIdReceta(1);

        usuario.agregarRecetaFavorita(receta);

        Set<Receta> favoritos = usuario.getRecetasFavoritas();
        assertTrue(favoritos.contains(receta));
        assertEquals(1, favoritos.size());
    }

    @Test
    void testQuitarRecetaFavorita() {
        Usuario usuario = new Usuario();
        Receta receta = new Receta();
        receta.setIdReceta(1);
        usuario.agregarRecetaFavorita(receta);

        usuario.quitarRecetaFavorita(receta);

        Set<Receta> favoritos = usuario.getRecetasFavoritas();
        assertFalse(favoritos.contains(receta));
        assertEquals(0, favoritos.size());
    }

    @Test
    void testUserDetailsMethods() {
        Usuario usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setEstaAutenticado(true);

        assertEquals("testuser", usuario.getUsername());
        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
        assertTrue(usuario.isEnabled());

        assertNotNull(usuario.getAuthorities());
        assertEquals(1, usuario.getAuthorities().size());
        GrantedAuthority authority = usuario.getAuthorities().iterator().next();
        assertEquals("ROLE_USER", authority.getAuthority());
    }

    @Test
    void testIsEnabledWhenNotAutenticado() {
        Usuario usuario = new Usuario();
        usuario.setEstaAutenticado(false);

        assertFalse(usuario.isEnabled());
    }

    @Test
    void testToString() {
        Usuario usuario = new Usuario(1, "testuser", "test@example.com", "password", true);
        String expected = "Usuario{idUsuario=1, username='testuser', email='test@example.com', password='password', estaAutenticado=true}";
        assertEquals(expected, usuario.toString());
    }
}
