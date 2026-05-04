package com.duoc.seguridadcalidad.controladores;

import com.duoc.seguridadcalidad.JWTAuthenticationConfig;
import com.duoc.seguridadcalidad.MyUserDetailsService;
import com.duoc.seguridadcalidad.repositorios.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LoginControllerTest {

    private LoginController loginController;
    private StubUserDetailsService stubUserDetailsService;
    private StubJWTConfig stubJWTConfig;

    // --- STUBS MANUALES ---

    private class StubUserDetailsService extends MyUserDetailsService {
        // Al heredar, debemos llamar al constructor del padre pasándole un Mock o null
        public StubUserDetailsService(UserRepository userRepository) {
            super(userRepository);
        }

        @Override
        public UserDetails loadUserByUsername(String username) {
            if ("admin".equals(username)) {
                return new User("admin", "1234", new ArrayList<>());
            }
            return null;
        }
    }

    private class StubJWTConfig extends JWTAuthenticationConfig {
        @Override
        public String getJWTToken(String username) {
            return "token-manual-123";
        }
    }

    @BeforeEach
    void setUp() {
        // Creamos un mock simple del repositorio solo para satisfacer el constructor
        // Como no lo usaremos realmente (el Stub sobrescribe el método), no dará error de modificación de clases
        UserRepository mockRepo = Mockito.mock(UserRepository.class);

        stubUserDetailsService = new StubUserDetailsService(mockRepo);
        stubJWTConfig = new StubJWTConfig();

        loginController = new LoginController(stubJWTConfig, stubUserDetailsService);
    }

    @Test
    @DisplayName("Debería retornar token cuando el login es correcto")
    void testLoginExitoso() {
        LoginController.LoginRequest request = new LoginController.LoginRequest();
        request.setUsername("admin");
        request.setPassword("1234");

        String resultado = loginController.login(request);

        assertEquals("token-manual-123", resultado);
    }

    @Test
    @DisplayName("Debería lanzar BadCredentialsException cuando la password es incorrecta")
    void testLoginPasswordIncorrecta() {
        LoginController.LoginRequest request = new LoginController.LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrong-pass");

        assertThrows(BadCredentialsException.class, () -> {
            loginController.login(request);
        });
    }

    @Test
    @DisplayName("Debería lanzar NullPointerException cuando el usuario no existe")
    void testLoginUsuarioNoExiste() {
        LoginController.LoginRequest request = new LoginController.LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("1234");

        assertThrows(NullPointerException.class, () -> {
            loginController.login(request);
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando payload del login está incompleto")
    void testLoginRequestIncompleto() {
        LoginController.LoginRequest request = new LoginController.LoginRequest();
        request.setUsername(null);
        request.setPassword("1234");

        assertThrows(NullPointerException.class, () -> {
            loginController.login(request);
        });
    }
}