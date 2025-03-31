package com.servidores.projeto.servidores.servidortemporario.controller;

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

import com.servidores.projeto.servidores.servidortemporario.dto.ServidorTemporarioRequestDTO;
import com.servidores.projeto.servidores.servidortemporario.dto.ServidorTemporarioResponseDTO;
import com.servidores.projeto.servidores.servidortemporario.service.ServidorTemporarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/servidores-temporarios")
@RequiredArgsConstructor
public class ServidorTemporarioController {

    private final ServidorTemporarioService servidorTemporarioService;

    @PostMapping
    public ResponseEntity<Long> criarServidorTemporario(@RequestBody ServidorTemporarioRequestDTO requestDTO) {
        Long id = servidorTemporarioService.create(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServidorTemporarioResponseDTO> buscarPorId(@PathVariable Long id) {
        ServidorTemporarioResponseDTO responseDTO = servidorTemporarioService.getById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ServidorTemporarioResponseDTO>> listarTodos(Pageable pageable) {
        Page<ServidorTemporarioResponseDTO> servidores = servidorTemporarioService.getAll(pageable);
        return ResponseEntity.ok(servidores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServidorTemporarioResponseDTO> atualizarServidor(
            @PathVariable Long id,
            @RequestBody ServidorTemporarioRequestDTO requestDTO) {
        ServidorTemporarioResponseDTO servidorAtualizado = servidorTemporarioService.update(id, requestDTO);
        return ResponseEntity.ok(servidorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirServidor(@PathVariable Long id) {
        servidorTemporarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}