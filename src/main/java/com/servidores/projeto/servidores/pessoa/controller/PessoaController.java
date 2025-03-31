package com.servidores.projeto.servidores.pessoa.controller;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.servidores.projeto.servidores.pessoa.dto.PessoaRequestDTO;
import com.servidores.projeto.servidores.pessoa.dto.PessoaResponseDTO;
import com.servidores.projeto.servidores.pessoa.service.PessoaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/pessoas")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;

    /**
     * Cria uma nova pessoa.
     */
    @PostMapping
    public ResponseEntity<Long> createPessoa(@RequestBody PessoaRequestDTO requestDTO) {
        Long id = pessoaService.createPessoa(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    /**
     * Busca uma pessoa pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> getById(@PathVariable Long id) {
        PessoaResponseDTO response = pessoaService.getById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas as pessoas com paginação.
     */
    @GetMapping
    public ResponseEntity<Page<PessoaResponseDTO>> getAll(Pageable pageable) {
        Page<PessoaResponseDTO> pessoas = pessoaService.getAll(pageable);
        return ResponseEntity.ok(pessoas);
    }

    /**
     * Atualiza uma pessoa existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid PessoaRequestDTO requestDTO) {
        PessoaResponseDTO response = pessoaService.update(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Deleta uma pessoa pelo ID.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        pessoaService.delete(id);
    }
}