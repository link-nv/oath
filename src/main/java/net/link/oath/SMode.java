package net.link.oath;

public class SMode {

    private int length = 64;

    public SMode() {
        // empty
    }

    public SMode(String sModeString) throws InvalidDataModeException {
        if (sModeString.charAt(0) != 'S') throw new InvalidDataModeException("S spec should start with 'S'");
        if (sModeString.length() != 4) throw new InvalidDataModeException("S spec should be 4 characters");
        try {
            Integer.parseInt(sModeString.substring(1));
        }
        catch (NumberFormatException e) {
            throw new InvalidDataModeException("S spec should contain a valid 3 digit length spec");
        }
    }
}
