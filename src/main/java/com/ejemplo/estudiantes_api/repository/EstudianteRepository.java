package com.ejemplo.estudiantes_api.repository;

import com.ejemplo.estudiantes_api.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    // Derived Query: Comprueba existencia ignorando mayúsculas/minúsculas
    boolean existsByCorreoIgnoreCase(String correo);

    // Derived Query: Búsqueda por subcadena insensible a mayúsculas
    List<Estudiante> findByProgramaContainingIgnoreCase(String programa);

    // Consulta personalizada equivalente mediante JPQL
    @Query("SELECT e FROM Estudiante e WHERE LOWER(e.programa) = LOWER(:programa)")
    List<Estudiante> buscarPorProgramaExacto(@Param("programa") String programa);
}