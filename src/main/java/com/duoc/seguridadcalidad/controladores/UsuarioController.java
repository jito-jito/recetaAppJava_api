package com.duoc.seguridadcalidad.controladores;

import com.duoc.seguridadcalidad.modelos.Usuario;
import com.duoc.seguridadcalidad.modelos.Receta;
import com.duoc.seguridadcalidad.repositorios.UserRepository;
import com.duoc.seguridadcalidad.repositorios.RecetaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    // S1192: constant for repeated literal "message"
    private static final String MSG_KEY = "message";

    private final UserRepository userRepository;
    private final RecetaRepository recetaRepository;
    private final PasswordEncoder passwordEncoder;

    // S6813: constructor injection
    public UsuarioController(UserRepository userRepository,
                             RecetaRepository recetaRepository,
                             PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.recetaRepository = recetaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        Map<String, String> response = new HashMap<>();

        try {
            if (userRepository.findByUsername(request.getUsername()) != null) {
                response.put(MSG_KEY, "Username already exists");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            Usuario newUser = new Usuario();
            newUser.setUsername(request.getUsername());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setEstaAutenticado(true);

            userRepository.save(newUser);

            response.put(MSG_KEY, "User registered successfully");
            response.put("username", newUser.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            response.put(MSG_KEY, "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Usuario> getProfile(@RequestParam String username) {
        Usuario user = userRepository.findByUsername(username);
        if (user != null) {
            Usuario safeUser = new Usuario();
            safeUser.setIdUsuario(user.getIdUsuario());
            safeUser.setUsername(user.getUsername());
            safeUser.setEmail(user.getEmail());
            safeUser.setEstaAutenticado(user.getEstaAutenticado());
            return ResponseEntity.ok(safeUser);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/favoritos/{recetaId}")
    public ResponseEntity<Map<String, String>> agregarRecetaFavorita(
            @PathVariable Integer recetaId,
            Authentication authentication) {

        Map<String, String> response = new HashMap<>();

        try {
            String username = authentication.getName();
            Usuario usuario = userRepository.findByUsername(username);

            if (usuario == null) {
                response.put(MSG_KEY, "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Optional<Receta> recetaOpt = recetaRepository.findById(recetaId);
            if (!recetaOpt.isPresent()) {
                response.put(MSG_KEY, "Receta no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Receta receta = recetaOpt.get();
            usuario.agregarRecetaFavorita(receta);
            userRepository.save(usuario);

            response.put(MSG_KEY, "Receta agregada a favoritos exitosamente");
            response.put("recetaId", recetaId.toString());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put(MSG_KEY, "Error al agregar receta a favoritos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/favoritos")
    public ResponseEntity<Set<Receta>> obtenerRecetasFavoritas(Authentication authentication) {

        try {
            String username = authentication.getName();
            Usuario usuario = userRepository.findByUsername(username);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Set<Receta> recetasFavoritas = usuario.getRecetasFavoritas();
            return ResponseEntity.ok(recetasFavoritas);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/favoritos/{recetaId}")
    public ResponseEntity<Map<String, String>> quitarRecetaFavorita(
            @PathVariable Integer recetaId,
            Authentication authentication) {

        Map<String, String> response = new HashMap<>();

        try {
            String username = authentication.getName();
            Usuario usuario = userRepository.findByUsername(username);

            if (usuario == null) {
                response.put(MSG_KEY, "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Optional<Receta> recetaOpt = recetaRepository.findById(recetaId);
            if (!recetaOpt.isPresent()) {
                response.put(MSG_KEY, "Receta no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Receta receta = recetaOpt.get();
            usuario.quitarRecetaFavorita(receta);
            userRepository.save(usuario);

            response.put(MSG_KEY, "Receta quitada de favoritos exitosamente");
            response.put("recetaId", recetaId.toString());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put(MSG_KEY, "Error al quitar receta de favoritos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    }
}