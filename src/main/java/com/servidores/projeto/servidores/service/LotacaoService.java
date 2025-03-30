package com.servidores.projeto.servidores.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.servidores.projeto.commons.ModelNaoEncontradaException;
import com.servidores.projeto.commons.NotFoundException;
import com.servidores.projeto.commons.enums.ErrorType;
import com.servidores.projeto.servidores.dto.LotacaoRequestDTO;
import com.servidores.projeto.servidores.dto.LotacaoResponseDTO;
import com.servidores.projeto.servidores.model.LotacaoModel;
import com.servidores.projeto.servidores.model.PessoaModel;
import com.servidores.projeto.servidores.model.UnidadeModel;
import com.servidores.projeto.servidores.repository.LotacaoRepository;
import com.servidores.projeto.servidores.repository.PessoaRepository;
import com.servidores.projeto.servidores.repository.UnidadeRepository;

import jakarta.transaction.Transactional;

@Service
public class LotacaoService {

    private final LotacaoRepository lotacaoRepository;
    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;
    private final UnidadeRepository unidadeRepository;

    public LotacaoService(LotacaoRepository lotacaoRepository,
            PessoaRepository pessoaRepository) {
        this.lotacaoRepository = lotacaoRepository;
        this.pessoaRepository = pessoaRepository;
        this.modelMapper = new ModelMapper();
        this.unidadeRepository = null;
    }

    @Transactional
    public Long create(LotacaoRequestDTO requestDTO) {
        PessoaModel pessoa = pessoaRepository.findById(requestDTO.getPessoaId())
                .orElseThrow(() -> new NotFoundException("Pessoa não encontrada"));
        UnidadeModel unidadeModel = unidadeRepository.findById(requestDTO.getUnidadeId())
                .orElseThrow(() -> new NotFoundException("Pessoa não encontrada"));

        LotacaoModel lotacao = LotacaoModel.builder()
                .id(pessoa.getId())
                .dataLotacao(requestDTO.getDataLotacao())
                .unidade(unidadeModel)
                .portaria(requestDTO.getPortaria())
                .pessoa(pessoa)
                .build();

        return lotacaoRepository.save(lotacao).getId();
    }

    public LotacaoResponseDTO getById(Long id) {
        return modelMapper.map(lotacaoRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.LOTACAO_NAO_ENCONTRADA, id)),
                LotacaoResponseDTO.class);
    }

    public Page<LotacaoResponseDTO> getAll(Pageable page) {
        return lotacaoRepository.findAll(page)
                .map(l -> modelMapper.map(l, LotacaoResponseDTO.class));
    }

    @Transactional
    public LotacaoResponseDTO update(Long id, LotacaoRequestDTO requestDTO) {
        LotacaoModel lotacao = lotacaoRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.LOTACAO_NAO_ENCONTRADA, id));

        // Atualize aqui os campos específicos
        return modelMapper.map(lotacaoRepository.save(lotacao), LotacaoResponseDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        if (!lotacaoRepository.existsById(id)) {
            throw new ModelNaoEncontradaException(ErrorType.LOTACAO_NAO_ENCONTRADA, id);
        }
        lotacaoRepository.deleteById(id);
    }
}