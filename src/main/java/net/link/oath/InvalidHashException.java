package net.link.oath;

public class InvalidHashException extends Exception {

    public InvalidHashException(String message) {
        super(message);
    }

    public InvalidHashException(String message, Throwable cause) {
        super(message,cause);
    }
}
