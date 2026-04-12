package com.duoc.seguridadcalidad.modelos;

import com.duoc.seguridadcalidad.modelos.Ingrediente;
import com.duoc.seguridadcalidad.modelos.Dificultad;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReceta;

    @Column
    private String autor;

    @Column
    private boolean publicada = false;

    @Column
    private String titulo;
    private String tipoCocina; // mediterranea, vegana, japonesa, casera, etc
    private String paisOrigen;

    @Enumerated(EnumType.STRING)
    private Dificultad dificultad; // BAJA, MEDIA, ALTA

    private Integer tiempoCoccion; // en minutos

    @Column(length = 2000)
    private String instrucciones; // paso a paso de la preparacion

    private Double popularidad; // ranking o numero de visualizaciones
    private LocalDate fechaPublicacion; // para tener "mas recientes"

    @ElementCollection
    @CollectionTable(name = "receta_imagenes")
    @Column(name = "imagen_url")
    private List<String> imagenes = new ArrayList<>(); // lista de urls a las fotos de la receta

    @ElementCollection
    @CollectionTable(name = "receta_videos")
    @Column(name = "video_url")
    private List<String> videos = new ArrayList<>(); // lista de urls a los videos de la receta

    @ElementCollection
    @CollectionTable(name = "receta_ingredientes")
    private List<Ingrediente> ingredientes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Valoracion> valoraciones = new ArrayList<>();

    public Receta() {
    }

    public Receta(Integer idReceta, String titulo, String tipoCocina, String paisOrigen, Dificultad dificultad,
            Integer tiempoCoccion, String instrucciones, Double popularidad, LocalDate fechaPublicacion,
            List<String> imagenes, List<String> videos, String autor, boolean publicada, Ingrediente... ingredientes) {
        this.idReceta = idReceta;
        this.titulo = titulo;
        this.tipoCocina = tipoCocina;
        this.paisOrigen = paisOrigen;
        this.dificultad = dificultad;
        this.tiempoCoccion = tiempoCoccion;
        this.instrucciones = instrucciones;
        this.popularidad = popularidad;
        this.fechaPublicacion = fechaPublicacion;
        this.autor = autor;
        this.publicada = publicada;
        this.imagenes = imagenes != null ? imagenes : new ArrayList<>();
        this.videos = videos != null ? videos : new ArrayList<>();
        this.ingredientes = List.of(ingredientes);
    }

    // Getters and Setters
    public Integer getIdReceta() {
        return idReceta;
    }

    public void setIdReceta(Integer idReceta) {
        this.idReceta = idReceta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipoCocina() {
        return tipoCocina;
    }

    public void setTipoCocina(String tipoCocina) {
        this.tipoCocina = tipoCocina;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public Dificultad getDificultad() {
        return dificultad;
    }

    public void setDificultad(Dificultad dificultad) {
        this.dificultad = dificultad;
    }

    public Integer getTiempoCoccion() {
        return tiempoCoccion;
    }

    public void setTiempoCoccion(Integer tiempoCoccion) {
        this.tiempoCoccion = tiempoCoccion;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public Double getPopularidad() {
        return popularidad;
    }

    public void setPopularidad(Double popularidad) {
        this.popularidad = popularidad;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public boolean isPublicada() {
        return publicada;
    }

    public void setPublicada(boolean publicada) {
        this.publicada = publicada;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }

    public void setValoraciones(List<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }

    @JsonProperty("puntajePromedio")
    public Double getPuntajePromedio() {
        if (valoraciones == null || valoraciones.isEmpty()) return 0.0;
        return valoraciones.stream()
                .filter(v -> v.getPuntaje() != null)
                .mapToInt(Valoracion::getPuntaje)
                .average()
                .orElse(0.0);
    }

    @JsonProperty("cantidadComentarios")
    public Integer getCantidadComentarios() {
        return comentarios == null ? 0 : comentarios.size();
    }

    @Override
    public String toString() {
        return "Receta{" +
                "idReceta=" + idReceta +
                ", titulo='" + titulo + '\'' +
                ", tipoCocina='" + tipoCocina + '\'' +
                ", paisOrigen='" + paisOrigen + '\'' +
                ", dificultad=" + dificultad +
                ", tiempoCoccion=" + tiempoCoccion +
                ", instrucciones='" + instrucciones + '\'' +
                ", popularidad=" + popularidad +
                ", fechaPublicacion=" + fechaPublicacion +
                ", autor='" + autor + '\'' +
                ", publicada=" + publicada +
                ", imagenes=" + imagenes +
                ", videos=" + videos +
                ", ingredientes=" + ingredientes +
                '}';
    }
}
