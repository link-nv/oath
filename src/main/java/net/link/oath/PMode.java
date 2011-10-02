package net.link.oath;

public class PMode {

    private Hash hash = Hash.SHA1;

    public PMode() {
        // empty
    }

    public PMode(String pModeString) throws InvalidDataModeException, InvalidHashException {
        if (pModeString.charAt(0) != 'P') throw new InvalidDataModeException("P mode spec should start with P");
        hash = Hash.toHash(pModeString.substring(1));
    }

    public Hash getHash() {
        return hash;
    }

    public void setHash(Hash hash) {
        this.hash = hash;
    }

    public String toString() {
        return "P" + hash.toString();
    }
}
