package net.link.oath;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class DataMode {

    private boolean counterMode = false;

    private QMode qMode;

    private PMode pMode;

    private SMode sMode;

    private TMode tMode;


    public DataMode() {
        // empty
    }

    public DataMode(String dataModeString) throws InvalidDataModeException {
        StringTokenizer tokens = new StringTokenizer(dataModeString, "-");
        if (1 > tokens.countTokens() && tokens.countTokens() > 5)
            throw new InvalidDataModeException("The data mode should be between 1 and 5 elements separated by '-'");

        String candidate = tokens.nextToken();
        if (candidate.equals("C")) {
            counterMode = true;
            candidate = tokens.nextToken();
        } else {
            counterMode = false;
        }

        qMode = new QMode(candidate);
        try {
            candidate = tokens.nextToken();
        } catch (NoSuchElementException e) {
            return;
        }

        try {
            pMode = new PMode(candidate);
            candidate = tokens.nextToken();
        } catch (NoSuchElementException e) {
            return;
        } catch (Exception e) {
            // swallow
        }
        try {
            sMode = new SMode(candidate);
            candidate = tokens.nextToken();
        } catch (NoSuchElementException e) {
            return;
        } catch (Exception e) {
            // swallow
        }
        try {
            tMode = new TMode(candidate);
        } catch (Exception e) {
            // swallow
        }
        if (tokens.hasMoreTokens()) throw new InvalidDataModeException("There is an illegal token in the data mode");
    }

    public boolean isCounterMode() {
        return counterMode;
    }

    public void setCounterMode(boolean counterMode) {
        this.counterMode = counterMode;
    }

    public QMode getqMode() {
        return qMode;
    }

    public void setqMode(QMode qMode) {
        this.qMode = qMode;
    }

    public PMode getpMode() {
        return pMode;
    }

    public void setpMode(PMode pMode) {
        this.pMode = pMode;
    }

    public SMode getsMode() {
        return sMode;
    }

    public void setsMode(SMode sMode) {
        this.sMode = sMode;
    }

    public TMode gettMode() {
        return tMode;
    }

    public void settMode(TMode tMode) {
        this.tMode = tMode;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        if (true == counterMode) {
            result.append("C-");
        }
        result.append(qMode.toString());
        if (null != pMode) {
            result.append("-");
            result.append(pMode.toString());
        }
        if (null != sMode) {
            result.append("-");
            result.append(sMode.toString());
        }
        if (null != tMode) {
            result.append("-");
            result.append(tMode.toString());
        }
        return result.toString();
    }
}
