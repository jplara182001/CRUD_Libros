package com.api.libros.Entities;

import java.sql.Date;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="libros")
public class LibrosEntity {

    @Id
    private UUID id;
    private String titulo;
    private String autor;
    private String genero;
    private Date fechaPublicacion;

}
