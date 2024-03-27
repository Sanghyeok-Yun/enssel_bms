package com.enssel.bms.storage.exception;

import java.io.IOException;

public class FileException extends IOException {
    public FileException(String message) {
        super(message);
    }
    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
