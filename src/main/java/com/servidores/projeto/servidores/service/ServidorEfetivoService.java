package com.servidores.projeto.servidores.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servidores.projeto.commons.ModelNaoEncontradaException;
import com.servidores.projeto.commons.enums.ErrorType;
import com.servidores.projeto.servidores.dto.ServidorEfetivoRequestDTO;
import com.servidores.projeto.servidores.dto.ServidorEfetivoResponseDTO;
import com.servidores.projeto.servidores.model.PessoaModel;
import com.servidores.projeto.servidores.model.ServidorEfetivoModel;
import com.servidores.projeto.servidores.repository.PessoaRepository;
import com.servidores.projeto.servidores.repository.ServidorEfetivoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServidorEfetivoService {

    private final ServidorEfetivoRepository servidorEfetivoRepository;
    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Long create(ServidorEfetivoRequestDTO requestDTO) {
        PessoaModel pessoa = buscarPessoaValidada(requestDTO.getPessoaId());

        ServidorEfetivoModel servidorEfetivo = modelMapper.map(requestDTO, ServidorEfetivoModel.class);
        servidorEfetivo.setPessoa(pessoa);

        servidorEfetivo.setId(pessoa.getId());

        return servidorEfetivoRepository.save(servidorEfetivo).getId();
    }

    @Transactional(readOnly = true)
    public ServidorEfetivoResponseDTO getById(Long id) {
        return servidorEfetivoRepository.findById(id).map(s -> modelMapper.map(s, ServidorEfetivoResponseDTO.class))
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.SERV_EFETIVO_NAO_ENCONTRADO, id));
    }

    @Transactional(readOnly = true)
    public Page<ServidorEfetivoResponseDTO> getAll(Pageable page) {
        return servidorEfetivoRepository.findAll(page)
                .map(s -> modelMapper.map(s, ServidorEfetivoResponseDTO.class));
    }

    @Transactional
    public ServidorEfetivoResponseDTO update(Long id, ServidorEfetivoRequestDTO requestDTO) {
        ServidorEfetivoModel servidorEfetivo = servidorEfetivoRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.SERV_EFETIVO_NAO_ENCONTRADO, id));

        modelMapper.map(requestDTO, servidorEfetivo);

        if (requestDTO.getPessoaId() != null && !requestDTO.getPessoaId().equals(servidorEfetivo.getPessoa().getId())) {
            PessoaModel novaPessoa = buscarPessoaValidada(requestDTO.getPessoaId());
            servidorEfetivo.setPessoa(novaPessoa);
        }
        return modelMapper.map(servidorEfetivoRepository.save(servidorEfetivo), ServidorEfetivoResponseDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        ServidorEfetivoModel servidor = servidorEfetivoRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.SERV_EFETIVO_NAO_ENCONTRADO, id));
        servidorEfetivoRepository.delete(servidor);
    }

    private PessoaModel buscarPessoaValidada(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.PESSOA_NAO_ENCONTRADA, pessoaId));
    }
}
