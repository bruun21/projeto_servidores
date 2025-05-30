package com.servidores.projeto.servidores.servidorefetivo.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.servidores.projeto.servidores.servidorefetivo.dto.EnderecoFuncionalDTO;
import com.servidores.projeto.servidores.servidorefetivo.dto.ServidorEfetivoLotacaoResponseDTO;
import com.servidores.projeto.servidores.servidorefetivo.dto.ServidorEfetivoRequestDTO;
import com.servidores.projeto.servidores.servidorefetivo.dto.ServidorEfetivoResponseDTO;
import com.servidores.projeto.servidores.servidorefetivo.service.ServidorEfetivoService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/servidor-efetivo")
@RequiredArgsConstructor
public class ServidorEfetivoController {

    private final ServidorEfetivoService servidorEfetivoService;

    @PostMapping
    public ResponseEntity<Long> criarServidorEfetivo(@RequestBody ServidorEfetivoRequestDTO requestDTO) {
        Long id = servidorEfetivoService.create(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(location).body(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServidorEfetivoResponseDTO> buscarPorId(@PathVariable Long id) {
        ServidorEfetivoResponseDTO responseDTO = servidorEfetivoService.getById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ServidorEfetivoResponseDTO>> listarTodos(@Parameter(
                description = "Paginação e ordenação", 
                example = "{\"page\": 0, \"size\": 5, \"sort\": \"id,asc\"}"
            ) Pageable pageable) {
        Page<ServidorEfetivoResponseDTO> servidores = servidorEfetivoService.getAll(pageable);
        return ResponseEntity.ok(servidores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServidorEfetivoResponseDTO> atualizarServidor(
            @PathVariable Long id,
            @RequestBody ServidorEfetivoRequestDTO requestDTO) {
        ServidorEfetivoResponseDTO servidorAtualizado = servidorEfetivoService.update(id, requestDTO);
        return ResponseEntity.ok(servidorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirServidor(@PathVariable Long id) {
        servidorEfetivoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/por-unidade/{unidId}")
    public ResponseEntity<Page<ServidorEfetivoLotacaoResponseDTO>> getPorUnidade(
            @PathVariable Long unidId, @Parameter(
                description = "Paginação e ordenação", 
                example = "{\"page\": 0, \"size\": 5, \"sort\": \"id,asc\"}"
            ) Pageable pageable) {

        Page<ServidorEfetivoLotacaoResponseDTO> response = servidorEfetivoService.findServidoresPorUnidade(unidId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/endereco-funcional")
    public ResponseEntity<Page<EnderecoFuncionalDTO>> findByNomeParcial(
            @RequestParam("nome") String parteNome, @Parameter(
                description = "Paginação e ordenação", 
                example = "{\"page\": 0, \"size\": 5, \"sort\": \"id,asc\"}"
            ) Pageable pageable) {
        
        if (parteNome == null || parteNome.trim().isEmpty() || parteNome.length() < 3) {
            return ResponseEntity.badRequest().body(null);
        }
        
        Page<EnderecoFuncionalDTO> enderecos = servidorEfetivoService.findEnderecoFuncionalByNomeParcial(parteNome, pageable);
        
        if (enderecos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(enderecos);
    }
}
