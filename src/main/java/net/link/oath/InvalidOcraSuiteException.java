package net.link.oath;

public class InvalidOcraSuiteException extends Exception {
    InvalidOcraSuiteException(String message) {
        super(message);
    }

    InvalidOcraSuiteException(String message, Throwable cause) {
        super(message,cause);
    }
}
