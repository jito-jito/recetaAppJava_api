package com.duoc.seguridadcalidad.repositorios;

import com.duoc.seguridadcalidad.modelos.Receta;
import com.duoc.seguridadcalidad.modelos.Valoracion;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import java.util.List;

public interface ValoracionRepository extends CrudRepository<Valoracion, Integer> {
    Optional<Valoracion> findByRecetaAndAutor(Receta receta, String autor);
    List<Valoracion> findByReceta(Receta receta);
}
