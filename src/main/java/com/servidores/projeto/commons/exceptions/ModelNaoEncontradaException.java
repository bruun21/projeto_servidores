package com.servidores.projeto.commons.exceptions;

import com.servidores.projeto.commons.enums.ErrorType;

import lombok.Getter;

@Getter
public class ModelNaoEncontradaException extends RuntimeException {
    private final String internalCode;

    public ModelNaoEncontradaException(ErrorType errorType, Long id) {
        super(errorType.getMessage() + " - ID: " + id);
        this.internalCode = errorType.getInternalCode();
    }

    public String getInternalCode() {
        return internalCode;
    }
}
