package com.servidores.projeto.servidores.pessoa.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.servidores.projeto.commons.MinioService;
import com.servidores.projeto.commons.enums.ErrorType;
import com.servidores.projeto.commons.exceptions.ModelNaoEncontradaException;
import com.servidores.projeto.servidores.endereco.model.EnderecoModel;
import com.servidores.projeto.servidores.endereco.repository.EnderecoRepository;
import com.servidores.projeto.servidores.pessoa.dto.PessoaRequestDTO;
import com.servidores.projeto.servidores.pessoa.dto.PessoaResponseDTO;
import com.servidores.projeto.servidores.pessoa.model.FotoPessoaModel;
import com.servidores.projeto.servidores.pessoa.model.PessoaModel;
import com.servidores.projeto.servidores.pessoa.repository.FotoPessoaRepository;
import com.servidores.projeto.servidores.pessoa.repository.PessoaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final ModelMapper modelMapper;
    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;
    private final FotoPessoaRepository fotoPessoaRepository;
    private final MinioService minioService;

    @Transactional
    public Long createPessoa(PessoaRequestDTO requestDTO) {
        List<String> fotoHashes = null;
        if (requestDTO.getFotos() != null && !requestDTO.getFotos().isEmpty()) {
            fotoHashes = requestDTO.getFotos().stream()
                    .map(minioService::uploadFile)
                    .collect(Collectors.toList());
        }

        PessoaModel pessoa = PessoaModel.builder()
       .nome(requestDTO.getNome())
       .dataNascimento(requestDTO.getDataNascimento())
       .sexo(requestDTO.getSexo())
       .mae(requestDTO.getMae())
       .pai(requestDTO.getPai())
       .build();

        List<EnderecoModel> enderecos = enderecoRepository.findAllById(requestDTO.getEnderecoIds());
        pessoa.setEnderecos(enderecos);

        pessoa = pessoaRepository.save(pessoa);

        if (fotoHashes != null) {
            for (String hash : fotoHashes) {
                FotoPessoaModel foto = FotoPessoaModel.builder()
                .pessoa(pessoa)
                .data(LocalDate.now())
                .bucket(MinioService.BUCKET_NAME)
                .hash(hash)
                .build();
                fotoPessoaRepository.save(foto);
                
            }
        }

        return pessoa.getId();
    }

    public PessoaResponseDTO getById(Long id) {
        return modelMapper.map(pessoaRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.PESSOA_NAO_ENCONTRADA, id)),
                PessoaResponseDTO.class);
    }

    public Page<PessoaResponseDTO> getAll(Pageable page) {
        return pessoaRepository.findAll(page)
                .map(l -> modelMapper.map(l, PessoaResponseDTO.class));
    }

    @Transactional
    public PessoaResponseDTO update(Long id, PessoaRequestDTO requestDTO) {
        PessoaModel pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new ModelNaoEncontradaException(ErrorType.PESSOA_NAO_ENCONTRADA, id));

        modelMapper.map(requestDTO, pessoa);
        return modelMapper.map(pessoaRepository.save(pessoa), PessoaResponseDTO.class);
    }

    @Transactional
    public void delete(Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new ModelNaoEncontradaException(ErrorType.PESSOA_NAO_ENCONTRADA, id);
        }
        pessoaRepository.deleteById(id);
    }
}
