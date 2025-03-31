package com.servidores.projeto.commons.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
