package com.example.sebo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.sebo.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    public List<Categoria> findAllByCategoriaContainingIgnoreCase(@Param("categoria") String categoria);
}
