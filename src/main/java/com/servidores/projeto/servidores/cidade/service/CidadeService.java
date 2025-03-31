package com.servidores.projeto.servidores.cidade.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.servidores.projeto.commons.ModelNaoEncontradaException;
import com.servidores.projeto.commons.enums.ErrorType;
import com.servidores.projeto.servidores.cidade.dto.CidadeRequestDTO;
import com.servidores.projeto.servidores.cidade.dto.CidadeResponseDTO;
import com.servidores.projeto.servidores.cidade.model.CidadeModel;
import com.servidores.projeto.servidores.cidade.repository.CidadeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CidadeService {

    private final ModelMapper modelMapper;
    private final CidadeRepository cidadeRepository;

    @Transactional
    public Long createCidade(CidadeRequestDTO requestDTO) {
        CidadeModel cidade = modelMapper.map(requestDTO, CidadeModel.class);
        return cidadeRepository.save(cidade).getId();
    }

    public CidadeResponseDTO getById(Long id) {
        return modelMapper.map(cidadeRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.CIDADE_NAO_ENCONTRADA, id)),
                CidadeResponseDTO.class);
    }

    public Page<CidadeResponseDTO> getAll(Pageable page) {
        return cidadeRepository.findAll(page)
                .map(l -> modelMapper.map(l, CidadeResponseDTO.class));
    }

    @Transactional
    public CidadeResponseDTO update(Long id, CidadeRequestDTO requestDTO) {
        CidadeModel cidade = cidadeRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.CIDADE_NAO_ENCONTRADA, id));

        modelMapper.map(requestDTO, cidade);
        return modelMapper.map(cidadeRepository.save(cidade), CidadeResponseDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        if (!cidadeRepository.existsById(id)) {
            throw new ModelNaoEncontradaException(ErrorType.CIDADE_NAO_ENCONTRADA, id);
        }
        cidadeRepository.deleteById(id);
    }
}
