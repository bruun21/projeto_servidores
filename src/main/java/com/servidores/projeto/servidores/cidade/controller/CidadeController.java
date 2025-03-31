package com.servidores.projeto.servidores.cidade.controller;

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

import com.servidores.projeto.servidores.cidade.dto.CidadeRequestDTO;
import com.servidores.projeto.servidores.cidade.dto.CidadeResponseDTO;
import com.servidores.projeto.servidores.cidade.service.CidadeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cidades")
@RequiredArgsConstructor
public class CidadeController {

    private final CidadeService cidadeService;

    @PostMapping
    public ResponseEntity<Long> criarCidade(@RequestBody CidadeRequestDTO requestDTO) {
        Long id = cidadeService.createCidade(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        CidadeResponseDTO responseDTO = cidadeService.getById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<CidadeResponseDTO>> listarTodas(Pageable pageable) {
        Page<CidadeResponseDTO> cidades = cidadeService.getAll(pageable);
        return ResponseEntity.ok(cidades);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CidadeResponseDTO> atualizarCidade(
            @PathVariable Long id,
            @RequestBody CidadeRequestDTO requestDTO) {
        CidadeResponseDTO cidadeAtualizada = cidadeService.update(id, requestDTO);
        return ResponseEntity.ok(cidadeAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCidade(@PathVariable Long id) {
        cidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}