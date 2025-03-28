package com.servidores.projeto.servidores.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servidores.projeto.servidores.dto.ServidorEfetivoRequestDTO;
import com.servidores.projeto.servidores.dto.ServidorEfetivoResponseDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/servidores-efetivos")
public class ServidorEfetivoController {

    @PostMapping
    public ResponseEntity<ServidorEfetivoResponseDTO> create(@RequestBody @Valid ServidorEfetivoRequestDTO dto) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServidorEfetivoResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServidorEfetivoResponseDTO> update(
            @PathVariable Long id, 
            @RequestBody @Valid ServidorEfetivoRequestDTO dto) {
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return ResponseEntity.notFound().build();
    }
}