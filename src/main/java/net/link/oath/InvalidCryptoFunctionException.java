package net.link.oath;

public class InvalidCryptoFunctionException extends Exception {

    public InvalidCryptoFunctionException(String message) {
        super(message);
    }

    public InvalidCryptoFunctionException(String message, Throwable cause) {
        super(message, cause);
    }
}
