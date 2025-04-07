package com.api.libros.Controllers;

import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.libros.Entities.LibrosEntity;
import com.api.libros.Services.LibrosService;

@RestController
@RequestMapping("/api/v0/libros")
public class LibrosController {

    private final LibrosService librosService;

    public LibrosController(LibrosService librosService) {
        this.librosService = librosService;
    }

    @GetMapping
    public ResponseEntity<?> getAllLibros(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "titulo,asc") String[] sort) {
        return librosService.getAllLibros(page, size, sort);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLibroById(@PathVariable UUID id) {
        return librosService.getLibroById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getLibrosByTitulo(
            @RequestParam String titulo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "titulo,asc") String[] sort) {
        return librosService.getLibrosByTitulo(titulo, page, size, sort);
    }

    @PostMapping
    public ResponseEntity<?> insertLibro(@Valid @RequestBody LibrosEntity libro) {
        return librosService.addLibro(libro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLibro(@PathVariable UUID id, @Valid @RequestBody LibrosEntity libro) {
        return librosService.updateLibro(id, libro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLibro(@PathVariable UUID id) {
        return librosService.deleteLibro(id);
    }

}
