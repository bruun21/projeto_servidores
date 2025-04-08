package com.servidores.projeto.servidores.servidorefetivo.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servidores.projeto.commons.enums.ErrorType;
import com.servidores.projeto.commons.exceptions.ModelNaoEncontradaException;
import com.servidores.projeto.commons.minio.MinioService;
import com.servidores.projeto.commons.utils.ModelMapperUtils;
import com.servidores.projeto.servidores.endereco.model.EnderecoModel;
import com.servidores.projeto.servidores.lotacao.model.LotacaoModel;
import com.servidores.projeto.servidores.pessoa.model.FotoPessoaModel;
import com.servidores.projeto.servidores.pessoa.model.PessoaModel;
import com.servidores.projeto.servidores.pessoa.repository.PessoaRepository;
import com.servidores.projeto.servidores.servidorefetivo.dto.EnderecoDTO;
import com.servidores.projeto.servidores.servidorefetivo.dto.EnderecoFuncionalDTO;
import com.servidores.projeto.servidores.servidorefetivo.dto.ServidorEfetivoLotacaoResponseDTO;
import com.servidores.projeto.servidores.servidorefetivo.dto.ServidorEfetivoRequestDTO;
import com.servidores.projeto.servidores.servidorefetivo.dto.ServidorEfetivoResponseDTO;
import com.servidores.projeto.servidores.servidorefetivo.model.ServidorEfetivoModel;
import com.servidores.projeto.servidores.servidorefetivo.repository.ServidorEfetivoRepository;
import com.servidores.projeto.servidores.unidade.model.UnidadeModel;
import com.servidores.projeto.servidores.unidade.repository.UnidadeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServidorEfetivoService {

    private final ServidorEfetivoRepository servidorEfetivoRepository;
    private final UnidadeRepository unidadeRepository;
    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;
    private final MinioService minioService;
    private final ModelMapperUtils modelMapperUtils;

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

        modelMapperUtils.mapNonNullFields(requestDTO, servidorEfetivo);

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

    public Page<ServidorEfetivoLotacaoResponseDTO> findServidoresPorUnidade(Long unidId, Pageable pageable) {
        if (!unidadeRepository.existsById(unidId)) {
            throw new ModelNaoEncontradaException(ErrorType.UNIDADE_NAO_ENCONTRADA, unidId);
        }

        Page<ServidorEfetivoModel> pageEntidade = servidorEfetivoRepository.findByUnidadeLotacao(unidId, pageable);
        return pageEntidade.map(this::toResponse);
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
        if (dataNascimento == null) {
            throw new IllegalArgumentException("Data de nascimento não pode ser nula");
        }
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    private String getFotoUrl(List<FotoPessoaModel> fotos) {
        return Optional.ofNullable(fotos)
                .filter(list -> !list.isEmpty())
                .map(list -> minioService.generatePresignedUrl(list.get(0).getHash(), 300))
                .orElse(null);
    }

    public Page<EnderecoFuncionalDTO> findEnderecoFuncionalByNomeParcial(String parteNome, Pageable pageable) {
        Page<ServidorEfetivoModel> servidoresPage = servidorEfetivoRepository.findByNomeContainingIgnoreCase(parteNome,
                pageable);

        List<EnderecoFuncionalDTO> dtos = servidoresPage.stream()
                .map(this::convertToDTO)
                .filter(dto -> !dto.getEnderecosFuncionais().isEmpty())
                .toList();

        return new PageImpl<>(
                dtos,
                servidoresPage.getPageable(),
                servidoresPage.getTotalElements());
    }

    private EnderecoFuncionalDTO convertToDTO(ServidorEfetivoModel servidor) {
        PessoaModel pessoa = servidor.getPessoa();

        LotacaoModel lotacaoAtual = getLotacaoAtiva(pessoa);

        if (lotacaoAtual == null) {
            return new EnderecoFuncionalDTO(
                    pessoa.getNome(),
                    servidor.getMatricula(),
                    "",
                    "",
                    new ArrayList<>());
        }

        UnidadeModel unidade = lotacaoAtual.getUnidade();

        List<EnderecoDTO> enderecosDTO = unidade.getEnderecos().stream()
                .map(this::convertEnderecoToDTO)
                .collect(Collectors.toList());

        return new EnderecoFuncionalDTO(
                pessoa.getNome(),
                servidor.getMatricula(),
                unidade.getNome(),
                unidade.getSigla(),
                enderecosDTO);
    }

    private EnderecoDTO convertEnderecoToDTO(EnderecoModel endereco) {
        return new EnderecoDTO(
                endereco.getLogradouro(),
                endereco.getNumero().toString(),
                endereco.getBairro(),
                endereco.getCidade().getNome(),
                endereco.getCidade().getUf());
    }
}
