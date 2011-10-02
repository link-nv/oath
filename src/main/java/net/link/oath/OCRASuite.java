package net.link.oath;

import java.util.StringTokenizer;

public class OCRASuite {

    private CryptoFunction cryptoFunction;
    private DataMode dataMode;

    public OCRASuite() {
        // empty
    }

    public OCRASuite(String suiteString) throws InvalidOcraSuiteException, InvalidCryptoFunctionException,
            InvalidHashException, InvalidDataModeException {
        StringTokenizer tokens = new StringTokenizer(suiteString, ":");
        if (tokens.countTokens() != 3)
            throw new InvalidOcraSuiteException("An OCRA suite should consist of 3 parts separated by ':'");

        String ocraVersion = tokens.nextToken();
        if (!ocraVersion.equals("OCRA-1")) throw new InvalidOcraSuiteException("The OCRA version should be OCRA-1");

        cryptoFunction = new CryptoFunction(tokens.nextToken());

        dataMode = new DataMode(tokens.nextToken());

    }

    public CryptoFunction getCryptoFunction() {
        return cryptoFunction;
    }

    public void setCryptoFunction(CryptoFunction cryptoFunction) {
        this.cryptoFunction = cryptoFunction;
    }

    public DataMode getDataMode() {
        return dataMode;
    }

    public void setDataMode(DataMode dataMode) {
        this.dataMode = dataMode;
    }

    public String toString() {
        return "OCRA-1:" + cryptoFunction.toString() + ":" + dataMode.toString();
    }
}
