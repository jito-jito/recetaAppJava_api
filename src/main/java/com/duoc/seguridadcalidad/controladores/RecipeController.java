package com.duoc.seguridadcalidad.controladores;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.duoc.seguridadcalidad.modelos.Comentario;
import com.duoc.seguridadcalidad.modelos.Receta;
import com.duoc.seguridadcalidad.modelos.RecetaRequestDto;
import com.duoc.seguridadcalidad.modelos.Valoracion;
import com.duoc.seguridadcalidad.repositorios.ComentarioRepository;
import com.duoc.seguridadcalidad.repositorios.RecetaRepository;
import com.duoc.seguridadcalidad.repositorios.ValoracionRepository;
import com.duoc.seguridadcalidad.servicios.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = "*")
public class RecipeController {

    private final RecetaRepository recetaRepository;
    private final ComentarioRepository comentarioRepository;
    private final ValoracionRepository valoracionRepository;
    private final ObjectMapper objectMapper;
    private final FileStorageService fileStorageService;

    // S6813: constructor injection instead of @Autowired field injection
    public RecipeController(RecetaRepository recetaRepository,
                            ComentarioRepository comentarioRepository,
                            ValoracionRepository valoracionRepository,
                            ObjectMapper objectMapper,
                            FileStorageService fileStorageService) {
        this.recetaRepository = recetaRepository;
        this.comentarioRepository = comentarioRepository;
        this.valoracionRepository = valoracionRepository;
        this.objectMapper = objectMapper;
        this.fileStorageService = fileStorageService;
    }

    // ------------------------------------------------------------------ queries

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

        return resolveSearch(titulo, tipoCocina, paisOrigen, tiempoMaximo, popularidadMinima);
    }

    // S3776: extracted search logic to reduce cognitive complexity of searchRecipes
    private Iterable<Receta> resolveSearch(String titulo, String tipoCocina,
                                           String paisOrigen, Integer tiempoMaximo,
                                           Double popularidadMinima) {
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

    @GetMapping("/mis-recetas")
    public Iterable<Receta> getMisRecetas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return recetaRepository.findByAutor(auth.getName());
    }

    // --------------------------------------------------------------- mutations

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Receta> createRecipe(
            @RequestPart("receta") String recetaJson,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenesFiles,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videosFiles) {

        try {
            // S4684: parse into DTO first, then map manually to the entity
            RecetaRequestDto dto = objectMapper.readValue(recetaJson, RecetaRequestDto.class);
            Receta receta = mapDtoToEntity(dto);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getName() != null) {
                receta.setAutor(auth.getName());
            }
            receta.setPublicada(false);

            storeMediaFiles(imagenesFiles, receta, "imagenes");
            storeMediaFiles(videosFiles, receta, "videos");

            Receta saved = recetaRepository.save(receta);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Receta> updateRecipe(
            @PathVariable("id") Integer id,
            @RequestPart("receta") String recetaJson,
            @RequestPart(value = "imagenes", required = false) List<MultipartFile> imagenesFiles,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videosFiles) {

        if (!recetaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            // S4684: parse into DTO, then merge into entity
            RecetaRequestDto dto = objectMapper.readValue(recetaJson, RecetaRequestDto.class);
            Receta receta = mapDtoToEntity(dto);
            receta.setIdReceta(id);

            storeMediaFiles(imagenesFiles, receta, "imagenes");
            storeMediaFiles(videosFiles, receta, "videos");

            Receta updated = recetaRepository.save(receta);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
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

    @PutMapping("/{id}/estado")
    public ResponseEntity<Receta> changePublishStatus(
            @PathVariable("id") Integer id,
            @RequestParam("publicada") boolean publicada) {

        Optional<Receta> optional = recetaRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Receta receta = optional.get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!auth.getName().equals(receta.getAutor())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        receta.setPublicada(publicada);
        Receta updated = recetaRepository.save(receta);
        return ResponseEntity.ok(updated);
    }

    // ------------------------------------------------------------ comentarios

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<Receta> addComentario(
            @PathVariable Integer id,
            @RequestBody Comentario payload) {          // Comentario is not a persistent entity here

        Optional<Receta> optReceta = recetaRepository.findById(id);
        if (optReceta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Receta receta = optReceta.get();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String autor = (auth != null && auth.getName() != null) ? auth.getName() : "Anonimo";

        if (payload.getTexto() == null || payload.getTexto().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Comentario comentario = new Comentario(payload.getTexto(), autor, receta);
        receta.getComentarios().add(comentario);
        Receta updated = recetaRepository.save(receta);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}/comentarios")
    public ResponseEntity<Iterable<Comentario>> getComentarios(@PathVariable Integer id) {
        Optional<Receta> optReceta = recetaRepository.findById(id);
        if (optReceta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(comentarioRepository.findByRecetaOrderByFechaDesc(optReceta.get()));
    }

    // ------------------------------------------------------------ valoraciones

    @PostMapping("/{id}/valoraciones")
    public ResponseEntity<Map<String, Object>> addValoracion( // S1452: replaced wildcard with concrete Map type
            @PathVariable Integer id,
            @RequestBody Valoracion payload) {

        Optional<Receta> optReceta = recetaRepository.findById(id);
        if (optReceta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Receta receta = optReceta.get();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String autor = (auth != null && auth.getName() != null) ? auth.getName() : "Anonimo";

        if (autor.equals(receta.getAutor())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "El autor no puede valorar su propia receta."));
        }

        Integer puntaje = payload.getPuntaje();
        if (puntaje == null || puntaje < 0 || puntaje > 5) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "El puntaje debe estar entre 0 y 5."));
        }

        Optional<Valoracion> optVal = valoracionRepository.findByRecetaAndAutor(receta, autor);
        if (optVal.isPresent()) {
            optVal.get().setPuntaje(puntaje);
        } else {
            receta.getValoraciones().add(new Valoracion(puntaje, autor, receta));
        }
        Receta updated = recetaRepository.save(receta);
        return ResponseEntity.ok(Map.of("receta", updated));
    }

    @GetMapping("/{id}/valoraciones")
    public ResponseEntity<Iterable<Valoracion>> getValoraciones(@PathVariable Integer id) {
        Optional<Receta> optReceta = recetaRepository.findById(id);
        if (optReceta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(valoracionRepository.findByReceta(optReceta.get()));
    }

    // ------------------------------------------------------ private helpers

    /** Maps a {@link RecetaRequestDto} to a new {@link Receta} entity. */
    private Receta mapDtoToEntity(RecetaRequestDto dto) {
        Receta receta = new Receta();
        receta.setTitulo(dto.getTitulo());
        receta.setTipoCocina(dto.getTipoCocina());
        receta.setPaisOrigen(dto.getPaisOrigen());
        receta.setDificultad(dto.getDificultad());
        receta.setTiempoCoccion(dto.getTiempoCoccion());
        receta.setInstrucciones(dto.getInstrucciones());
        receta.setPopularidad(dto.getPopularidad());
        receta.setFechaPublicacion(dto.getFechaPublicacion());
        return receta;
    }

    /** Stores uploaded media files and adds their URLs to the recipe. */
    private void storeMediaFiles(List<MultipartFile> files, Receta receta, String type) throws java.io.IOException {
        if (files == null) {
            return;
        }
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String url = fileStorageService.storeFile(file, type);
                if ("imagenes".equals(type)) {
                    receta.getImagenes().add(url);
                } else {
                    receta.getVideos().add(url);
                }
            }
        }
    }
}