package com.servidores.projeto.servidores.pessoa.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PessoaRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 200, message = "Nome deve ter até 200 caracteres")
    private String nome;

    private LocalDate dataNascimento;

    @NotBlank(message = "Sexo é obrigatório")
    @Size(max = 9, message = "Sexo deve ter até 9 caracteres (ex: MASCULINO)")
    private String sexo;

    @Size(max = 200, message = "Nome da mãe deve ter até 200 caracteres")
    private String mae;

    @Size(max = 200, message = "Nome do pai deve ter até 200 caracteres")
    private String pai;

    private List<Long> enderecoIds;

    private List<MultipartFile> fotos;

}
