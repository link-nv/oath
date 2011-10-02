package net.link.oath;

public class QMode {

    public QType qType = QType.N;

    public int length = 8;

    public QMode() {
        // empty
    }

    public QMode(String qModeString) throws InvalidDataModeException {
        if (!qModeString.startsWith("Q")) throw new InvalidDataModeException("Q mode spec should start with 'Q'");
        if (qModeString.length() != 4) throw new InvalidDataModeException("Q mode spec should be 4 characters");
        if (qModeString.charAt(1) == 'A') {
            qType = QType.A;
        } else if (qModeString.charAt(1) == 'N') {
            qType = QType.N;
        } else if (qModeString.charAt(1) == 'H') {
            qType = QType.H;
        } else {
            throw new InvalidDataModeException("Q type should be A, N or H");
        }
        try {
            length = Integer.parseInt(qModeString.substring(2));
        } catch (NumberFormatException e) {
            throw new InvalidDataModeException("Q spec should contain a valid 2 char number between 4 and 64");
        }
        if (length < 4 || length > 64) throw new InvalidDataModeException("Q length should be between 4 and 64");
    }

    public QType getqType() {
        return qType;
    }

    public void setqType(QType qType) {
        this.qType = qType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) throws InvalidDataModeException {
        if (length < 4 || length > 64) throw new InvalidDataModeException("Q length should be between 4 and 64");
        this.length = length;
    }

    public String toString() {
        return String.format("Q%S%02d", qType.toString(),length);
    }

    public enum QType {
        A("A"), N("N"), H("H");

        private String stringRep;

        QType(String stringRep) {
            this.stringRep = stringRep;
        }

        public String toString() {
            return stringRep;
        }
    }
}
