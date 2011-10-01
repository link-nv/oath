package net.link.oath;

public class TMode {

    private int size = 1;

    private TType tType = TType.M;

    public TMode() {
        // empty
    }

    public TMode(String tModeString) throws InvalidDataModeException {
        if (!tModeString.startsWith("T")) throw new InvalidDataModeException("T mode spec should start with 'T'");
        int stringLength = tModeString.length();
        if (stringLength < 3 || stringLength > 4)
            throw new InvalidDataModeException("T mode spec should be between 3 and 4 characters");
        try {
            size = Integer.parseInt(tModeString.substring(1,stringLength - 2));
        }
        catch (NumberFormatException e) {
            throw new InvalidDataModeException("Invalid amount for T mode spec");
        }
        if (tModeString.charAt(stringLength) == 'S') {
            tType = TType.S;
            if (size < 1 || size > 59)
                throw new InvalidDataModeException("Invalid size for T mode seconds, should be between 1 and 59");
        } else if (tModeString.charAt(stringLength) == 'M') {
            tType = TType.M;
            if (size < 1 || size > 59)
                throw new InvalidDataModeException("Invalid size for T mode minutes, should be between 1 and 59");
        } else if (tModeString.charAt(stringLength) == 'H') {
            tType = TType.H;
            if (size < 0 || size > 48)
                throw new InvalidDataModeException("Invalid size for T mode hours, should be between 1 and 59");
        } else {
            throw new InvalidDataModeException("Invalid T type, should be S, M or H");
        }

    }

    public enum TType {
        S, M, H
    }

}
