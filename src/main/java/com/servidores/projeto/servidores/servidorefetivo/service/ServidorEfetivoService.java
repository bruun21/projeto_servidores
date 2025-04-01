package com.servidores.projeto.servidores.servidorefetivo.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servidores.projeto.commons.MinioService;
import com.servidores.projeto.commons.enums.ErrorType;
import com.servidores.projeto.commons.exceptions.ModelNaoEncontradaException;
import com.servidores.projeto.servidores.lotacao.model.LotacaoModel;
import com.servidores.projeto.servidores.pessoa.model.FotoPessoaModel;
import com.servidores.projeto.servidores.pessoa.model.PessoaModel;
import com.servidores.projeto.servidores.pessoa.repository.PessoaRepository;
import com.servidores.projeto.servidores.servidorefetivo.dto.ServidorEfetivoLotacaoResponseDTO;
import com.servidores.projeto.servidores.servidorefetivo.dto.ServidorEfetivoRequestDTO;
import com.servidores.projeto.servidores.servidorefetivo.dto.ServidorEfetivoResponseDTO;
import com.servidores.projeto.servidores.servidorefetivo.model.ServidorEfetivoModel;
import com.servidores.projeto.servidores.servidorefetivo.repository.ServidorEfetivoRepository;
import com.servidores.projeto.servidores.unidade.repository.UnidadeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServidorEfetivoService {

    private final ServidorEfetivoRepository servidorEfetivoRepository;
    private final UnidadeRepository unidadeRepository;
    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;
    private final MinioService minioService;

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @Transactional
    public Long create(ServidorEfetivoRequestDTO requestDTO) {
        PessoaModel pessoa = buscarPessoaValidada(requestDTO.getPessoaId());

        ServidorEfetivoModel servidorEfetivo = new ServidorEfetivoModel(pessoa, requestDTO.getMatricula());

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

    public List<ServidorEfetivoLotacaoResponseDTO> findServidoresPorUnidade(Long unidId) {
        unidadeRepository.findById(unidId)
                .orElseThrow(() -> new EntityNotFoundException("Unidade não encontrada"));

        return servidorEfetivoRepository.findByUnidadeLotacao(unidId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ServidorEfetivoLotacaoResponseDTO toResponse(ServidorEfetivoModel servidor) {
        PessoaModel pessoa = servidor.getPessoa();
        LotacaoModel lotacaoAtiva = getLotacaoAtiva(pessoa);

        return new ServidorEfetivoLotacaoResponseDTO(
                pessoa.getNome(),
                calcularIdade(pessoa.getDataNascimento()),
                lotacaoAtiva.getUnidade().getNome(),
                getFotoUrl(pessoa.getFotos()));
    }

    private LotacaoModel getLotacaoAtiva(PessoaModel pessoa) {
        return pessoa.getLotacoes().stream()
                .filter(l -> l.getDataRemocao() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Servidor sem lotação ativa"));
    }

    private Integer calcularIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    private String getFotoUrl(List<FotoPessoaModel> fotos) {
        if (fotos == null || fotos.isEmpty()) {
            return null;
        }

        String objectName = fotos.get(0).getHash();

        return String.format("%s/%s/%s", minioEndpoint, minioService.getBucketName(), objectName);
    }

}
