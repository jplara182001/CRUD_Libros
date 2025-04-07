package com.api.libros.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.libros.Entities.LibrosEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LibrosRepository extends JpaRepository<LibrosEntity, UUID> {

    Page<LibrosEntity> findAllByTituloContainingIgnoreCase(String titulo, Pageable pageable);


}
