package com.servidores.projeto.commons.enums;

import lombok.Getter;

@Getter
public enum ErrorType {

    SERV_EFETIVO_NAO_ENCONTRADO("Servidor efetivo não encontrado", "SE-001"),
    LOTACAO_NAO_ENCONTRADA("Lotação não encontrada", "LT-001"),
    SERV_TEMP_NAO_ENCONTRADO("Servidor temporário não encontrado", "ST-001"),
    UNIDADE_NAO_ENCONTRADA("Unidade não encontrada", "UN-001"),
    PESSOA_NAO_ENCONTRADA("Pessoa não encontrada", "P-001");

    private String message;
    private String internalCode;

    private ErrorType(String message, String internalCode) {
        this.message = message;
        this.internalCode = internalCode;
    }
}
