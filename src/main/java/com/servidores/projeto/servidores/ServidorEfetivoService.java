package com.servidores.projeto.servidores;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.servidores.projeto.commons.NotFoundException;
import com.servidores.projeto.servidores.dto.ServidorEfetivoRequestDTO;
import com.servidores.projeto.servidores.dto.ServidorEfetivoResponseDTO;
import com.servidores.projeto.servidores.model.PessoaModel;
import com.servidores.projeto.servidores.model.ServidorEfetivoModel;
import com.servidores.projeto.servidores.repository.PessoaRepository;
import com.servidores.projeto.servidores.repository.ServidorEfetivoRepository;

import jakarta.transaction.Transactional;

@Service
public class ServidorEfetivoService {

    private final ServidorEfetivoRepository servidorEfetivoRepository;
    private final PessoaRepository pessoaRepository;

    public ServidorEfetivoService(ServidorEfetivoRepository servidorEfetivoRepository,
            PessoaRepository pessoaRepository) {
        this.servidorEfetivoRepository = servidorEfetivoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public Long create(ServidorEfetivoRequestDTO requestDTO) {
        PessoaModel pessoa = pessoaRepository.findById(requestDTO.getPessoaId())
                .orElseThrow(() -> new NotFoundException("Pessoa não encontrada"));

        ServidorEfetivoModel servidorEfetivo = ServidorEfetivoModel.builder()
                .id(pessoa.getId())
                .pessoa(pessoa)
                .matricula(requestDTO.getMatricula())
                .build();

        return servidorEfetivoRepository.save(servidorEfetivo).getId();
    }

    public ServidorEfetivoResponseDTO getById(Long id) {
        ServidorEfetivoModel servidorEfetivo = servidorEfetivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servidor efetivo não encontrado"));
        return new ServidorEfetivoResponseDTO(servidorEfetivo);
    }

    public List<ServidorEfetivoResponseDTO> getAll() {
        return servidorEfetivoRepository.findAll().stream()
                .map(ServidorEfetivoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServidorEfetivoResponseDTO update(Long id, ServidorEfetivoRequestDTO requestDTO) {
        ServidorEfetivoModel servidorEfetivo = servidorEfetivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servidor efetivo não encontrado"));

        servidorEfetivo.setMatricula(requestDTO.getMatricula());
        servidorEfetivo = servidorEfetivoRepository.save(servidorEfetivo);
        return new ServidorEfetivoResponseDTO(servidorEfetivo);
    }

    @Transactional
    public void delete(Long id) {
        if (!servidorEfetivoRepository.existsById(id)) {
            throw new RuntimeException("Servidor efetivo não encontrado");
        }
        servidorEfetivoRepository.deleteById(id);
    }
}
