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

import java.lang.reflect.UndeclaredThrowableException;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;

/**
 * This is an example implementation of the OATH
 * TOTP algorithm.
 * Visit www.openauthentication.org for more information.
 *
 * @author Johan Rydell, PortWise, Inc.
 */

public class TOTP {

    private TOTP() {
    }

    private static final int[] DIGITS_POWER
            // 0 1  2   3    4     5      6       7        8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    /**
     * This method generates a TOTP value for the given
     * set of parameters.
     *
     * @param key:          the shared secret, HEX encoded
     * @param time:         a value that reflects a time
     * @param returnDigits: number of digits to return
     * @return: a numeric String in base 10 that includes
     * truncationDigits digits
     */

    public static String generateTOTP(String key,
                                      String time,
                                      String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA1");
    }

    /**
     * This method generates a TOTP value for the given
     * set of parameters.
     *
     * @param key:          the shared secret, HEX encoded
     * @param time:         a value that reflects a time
     * @param returnDigits: number of digits to return
     * @return: a numeric String in base 10 that includes
     * truncationDigits digits
     */

    public static String generateTOTP256(String key,
                                         String time,
                                         String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA256");
    }

    /**
     * This method generates a TOTP value for the given
     * set of parameters.
     *
     * @param key:          the shared secret, HEX encoded
     * @param time:         a value that reflects a time
     * @param returnDigits: number of digits to return
     * @return: a numeric String in base 10 that includes
     * truncationDigits digits
     */

    public static String generateTOTP512(String key,
                                         String time,
                                         String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA512");
    }

    /**
     * This method generates a TOTP value for the given
     * set of parameters.
     *
     * @param key:          the shared secret, HEX encoded
     * @param time:         a value that reflects a time
     * @param returnDigits: number of digits to return
     * @param crypto:       the crypto function to use
     * @return: a numeric String in base 10 that includes
     * truncationDigits digits
     */

    public static String generateTOTP(String key,
                                      String time,
                                      String returnDigits,
                                      String crypto) {
        int codeDigits = Integer.decode(returnDigits).intValue();
        String result = null;

        // Using the counter
        // First 8 bytes are for the movingFactor
        // Compliant with base RFC 4226 (HOTP)
        while (time.length() < 16)
            time = "0" + time;

        // Get the HEX in a Byte[]
        byte[] msg = Util.hexStr2Bytes(time);
        byte[] k = Util.hexStr2Bytes(key);
        byte[] hash = Util.hmac_sha(crypto, k, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary =
                ((hash[offset] & 0x7f) << 24) |
                        ((hash[offset + 1] & 0xff) << 16) |
                        ((hash[offset + 2] & 0xff) << 8) |
                        (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];

        result = Integer.toString(otp);
        while (result.length() < codeDigits) {
            result = "0" + result;
        }
        return result;
    }
}