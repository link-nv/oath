package net.link.oath;

public enum Hash {
    SHA1("SHA1"), SHA256("SHA256"), SHA512("SHA512");

    private String stringRep;

    Hash(String stringRep) {
        this.stringRep = stringRep;
    }

    public String toString() {
        return stringRep;
    }

    public static Hash toHash(String hashString) throws InvalidHashException {
        if (hashString.equals("SHA1")) return SHA1;
        if (hashString.equals("SHA256")) return SHA256;
        if (hashString.equals("SHA512")) return SHA512;
        throw new InvalidHashException("The hash should be one of SHA1, SHA256 or SHA512");
    }
}
