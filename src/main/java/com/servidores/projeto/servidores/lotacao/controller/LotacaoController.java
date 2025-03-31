package com.servidores.projeto.servidores.lotacao.controller;

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

import com.servidores.projeto.servidores.lotacao.dto.LotacaoRequestDTO;
import com.servidores.projeto.servidores.lotacao.dto.LotacaoResponseDTO;
import com.servidores.projeto.servidores.lotacao.service.LotacaoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lotacoes")
@RequiredArgsConstructor
public class LotacaoController {

    private final LotacaoService lotacaoService;

    @PostMapping
    public ResponseEntity<Long> criarLotacao(@RequestBody LotacaoRequestDTO requestDTO) {
        Long id = lotacaoService.create(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LotacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        LotacaoResponseDTO responseDTO = lotacaoService.getById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<LotacaoResponseDTO>> listarTodos(Pageable pageable) {
        Page<LotacaoResponseDTO> lotacoes = lotacaoService.getAll(pageable);
        return ResponseEntity.ok(lotacoes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LotacaoResponseDTO> atualizarLotacao(
            @PathVariable Long id,
            @RequestBody LotacaoRequestDTO requestDTO) {
        LotacaoResponseDTO lotacaoAtualizada = lotacaoService.update(id, requestDTO);
        return ResponseEntity.ok(lotacaoAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirLotacao(@PathVariable Long id) {
        lotacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}