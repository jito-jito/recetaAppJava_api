package com.duoc.seguridadcalidad.controladores;

import com.duoc.seguridadcalidad.JWTAuthenticationConfig;
import com.duoc.seguridadcalidad.MyUserDetailsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final JWTAuthenticationConfig jwtAuthenticationConfig;
    private final MyUserDetailsService userDetailsService;

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    // S6813: constructor injection
    public LoginController(JWTAuthenticationConfig jwtAuthenticationConfig,
                           MyUserDetailsService userDetailsService) {
        this.jwtAuthenticationConfig = jwtAuthenticationConfig;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("login")
    public String login(@RequestBody LoginRequest loginRequest) {

        logger.info("Recibida solicitud de login para usuario: {}", loginRequest.getUsername());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        logger.info("Usuario encontrado en la base de datos: {}", userDetails.getUsername());

        // S112: use specific exception instead of RuntimeException
        if (!userDetails.getPassword().equals(loginRequest.getPassword())) {
            throw new BadCredentialsException("Invalid login");
        }

        // S1488: return expression directly instead of assigning to temporary variable
        return jwtAuthenticationConfig.getJWTToken(loginRequest.getUsername());
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }

}