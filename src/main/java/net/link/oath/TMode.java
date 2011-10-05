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

        if (tModeString.charAt(stringLength - 1) == 'S') {
            tType = TType.S;
        } else if (tModeString.charAt(stringLength - 1) == 'M') {
            tType = TType.M;
        } else if (tModeString.charAt(stringLength - 1) == 'H') {
            tType = TType.H;
        } else {
            throw new InvalidDataModeException("Invalid T type, should be S, M or H");
        }

        try {
            size = Integer.parseInt(tModeString.substring(1, stringLength - 1));
        } catch (NumberFormatException e) {
            throw new InvalidDataModeException("Invalid amount for T mode spec");
        }
        this.setSize(size);

    }

    public enum TType {
        S("S",1000), M("M",60000), H("H",3600000);

        private String stringRep;
        private long millis;

        TType(String stringRep, long millis) {
            this.stringRep = stringRep;
            this.millis = millis;
        }

        public String toString() {
            return stringRep;
        }

        public long millis() {
            return millis;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) throws InvalidDataModeException {
        if (tType == TType.S && (size < 1 || size > 59))
            throw new InvalidDataModeException("Invalid size for T mode seconds, should be between 1 and 59");
        if (tType == TType.M && (size < 1 || size > 59))
            throw new InvalidDataModeException("Invalid size for T mode minutes, should be between 1 and 59");
        if (tType == TType.H && (size < 0 || size > 48))
            throw new InvalidDataModeException("Invalid size for T mode hours, should be between 0 and 48");
        this.size = size;
    }

    public TType gettType() {
        return tType;
    }

    public void settType(TType tType) throws InvalidDataModeException {
        TType asItWas = this.tType;
        this.tType = tType;
        try {
            this.setSize(this.size);
        } catch (InvalidDataModeException e) {
            this.tType = asItWas;
            throw e;
        }
    }

    public String toString() {
        return "T" + size + tType.toString();
    }
}
