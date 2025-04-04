package com.servidores.projeto.servidores.endereco.controller;

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

import com.servidores.projeto.servidores.endereco.dto.EnderecoRequestDTO;
import com.servidores.projeto.servidores.endereco.dto.EnderecoResponseDTO;
import com.servidores.projeto.servidores.endereco.service.EnderecoService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/endereco")
@RequiredArgsConstructor
public class EnderecoController {

    private final EnderecoService enderecoService;

    @PostMapping
    public ResponseEntity<Long> criarEndereco(@RequestBody EnderecoRequestDTO requestDTO) {
        Long id = enderecoService.create(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> buscarPorId(@PathVariable Long id) {
        EnderecoResponseDTO responseDTO = enderecoService.getById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<EnderecoResponseDTO>> listarTodos(
            @Parameter(description = "Paginação e ordenação", example = "{\"page\": 0, \"size\": 5, \"sort\": \"id,asc\"}") Pageable pageable) {
        Page<EnderecoResponseDTO> enderecos = enderecoService.getAll(pageable);
        return ResponseEntity.ok(enderecos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> atualizarEndereco(
            @PathVariable Long id,
            @RequestBody EnderecoRequestDTO requestDTO) {
        EnderecoResponseDTO enderecoAtualizado = enderecoService.update(id, requestDTO);
        return ResponseEntity.ok(enderecoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEndereco(@PathVariable Long id) {
        enderecoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}