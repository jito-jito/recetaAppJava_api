package com.duoc.seguridadcalidad.modelos;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

@Entity
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column
    private String username;
    private String email;
    private String password;
    private Boolean estaAutenticado;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "usuario_recetas_favoritas",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "receta_id")
    )
    private transient Set<Receta> recetasFavoritas = new HashSet<>(); // S1948: transient

    public Usuario() {
    }

    public Usuario(Integer idUsuario, String username, String email, String password, Boolean estaAutenticado) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.email = email;
        this.password = password;
        this.estaAutenticado = estaAutenticado;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

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

    public Boolean getEstaAutenticado() {
        return estaAutenticado;
    }

    public void setEstaAutenticado(Boolean estaAutenticado) {
        this.estaAutenticado = estaAutenticado;
    }
    
    public Set<Receta> getRecetasFavoritas() {
        return recetasFavoritas;
    }
    
    public void setRecetasFavoritas(Set<Receta> recetasFavoritas) {
        this.recetasFavoritas = recetasFavoritas;
    }
    
    public void agregarRecetaFavorita(Receta receta) {
        this.recetasFavoritas.add(receta);
    }
    
    public void quitarRecetaFavorita(Receta receta) {
        this.recetasFavoritas.remove(receta);
    }

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return estaAutenticado == null || estaAutenticado; // S1125: removed unnecessary boolean literal
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", estaAutenticado=" + estaAutenticado +
                '}';
    }
}
