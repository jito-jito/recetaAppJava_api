package com.duoc.seguridadcalidad.modelos;

/**
 * DTO used for receiving recipe creation/update data from the frontend.
 * Separates the HTTP request body from the JPA entity (S4684).
 */
public class RecetaRequestDto {

    private String titulo;
    private String tipoCocina;
    private String paisOrigen;
    private Dificultad dificultad;
    private Integer tiempoCoccion;
    private String instrucciones;
    private Double popularidad;
    private java.time.LocalDate fechaPublicacion;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTipoCocina() { return tipoCocina; }
    public void setTipoCocina(String tipoCocina) { this.tipoCocina = tipoCocina; }

    public String getPaisOrigen() { return paisOrigen; }
    public void setPaisOrigen(String paisOrigen) { this.paisOrigen = paisOrigen; }

    public Dificultad getDificultad() { return dificultad; }
    public void setDificultad(Dificultad dificultad) { this.dificultad = dificultad; }

    public Integer getTiempoCoccion() { return tiempoCoccion; }
    public void setTiempoCoccion(Integer tiempoCoccion) { this.tiempoCoccion = tiempoCoccion; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }

    public Double getPopularidad() { return popularidad; }
    public void setPopularidad(Double popularidad) { this.popularidad = popularidad; }

    public java.time.LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(java.time.LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
}
