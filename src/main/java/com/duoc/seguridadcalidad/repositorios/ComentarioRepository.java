package com.duoc.seguridadcalidad.repositorios;

import com.duoc.seguridadcalidad.modelos.Comentario;
import com.duoc.seguridadcalidad.modelos.Receta;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ComentarioRepository extends CrudRepository<Comentario, Integer> {
    List<Comentario> findByReceta(Receta receta);
    List<Comentario> findByRecetaOrderByFechaDesc(Receta receta);
}
