package net.link.oath;

import java.util.StringTokenizer;

public class CryptoFunction {

    private Hash hash;
    private int truncation;

    public CryptoFunction() {
        // empty
    }

    public CryptoFunction(String cryptoString) throws InvalidCryptoFunctionException, InvalidHashException {
        StringTokenizer tokens = new StringTokenizer(cryptoString, "-");
        if (tokens.countTokens() != 3)
            throw new InvalidCryptoFunctionException("A cryptofunction should have 3 parts separated by '-'");

        String hotp = tokens.nextToken();
        if (!hotp.equals("HOTP")) throw new InvalidCryptoFunctionException("The algorithm should always be 'HOTP'");

        this.hash = Hash.toHash(tokens.nextToken());

        try {
            truncation = Integer.parseInt(tokens.nextToken());
        } catch (NumberFormatException e) {
            throw new InvalidCryptoFunctionException("The crypto function truncation should be a valid number");
        }
        if (truncation != 0 && truncation < 4 && truncation > 10)
            throw new InvalidCryptoFunctionException("The truncation should be 0 or between 4 and 10");
    }

    public Hash getHash() {
        return hash;
    }

    public void setHash(Hash hash) {
        this.hash = hash;
    }

    public int getTruncation() {
        return truncation;
    }

    public void setTruncation(int truncation) throws InvalidCryptoFunctionException {
        if (truncation != 0 && truncation < 4 && truncation > 10)
            throw new InvalidCryptoFunctionException("The truncation should be 0 or between 4 and 10");
        this.truncation = truncation;
    }

    public String toString() {
        return "HOTP-" + hash.toString() + "-" + truncation;
    }
}
