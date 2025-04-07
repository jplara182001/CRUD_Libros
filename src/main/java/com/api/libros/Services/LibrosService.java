package com.api.libros.Services;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.Collections;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.api.libros.Entities.LibrosEntity;
import com.api.libros.Repositories.LibrosRepository;

@Service
public class LibrosService {

    private final LibrosRepository librosRepository;

    public LibrosService(LibrosRepository librosRepository){
        this.librosRepository = librosRepository;
    }

    public ResponseEntity<?> getAllLibros(int page, int size, String[] sortParams) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sortParams)));
            Page<LibrosEntity> libros = librosRepository.findAll(pageable);
            return buildPaginatedResponse(libros);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid sorting direction. Use 'asc' or 'desc'.");
        }
    }

    public ResponseEntity<Map<String, Object>> getLibroById(UUID id) {
        Optional<LibrosEntity> libro = librosRepository.findById(id);
        return libro.map(value -> ResponseEntity.ok(Collections.singletonMap("Libro", (Object) value)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                                                   .body(Collections.singletonMap("Status", "Libro no encontrado.")));
    }    


    public ResponseEntity<?> getLibrosByTitulo(String titulo, int page, int size, String[] sortParams) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(parseSort(sortParams)));
        Page<LibrosEntity> libros = librosRepository.findAllByTituloContainingIgnoreCase(titulo, pageable);
        return buildPaginatedResponse(libros);
    }

    public ResponseEntity<?> addLibro(LibrosEntity libroToAdd) {
        libroToAdd.setId(UUID.randomUUID());
        LibrosEntity saved = librosRepository.save(libroToAdd);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(Collections.singletonMap("Status", "Libro agregado con ID " + saved.getId()));
    }

    public ResponseEntity<?> updateLibro(UUID id, LibrosEntity libroToUpdate) {
        Optional<LibrosEntity> libro = librosRepository.findById(id);
        if (libro.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Collections.singletonMap("Status", "Libro no encontrado."));
        }

        LibrosEntity existing = libro.get();
        existing.setTitulo(libroToUpdate.getTitulo());
        existing.setAutor(libroToUpdate.getAutor());
        existing.setGenero(libroToUpdate.getGenero());
        existing.setFechaPublicacion(libroToUpdate.getFechaPublicacion());

        librosRepository.save(existing);
        return ResponseEntity.ok(Collections.singletonMap("Status", "Libro actualizado."));
    }

    public ResponseEntity<?> deleteLibro(UUID id) {
        if (!librosRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(Collections.singletonMap("Status", "Libro no encontrado."));
        }

        librosRepository.deleteById(id);
        return ResponseEntity.ok(Collections.singletonMap("Status", "Libro eliminado."));
    }

    private Sort.Order parseSort(String[] sortParams) {
        if (sortParams.length < 2) {
            throw new IllegalArgumentException("Debe incluir campo y dirección (e.g., titulo,asc)");
        }

        String property = sortParams[0];
        String direction = sortParams[1].toLowerCase();

        if (!direction.equals("asc") && !direction.equals("desc")) {
            throw new IllegalArgumentException("Dirección de ordenamiento inválida.");
        }

        return new Sort.Order(Sort.Direction.fromString(direction), property);
    }

    private ResponseEntity<?> buildPaginatedResponse(Page<LibrosEntity> page) {
        Map<String, Object> response = new HashMap<>();
        response.put("TotalElements", page.getTotalElements());
        response.put("TotalPages", page.getTotalPages());
        response.put("CurrentPage", page.getNumber());
        response.put("NumberOfElements", page.getNumberOfElements());
        response.put("Libros", page.getContent());
        return ResponseEntity.ok(response);
    }

}
