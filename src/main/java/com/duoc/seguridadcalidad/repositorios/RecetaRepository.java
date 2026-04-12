package com.duoc.seguridadcalidad.repositorios;

import org.springframework.data.repository.CrudRepository;
import com.duoc.seguridadcalidad.modelos.Receta;
import com.duoc.seguridadcalidad.modelos.Dificultad;

import java.util.List;

public interface RecetaRepository extends CrudRepository<Receta, Integer> {

    List<Receta> findByTituloContainingIgnoreCaseAndPublicadaTrue(String titulo);

    List<Receta> findByTipoCocinaContainingIgnoreCaseAndPublicadaTrue(String tipoCocina);

    List<Receta> findByPaisOrigenContainingIgnoreCaseAndPublicadaTrue(String paisOrigen);

    List<Receta> findByDificultadAndPublicadaTrue(Dificultad dificultad);

    List<Receta> findByTiempoCoccionLessThanEqualAndPublicadaTrue(Integer tiempoCoccion);

    List<Receta> findByPopularidadGreaterThanEqualAndPublicadaTrue(Double popularidad);

    List<Receta> findAllByPublicadaTrueOrderByFechaPublicacionDesc();

    List<Receta> findAllByPublicadaTrueOrderByPopularidadDesc();

    List<Receta> findByPublicadaTrue();

    List<Receta> findByAutor(String autor);

}
