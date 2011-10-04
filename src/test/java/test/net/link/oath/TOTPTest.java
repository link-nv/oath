package test.net.link.oath;

import net.link.oath.TOTP;
import org.junit.Test;

public class TOTPTest {

    @Test
    public void appendixBTest() throws Exception {
        long[] testTimes = {
                59000L,
                1111111109000L,
                1111111111000L,
                1234567890000L,
                2000000000000L,
                20000000000000L};

        String[] sha1Results = {
                "94287082",
                "07081804",
                "14050471",
                "89005924",
                "69279037",
                "65353130"};

        String[] sha256Results = {
                "46119246",
                "68084774",
                "67062674",
                "91819424",
                "90698825",
                "77737706"};

        String[] sha512Results = {
                "90693936",
                "25091201",
                "99943326",
                "93441116",
                "38618901",
                "47863826"};

        String key20 = "12345678901234567890";
        String key32 = key20 + "123456789012";
        String key64 = key20 + key20 + key20 + "1234";

        TOTP totp = new TOTP(key20.getBytes(),8,30,0,0);
        TOTP totp256 = new TOTP(key32.getBytes(),8,30,0,0);
        TOTP totp512 = new TOTP(key64.getBytes(),8,30,0,0);
        for (int i = 0; i < testTimes.length; i++) {
            assert(totp.generateTOTP(testTimes[i]).equals(sha1Results[i]));
            assert(totp256.generateTOTP(testTimes[i]).equals(sha256Results[i]));
            assert(totp512.generateTOTP(testTimes[i]).equals(sha512Results[i]));
        }
    }
}
