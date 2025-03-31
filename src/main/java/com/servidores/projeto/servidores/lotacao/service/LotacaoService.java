package com.servidores.projeto.servidores.lotacao.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.servidores.projeto.commons.enums.ErrorType;
import com.servidores.projeto.commons.exceptions.ModelNaoEncontradaException;
import com.servidores.projeto.servidores.lotacao.dto.LotacaoRequestDTO;
import com.servidores.projeto.servidores.lotacao.dto.LotacaoResponseDTO;
import com.servidores.projeto.servidores.lotacao.model.LotacaoModel;
import com.servidores.projeto.servidores.lotacao.repository.LotacaoRepository;
import com.servidores.projeto.servidores.pessoa.model.PessoaModel;
import com.servidores.projeto.servidores.pessoa.repository.PessoaRepository;
import com.servidores.projeto.servidores.unidade.model.UnidadeModel;
import com.servidores.projeto.servidores.unidade.repository.UnidadeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LotacaoService {

    private final LotacaoRepository lotacaoRepository;
    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;
    private final UnidadeRepository unidadeRepository;

    @Transactional
    public Long create(LotacaoRequestDTO requestDTO) {
        PessoaModel pessoa = buscarPessoaOuLancarExcecao(requestDTO.getPessoaId());
        UnidadeModel unidade = buscarUnidadeOuLancarExcecao(requestDTO.getUnidadeId());

        LotacaoModel lotacao = modelMapper.map(requestDTO, LotacaoModel.class);
        lotacao.setPessoa(pessoa);
        lotacao.setUnidade(unidade);

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

        modelMapper.map(requestDTO, lotacao);

        if (requestDTO.getPessoaId() != null) {
            lotacao.setPessoa(buscarPessoaOuLancarExcecao(requestDTO.getPessoaId()));
        }
        if (requestDTO.getUnidadeId() != null) {
            lotacao.setUnidade(buscarUnidadeOuLancarExcecao(requestDTO.getUnidadeId()));
        }

        return modelMapper.map(lotacaoRepository.save(lotacao), LotacaoResponseDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        if (!lotacaoRepository.existsById(id)) {
            throw new ModelNaoEncontradaException(ErrorType.LOTACAO_NAO_ENCONTRADA, id);
        }
        lotacaoRepository.deleteById(id);
    }

    private PessoaModel buscarPessoaOuLancarExcecao(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.PESSOA_NAO_ENCONTRADA, pessoaId));
    }

    private UnidadeModel buscarUnidadeOuLancarExcecao(Long unidadeId) {
        return unidadeRepository.findById(unidadeId)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.UNIDADE_NAO_ENCONTRADA, unidadeId));
    }
}
