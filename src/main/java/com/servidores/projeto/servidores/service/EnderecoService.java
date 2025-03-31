package com.servidores.projeto.servidores.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.servidores.projeto.commons.ModelNaoEncontradaException;
import com.servidores.projeto.commons.enums.ErrorType;
import com.servidores.projeto.servidores.dto.EnderecoRequestDTO;
import com.servidores.projeto.servidores.dto.EnderecoResponseDTO;
import com.servidores.projeto.servidores.model.CidadeModel;
import com.servidores.projeto.servidores.model.EnderecoModel;
import com.servidores.projeto.servidores.repository.CidadeRepository;
import com.servidores.projeto.servidores.repository.EnderecoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final ModelMapper modelMapper;
    private final CidadeRepository cidadeRepository;

    @Transactional
    public Long create(EnderecoRequestDTO requestDTO) {
        CidadeModel cidade = cidadeRepository.findById(requestDTO.getCidadeId())
                .orElseThrow(() -> new RuntimeException("Cidade não encontrada"));

        EnderecoModel endereco = modelMapper.map(requestDTO, EnderecoModel.class);
        endereco.setCidade(cidade);

        return enderecoRepository.save(endereco).getId();
    }

    public EnderecoResponseDTO getById(Long id) {
        return modelMapper.map(enderecoRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.LOTACAO_NAO_ENCONTRADA, id)),
                EnderecoResponseDTO.class);
    }

    public Page<EnderecoResponseDTO> getAll(Pageable page) {
        return enderecoRepository.findAll(page)
                .map(l -> modelMapper.map(l, EnderecoResponseDTO.class));
    }

    @Transactional
    public EnderecoResponseDTO update(Long id, EnderecoRequestDTO requestDTO) {
        EnderecoModel endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.LOTACAO_NAO_ENCONTRADA, id));

        modelMapper.map(requestDTO, endereco);
        return modelMapper.map(enderecoRepository.save(endereco), EnderecoResponseDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        if (!enderecoRepository.existsById(id)) {
            throw new ModelNaoEncontradaException(ErrorType.LOTACAO_NAO_ENCONTRADA, id);
        }
        enderecoRepository.deleteById(id);
    }
}
