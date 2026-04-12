package com.duoc.seguridadcalidad.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.duoc.seguridadcalidad.modelos.Receta;
import com.duoc.seguridadcalidad.repositorios.RecetaRepository;

@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = "*") // Para permitir peticiones desde el frontend
public class RecipeController {

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Autowired
    private com.duoc.seguridadcalidad.servicios.FileStorageService fileStorageService;

    @GetMapping
    public Iterable<Receta> getAllRecipes() {
        return recetaRepository.findByPublicadaTrue();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receta> getRecipeById(@PathVariable("id") Integer id) {
        Optional<Receta> receta = recetaRepository.findById(id);
        return receta.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public Iterable<Receta> searchRecipes(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String tipoCocina,
            @RequestParam(required = false) String paisOrigen,
            @RequestParam(required = false) Integer tiempoMaximo,
            @RequestParam(required = false) Double popularidadMinima) {

        if (titulo != null && !titulo.isBlank()) {
            return recetaRepository.findByTituloContainingIgnoreCaseAndPublicadaTrue(titulo);
        }
        if (tipoCocina != null && !tipoCocina.isBlank()) {
            return recetaRepository.findByTipoCocinaContainingIgnoreCaseAndPublicadaTrue(tipoCocina);
        }
        if (paisOrigen != null && !paisOrigen.isBlank()) {
            return recetaRepository.findByPaisOrigenContainingIgnoreCaseAndPublicadaTrue(paisOrigen);
        }
        if (tiempoMaximo != null) {
            return recetaRepository.findByTiempoCoccionLessThanEqualAndPublicadaTrue(tiempoMaximo);
        }
        if (popularidadMinima != null) {
            return recetaRepository.findByPopularidadGreaterThanEqualAndPublicadaTrue(popularidadMinima);
        }

        return recetaRepository.findByPublicadaTrue();
    }

    @GetMapping("/populares")
    public List<Receta> getRecipesByPopularity() {
        return recetaRepository.findAllByPublicadaTrueOrderByPopularidadDesc();
    }

    @GetMapping("/recientes")
    public List<Receta> getRecentRecipes() {
        return recetaRepository.findAllByPublicadaTrueOrderByFechaPublicacionDesc();
    }

    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Receta> createRecipe(
            @RequestPart("receta") String recetaJson,
            @RequestPart(value = "imagenes", required = false) List<org.springframework.web.multipart.MultipartFile> imagenesFiles,
            @RequestPart(value = "videos", required = false) List<org.springframework.web.multipart.MultipartFile> videosFiles) {

        try {
            Receta receta = objectMapper.readValue(recetaJson, Receta.class);
            if (receta.getIdReceta() != null) {
                receta.setIdReceta(null);
            }

            org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null) {
                receta.setAutor(auth.getName());
            }
            receta.setPublicada(false);

            if (imagenesFiles != null) {
                for (org.springframework.web.multipart.MultipartFile file : imagenesFiles) {
                    if (!file.isEmpty()) {
                        String url = fileStorageService.storeFile(file, "imagenes");
                        receta.getImagenes().add(url);
                    }
                }
            }

            if (videosFiles != null) {
                for (org.springframework.web.multipart.MultipartFile file : videosFiles) {
                    if (!file.isEmpty()) {
                        String url = fileStorageService.storeFile(file, "videos");
                        receta.getVideos().add(url);
                    }
                }
            }

            Receta saved = recetaRepository.save(receta);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Receta> updateRecipe(
            @PathVariable("id") Integer id,
            @RequestPart("receta") String recetaJson,
            @RequestPart(value = "imagenes", required = false) List<org.springframework.web.multipart.MultipartFile> imagenesFiles,
            @RequestPart(value = "videos", required = false) List<org.springframework.web.multipart.MultipartFile> videosFiles) {

        if (!recetaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Receta receta = objectMapper.readValue(recetaJson, Receta.class);
            receta.setIdReceta(id);

            if (imagenesFiles != null) {
                for (org.springframework.web.multipart.MultipartFile file : imagenesFiles) {
                    if (!file.isEmpty()) {
                        String url = fileStorageService.storeFile(file, "imagenes");
                        receta.getImagenes().add(url);
                    }
                }
            }

            if (videosFiles != null) {
                for (org.springframework.web.multipart.MultipartFile file : videosFiles) {
                    if (!file.isEmpty()) {
                        String url = fileStorageService.storeFile(file, "videos");
                        receta.getVideos().add(url);
                    }
                }
            }

            Receta updated = recetaRepository.save(receta);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable("id") Integer id) {
        if (!recetaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        recetaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mis-recetas")
    public Iterable<Receta> getMisRecetas() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        return recetaRepository.findByAutor(auth.getName());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Receta> changePublishStatus(@PathVariable("id") Integer id, @RequestParam("publicada") boolean publicada) {
        Optional<Receta> optional = recetaRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Receta receta = optional.get();
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        
        if (!auth.getName().equals(receta.getAutor())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        receta.setPublicada(publicada);
        Receta updated = recetaRepository.save(receta);
        return ResponseEntity.ok(updated);
    }
}