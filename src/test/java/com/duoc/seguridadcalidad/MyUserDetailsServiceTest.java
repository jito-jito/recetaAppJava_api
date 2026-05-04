package com.duoc.seguridadcalidad;

import com.duoc.seguridadcalidad.modelos.Usuario;
import com.duoc.seguridadcalidad.repositorios.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MyUserDetailsService Tests")
public class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService userDetailsService;

    @Test
    @DisplayName("Debería cargar usuario por nombre de usuario existente")
    void testLoadUserByUsernameSuccess() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setUsername("juan");
        usuario.setPassword("password123");

        when(userRepository.findByUsername("juan")).thenReturn(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername("juan");

        assertNotNull(userDetails);
        assertEquals("juan", userDetails.getUsername());
        verify(userRepository, times(1)).findByUsername("juan");
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando usuario no existe")
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByUsername("noexiste")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, 
            () -> userDetailsService.loadUserByUsername("noexiste"));

        verify(userRepository, times(1)).findByUsername("noexiste");
    }

    @Test
    @DisplayName("Debería retornar la instancia del usuario como UserDetails")
    void testLoadUserByUsernameReturnsCorrectType() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(2);
        usuario.setUsername("maria");
        usuario.setPassword("pass456");

        when(userRepository.findByUsername("maria")).thenReturn(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername("maria");

        assertInstanceOf(Usuario.class, userDetails);
        assertEquals(usuario, userDetails);
    }

    @Test
    @DisplayName("Debería manejar múltiples búsquedas de usuarios diferentes")
    void testMultipleDifferentUsers() {
        Usuario usuario1 = new Usuario();
        usuario1.setUsername("usuario1");
        usuario1.setPassword("pass1");

        Usuario usuario2 = new Usuario();
        usuario2.setUsername("usuario2");
        usuario2.setPassword("pass2");

        when(userRepository.findByUsername("usuario1")).thenReturn(usuario1);
        when(userRepository.findByUsername("usuario2")).thenReturn(usuario2);

        UserDetails details1 = userDetailsService.loadUserByUsername("usuario1");
        UserDetails details2 = userDetailsService.loadUserByUsername("usuario2");

        assertEquals("usuario1", details1.getUsername());
        assertEquals("usuario2", details2.getUsername());
        assertNotEquals(details1.getUsername(), details2.getUsername());
    }

    @Test
    @DisplayName("Debería lanzar excepción con el username en el mensaje")
    void testExceptionMessageIncludesUsername() {
        when(userRepository.findByUsername("usuarioInexistente")).thenReturn(null);

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, 
            () -> userDetailsService.loadUserByUsername("usuarioInexistente"));

        assertTrue(exception.getMessage().contains("usuarioInexistente"));
    }

    @Test
    @DisplayName("Debería buscar el usuario exactamente una vez")
    void testRepositoryCalledExactlyOnce() {
        Usuario usuario = new Usuario();
        usuario.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(usuario);

        userDetailsService.loadUserByUsername("test");

        verify(userRepository, times(1)).findByUsername("test");
    }

    @Test
    @DisplayName("Debería retornar usuario con credenciales correctas")
    void testLoadUserWithValidCredentials() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(5);
        usuario.setUsername("chef");
        usuario.setPassword("secreto");

        when(userRepository.findByUsername("chef")).thenReturn(usuario);

        UserDetails userDetails = userDetailsService.loadUserByUsername("chef");

        assertEquals("chef", userDetails.getUsername());
        assertEquals(usuario.getPassword(), userDetails.getPassword());
    }

    @Test
    @DisplayName("Debería ser sensible a mayúsculas y minúsculas en el username")
    void testUsernameCaseSensitive() {
        Usuario usuario = new Usuario();
        usuario.setUsername("JuanPerez");

        when(userRepository.findByUsername("JuanPerez")).thenReturn(usuario);
        when(userRepository.findByUsername("juanperez")).thenReturn(null);

        UserDetails details = userDetailsService.loadUserByUsername("JuanPerez");
        assertEquals("JuanPerez", details.getUsername());

        assertThrows(UsernameNotFoundException.class, 
            () -> userDetailsService.loadUserByUsername("juanperez"));
    }
}





