package com.servidores.projeto.servidores.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.servidores.projeto.commons.ModelNaoEncontradaException;
import com.servidores.projeto.commons.NotFoundException;
import com.servidores.projeto.commons.enums.ErrorType;
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
    private final ModelMapper modelMapper;

    public ServidorEfetivoService(ServidorEfetivoRepository servidorEfetivoRepository,
            PessoaRepository pessoaRepository) {
        this.servidorEfetivoRepository = servidorEfetivoRepository;
        this.pessoaRepository = pessoaRepository;
        this.modelMapper = new ModelMapper();
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
        return modelMapper.map(servidorEfetivoRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.SERV_EFETIVO_NAO_ENCONTRADO, id)),
                ServidorEfetivoResponseDTO.class);
    }

    public Page<ServidorEfetivoResponseDTO> getAll(Pageable page) {
        return servidorEfetivoRepository.findAll(page)
                .map(s -> modelMapper.map(s, ServidorEfetivoResponseDTO.class));
    }

    @Transactional
    public ServidorEfetivoResponseDTO update(Long id, ServidorEfetivoRequestDTO requestDTO) {
        ServidorEfetivoModel servidorEfetivo = servidorEfetivoRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.SERV_EFETIVO_NAO_ENCONTRADO, id));

        servidorEfetivo.setMatricula(requestDTO.getMatricula());
        servidorEfetivo = servidorEfetivoRepository.save(servidorEfetivo);
        return modelMapper.map(servidorEfetivo, ServidorEfetivoResponseDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        if (!servidorEfetivoRepository.existsById(id)) {
            throw new ModelNaoEncontradaException(ErrorType.SERV_EFETIVO_NAO_ENCONTRADO, id);
        }
        servidorEfetivoRepository.deleteById(id);
    }
}
