package com.servidores.projeto.servidores.servidortemporario.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servidores.projeto.commons.enums.ErrorType;
import com.servidores.projeto.commons.exceptions.ModelNaoEncontradaException;
import com.servidores.projeto.servidores.pessoa.model.PessoaModel;
import com.servidores.projeto.servidores.pessoa.repository.PessoaRepository;
import com.servidores.projeto.servidores.servidortemporario.dto.ServidorTemporarioRequestDTO;
import com.servidores.projeto.servidores.servidortemporario.dto.ServidorTemporarioResponseDTO;
import com.servidores.projeto.servidores.servidortemporario.model.ServidorTemporarioModel;
import com.servidores.projeto.servidores.servidortemporario.repository.ServidorTemporarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServidorTemporarioService {

    private final ServidorTemporarioRepository servidorTemporarioRepository;
    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public Long create(ServidorTemporarioRequestDTO requestDTO) {
        PessoaModel pessoa = buscarPessoaValidada(requestDTO.getPessoaId());

        ServidorTemporarioModel servidorTemporario = new ServidorTemporarioModel(pessoa, requestDTO.getDataAdmissao(),
                requestDTO.getDataDemissao());

        return servidorTemporarioRepository.save(servidorTemporario).getPessoa().getId();
    }

    @Transactional(readOnly = true)
    public ServidorTemporarioResponseDTO getById(Long id) {
        return servidorTemporarioRepository.findById(id)
                .map(s -> modelMapper.map(s, ServidorTemporarioResponseDTO.class))
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.SERV_TEMPORARIO_NAO_ENCONTRADO, id));
    }

    @Transactional(readOnly = true)
    public Page<ServidorTemporarioResponseDTO> getAll(Pageable page) {
        return servidorTemporarioRepository.findAll(page)
                .map(s -> modelMapper.map(s, ServidorTemporarioResponseDTO.class));
    }

    @Transactional
    public ServidorTemporarioResponseDTO update(Long id, ServidorTemporarioRequestDTO requestDTO) {
        ServidorTemporarioModel servidorTemporario = servidorTemporarioRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.SERV_TEMPORARIO_NAO_ENCONTRADO, id));

        modelMapper.map(requestDTO, servidorTemporario);

        if (requestDTO.getPessoaId() != null
                && !requestDTO.getPessoaId().equals(servidorTemporario.getPessoa().getId())) {
            PessoaModel novaPessoa = buscarPessoaValidada(requestDTO.getPessoaId());
            servidorTemporario.setPessoa(novaPessoa);
        }
        return modelMapper.map(servidorTemporarioRepository.save(servidorTemporario),
                ServidorTemporarioResponseDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        ServidorTemporarioModel servidor = servidorTemporarioRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.SERV_TEMPORARIO_NAO_ENCONTRADO, id));
        servidorTemporarioRepository.delete(servidor);
    }

    private PessoaModel buscarPessoaValidada(Long pessoaId) {
        return pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.PESSOA_NAO_ENCONTRADA, pessoaId));
    }
}
