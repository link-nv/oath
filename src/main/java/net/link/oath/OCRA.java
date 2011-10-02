package net.link.oath;

/**
 Copyright (c) 2011 IETF Trust and the persons identified as
 authors of the code. All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, is permitted pursuant to, and subject to the license
 terms contained in, the Simplified BSD License set forth in Section
 4.c of the IETF Trust's Legal Provisions Relating to IETF Documents
 (http://trustee.ietf.org/license-info).
 */

/**
 * This an example implementation of OCRA.
 * Visit www.openauthentication.org for more information.
 *
 * @author Johan Rydell, PortWise
 */
public class OCRA {

    private OCRA() {
    }

    private static final int[] DIGITS_POWER
            // 0 1  2   3    4     5      6       7        8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};


    /**
     * This method generates an OCRA HOTP value for the given
     * set of parameters.
     *
     * @param ocraSuite          the OCRA Suite
     * @param key                the shared secret, HEX encoded
     * @param counter            the counter that changes on a per use
     *                           basis, HEX encoded
     * @param question           the challenge question, HEX encoded
     * @param password           a password that can be used, HEX encoded
     * @param sessionInformation Static information that identifies
     *                           the current session, Hex encoded
     * @param timeStamp          a value that reflects a time
     * @return A numeric String in base 10 that includes
     *         truncationDigits digits
     */
    static public String generateOCRA(OCRASuite ocraSuite,
                                      String key,
                                      String counter,
                                      String question,
                                      String password,
                                      String sessionInformation,
                                      String timeStamp) {

        int codeDigits;
        int counterLength = 0;
        int passwordLength = 0;
        int sessionInformationLength = 0;
        int timeStampLength = 0;

        // How many digits should we return
        codeDigits = ocraSuite.getCryptoFunction().getTruncation();

        // The size of the byte array message to be encrypted
        // Counter
        if (ocraSuite.getDataMode().isCounterMode()) {
            // Fix the length of the HEX string
            while (counter.length() < 16)
                counter = "0" + counter;
            counterLength = 8;
        }

        while (question.length() < 256)
            question = question + "0";
        int questionLength = 128;

        // Password - sha1
        PMode pMode = ocraSuite.getDataMode().getpMode();
        if (null != pMode) {
            if (pMode.getHash() == Hash.SHA1) {
                while (password.length() < 40)
                    password = "0" + password;
                passwordLength = 20;
            } else if (pMode.getHash() == Hash.SHA256) {
                while (password.length() < 64)
                    password = "0" + password;
                passwordLength = 32;
            } else if (pMode.getHash() == Hash.SHA512) {
                while (password.length() < 128)
                    password = "0" + password;
                passwordLength = 64;
            }
        }

        // sessionInformation
        if (ocraSuite.getDataMode().getsMode() != null) {
            int sLength = ocraSuite.getDataMode().getsMode().getLength();
            while (sessionInformation.length() < (sLength * 2))
                sessionInformation = "0" + sessionInformation;
            sessionInformationLength = sLength;
        }

        // TimeStamp
        if (ocraSuite.getDataMode().gettMode() != null) {
            while (timeStamp.length() < 16)
                timeStamp = "0" + timeStamp;
            timeStampLength = 8;
        }

        int ocraSuiteLength = ocraSuite.toString().length();

        // Remember to add "1" for the "00" byte delimiter
        byte[] msg = new byte[ocraSuiteLength +
                counterLength +
                questionLength +
                passwordLength +
                sessionInformationLength +
                timeStampLength +
                1];

        // Put the bytes of "ocraSuite" parameters into the message
        byte[] bArray = ocraSuite.toString().getBytes();
        System.arraycopy(bArray, 0, msg, 0, bArray.length);

        // Delimiter
        msg[bArray.length] = 0x00;

        // Put the bytes of "Counter" to the message
        // Input is HEX encoded
        if (counterLength > 0) {
            bArray = Util.hexStr2Bytes(counter);
            System.arraycopy(bArray, 0, msg, ocraSuiteLength + 1,
                    bArray.length);
        }

        // Put the bytes of "question" to the message
        // Input is text encoded
        if (questionLength > 0)

        {
            bArray = Util.hexStr2Bytes(question);
            System.arraycopy(bArray, 0, msg, ocraSuiteLength + 1 +
                    counterLength, bArray.length);
        }

        // Put the bytes of "password" to the message
        // Input is HEX encoded
        if (passwordLength > 0)

        {
            bArray = Util.hexStr2Bytes(password);
            System.arraycopy(bArray, 0, msg, ocraSuiteLength + 1 +
                    counterLength + questionLength, bArray.length);

        }

        // Put the bytes of "sessionInformation" to the message
        // Input is text encoded
        if (sessionInformationLength > 0)

        {
            bArray = Util.hexStr2Bytes(sessionInformation);
            System.arraycopy(bArray, 0, msg, ocraSuiteLength + 1 +
                    counterLength + questionLength +
                    passwordLength, bArray.length);
        }

        // Put the bytes of "time" to the message
        // Input is text value of minutes
        if (timeStampLength > 0)

        {
            bArray = Util.hexStr2Bytes(timeStamp);
            System.arraycopy(bArray, 0, msg, ocraSuiteLength + 1 +
                    counterLength + questionLength +
                    passwordLength + sessionInformationLength,
                    bArray.length);
        }

        bArray = Util.hexStr2Bytes(key);

        byte[] hash = Util.hmac_sha("Hmac" + ocraSuite.getCryptoFunction().getHash().toString(), bArray, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary =
                ((hash[offset] & 0x7f) << 24) |
                        ((hash[offset + 1] & 0xff) << 16) |
                        ((hash[offset + 2] & 0xff) << 8) |
                        (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];

        String result = Integer.toString(otp);
        while (result.length() < codeDigits)

        {
            result = "0" + result;
        }

        return result;
    }
}
