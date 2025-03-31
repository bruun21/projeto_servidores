package com.servidores.projeto.servidores.unidade.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.servidores.projeto.servidores.unidade.dto.UnidadeRequestDTO;
import com.servidores.projeto.servidores.unidade.dto.UnidadeResponseDTO;
import com.servidores.projeto.servidores.unidade.service.UnidadeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/unidade")
@RequiredArgsConstructor
public class UnidadeController {

    private final UnidadeService unidadeService;

    @PostMapping
    public ResponseEntity<Long> createUnidade(@RequestBody UnidadeRequestDTO requestDTO) {
        Long id = unidadeService.create(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadeResponseDTO> getUnidadeById(@PathVariable Long id) {
        UnidadeResponseDTO responseDTO = unidadeService.getById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<UnidadeResponseDTO>> getAllUnidades(Pageable pageable) {
        Page<UnidadeResponseDTO> unidades = unidadeService.getAll(pageable);
        return ResponseEntity.ok(unidades);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponseDTO> updateUnidade(
            @PathVariable Long id,
            @RequestBody UnidadeRequestDTO requestDTO) {
        UnidadeResponseDTO updatedUnidade = unidadeService.update(id, requestDTO);
        return ResponseEntity.ok(updatedUnidade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnidade(@PathVariable Long id) {
        unidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}