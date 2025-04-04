package com.servidores.projeto.servidores.pessoa.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.servidores.projeto.commons.MinioService;
import com.servidores.projeto.commons.enums.ErrorType;
import com.servidores.projeto.commons.exceptions.FileStorageException;
import com.servidores.projeto.commons.exceptions.ModelNaoEncontradaException;
import com.servidores.projeto.servidores.endereco.model.EnderecoModel;
import com.servidores.projeto.servidores.endereco.repository.EnderecoRepository;
import com.servidores.projeto.servidores.pessoa.dto.PessoaRequestDTO;
import com.servidores.projeto.servidores.pessoa.dto.PessoaResponseDTO;
import com.servidores.projeto.servidores.pessoa.model.FotoPessoaModel;
import com.servidores.projeto.servidores.pessoa.model.PessoaModel;
import com.servidores.projeto.servidores.pessoa.repository.FotoPessoaRepository;
import com.servidores.projeto.servidores.pessoa.repository.PessoaRepository;

import jakarta.persistence.EntityNotFoundException;
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

        final PessoaModel pessoa = buildPessoaFromRequest(requestDTO);
        associateEnderecos(pessoa, requestDTO.getEnderecoIds());

        pessoaRepository.save(pessoa);

        processFotos(requestDTO, pessoa);

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

    private PessoaModel buildPessoaFromRequest(PessoaRequestDTO requestDTO) {
        return new PessoaModel(
                requestDTO.getNome(),
                requestDTO.getDataNascimento(),
                requestDTO.getSexo(),
                requestDTO.getMae(),
                requestDTO.getPai());
    }

    private void associateEnderecos(PessoaModel pessoa, List<Long> enderecoIds) {
        if (enderecoIds == null || enderecoIds.isEmpty()) {
            return;
        }

        final List<EnderecoModel> enderecos = enderecoRepository.findAllById(enderecoIds);

        if (enderecos.size() != enderecoIds.size()) {
            final Set<Long> foundIds = enderecos.stream()
                    .map(EnderecoModel::getId)
                    .collect(Collectors.toSet());

            final List<Long> missingIds = enderecoIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());

            throw new EntityNotFoundException("Endereços não encontrados: " + missingIds);
        }

        pessoa.setEnderecos(enderecos);
    }

    private void processFotos(PessoaRequestDTO requestDTO, PessoaModel pessoa) {
        Optional.ofNullable(requestDTO.getFotos())
                .filter(fotos -> !fotos.isEmpty())
                .ifPresent(fotos -> {
                    validateFotos(fotos);
                    saveFotos(pessoa, fotos);
                });
    }

    private void validateFotos(List<MultipartFile> fotos) {
        fotos.forEach(foto -> {
            if (foto.isEmpty()) {
                throw new FileStorageException("Arquivo vazio recebido");
            }
            if (!foto.getContentType().startsWith("image/")) {
                throw new FileStorageException("Tipo de arquivo inválido: " + foto.getContentType());
            }
        });
    }

    private void saveFotos(PessoaModel pessoa, List<MultipartFile> fotos) {
        final List<FotoPessoaModel> fotoModels = fotos.stream()
                .map(this::uploadFoto)
                .map(minioKey -> buildFotoModel(pessoa, minioKey))
                .collect(Collectors.toList());

        fotoPessoaRepository.saveAll(fotoModels);
    }

    private String uploadFoto(MultipartFile foto) {
        try {
            return minioService.uploadFile(foto);
        } catch (RuntimeException e) {
            throw new FileStorageException("Erro ao salvar foto no storage");
        }
    }

    private FotoPessoaModel buildFotoModel(PessoaModel pessoa, String minioKey) {
        return FotoPessoaModel.builder()
                .pessoa(pessoa)
                .data(LocalDate.now())
                .bucket(minioService.getBucketName())
                .hash(minioKey)
                .build();
    }

}
