package com.servidores.projeto.commons.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }
}
