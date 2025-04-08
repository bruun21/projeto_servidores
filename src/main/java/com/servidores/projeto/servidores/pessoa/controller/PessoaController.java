package com.servidores.projeto.servidores.pessoa.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.servidores.projeto.servidores.pessoa.dto.PessoaGetDTO;
import com.servidores.projeto.servidores.pessoa.dto.PessoaRequestDTO;
import com.servidores.projeto.servidores.pessoa.dto.PessoaResponseDTO;
import com.servidores.projeto.servidores.pessoa.service.PessoaService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
public class PessoaController {

    private final PessoaService pessoaService;

    /**
     * Cria uma nova pessoa.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createPessoa(
            @ModelAttribute PessoaRequestDTO requestDTO,
            BindingResult result) {
        return ResponseEntity.ok(pessoaService.createPessoa(requestDTO));
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
    public ResponseEntity<Page<PessoaGetDTO>> getAll(
            @Parameter(description = "Paginação e ordenação", example = "{\"page\": 0, \"size\": 5, \"sort\": \"id,asc\"}") Pageable pageable) {
        Page<PessoaGetDTO> pessoas = pessoaService.getAll(pageable);
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