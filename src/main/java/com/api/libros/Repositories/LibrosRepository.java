package com.api.libros.Repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.libros.Entities.LibrosEntity;

public interface LibrosRepository extends JpaRepository<LibrosEntity, UUID> {

}
