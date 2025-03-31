package com.servidores.projeto.servidores.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.servidores.projeto.commons.ModelNaoEncontradaException;
import com.servidores.projeto.commons.enums.ErrorType;
import com.servidores.projeto.servidores.dto.UnidadeRequestDTO;
import com.servidores.projeto.servidores.dto.UnidadeResponseDTO;
import com.servidores.projeto.servidores.model.UnidadeModel;
import com.servidores.projeto.servidores.repository.UnidadeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Long create(UnidadeRequestDTO requestDTO) {
        return unidadeRepository.save(modelMapper.map(requestDTO, UnidadeModel.class)).getId();
    }

    public UnidadeResponseDTO getById(Long id) {
        return modelMapper.map(unidadeRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.LOTACAO_NAO_ENCONTRADA, id)),
                UnidadeResponseDTO.class);
    }

    public Page<UnidadeResponseDTO> getAll(Pageable page) {
        return unidadeRepository.findAll(page)
                .map(l -> modelMapper.map(l, UnidadeResponseDTO.class));
    }

    @Transactional
    public UnidadeResponseDTO update(Long id, UnidadeRequestDTO requestDTO) {
        UnidadeModel unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.LOTACAO_NAO_ENCONTRADA, id));

        modelMapper.map(requestDTO, unidade);
        return modelMapper.map(unidadeRepository.save(unidade), UnidadeResponseDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        if (!unidadeRepository.existsById(id)) {
            throw new ModelNaoEncontradaException(ErrorType.LOTACAO_NAO_ENCONTRADA, id);
        }
        unidadeRepository.deleteById(id);
    }
}
