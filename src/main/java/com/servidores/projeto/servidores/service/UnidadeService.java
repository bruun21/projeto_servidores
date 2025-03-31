package com.servidores.projeto.servidores.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.servidores.projeto.commons.ModelNaoEncontradaException;
import com.servidores.projeto.commons.enums.ErrorType;
import com.servidores.projeto.servidores.dto.UnidadeRequestDTO;
import com.servidores.projeto.servidores.dto.UnidadeResponseDTO;
import com.servidores.projeto.servidores.model.EnderecoModel;
import com.servidores.projeto.servidores.model.UnidadeModel;
import com.servidores.projeto.servidores.repository.EnderecoRepository;
import com.servidores.projeto.servidores.repository.UnidadeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;
    private final ModelMapper modelMapper;
    private final EnderecoRepository enderecoRepository;

    @Transactional
    public Long create(UnidadeRequestDTO requestDTO) {
        UnidadeModel unidade = modelMapper.map(requestDTO, UnidadeModel.class);

        List<EnderecoModel> enderecos = requestDTO.getIdEnderecos().stream()
                .map(idEndereco -> enderecoRepository.findById(idEndereco)
                        .orElseThrow(() -> new ModelNaoEncontradaException(
                                ErrorType.ENDERECO_NAO_ENCONTRADO, idEndereco)))
                .toList();

        unidade.setEnderecos(enderecos);
        unidade = unidadeRepository.save(unidade);

        return unidade.getId();
    }

    public UnidadeResponseDTO getById(Long id) {
        return modelMapper.map(unidadeRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.UNIDADE_NAO_ENCONTRADA, id)),
                UnidadeResponseDTO.class);
    }

    public Page<UnidadeResponseDTO> getAll(Pageable page) {
        return unidadeRepository.findAll(page)
                .map(l -> modelMapper.map(l, UnidadeResponseDTO.class));
    }

    @Transactional
    public UnidadeResponseDTO update(Long id, UnidadeRequestDTO requestDTO) {
        UnidadeModel unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.UNIDADE_NAO_ENCONTRADA, id));

        modelMapper.map(requestDTO, unidade);
        return modelMapper.map(unidadeRepository.save(unidade), UnidadeResponseDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        if (!unidadeRepository.existsById(id)) {
            throw new ModelNaoEncontradaException(ErrorType.UNIDADE_NAO_ENCONTRADA, id);
        }
        unidadeRepository.deleteById(id);
    }
}
