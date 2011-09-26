package vl.houthooft.dieter.oath;

import org.junit.Test;

public class TOTPTest {

    @Test
    public void appendixBTest() {
        String[] testTimes = {
                "0000000000000001",
                "00000000023523EC",
                "00000000023523ED",
                "000000000273EF07",
                "0000000003F940AA",
                "0000000027BC86AA"};

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

        String key = "3132333435363738393031323334353637383930";
        String key32 = key + "313233343536373839303132";
        String key64 = key + key + key + "31323334";

        for (int i = 0; i < testTimes.length; i++) {
            assert(TOTP.generateTOTP(key,testTimes[i],"8").equals(sha1Results[i]));
            assert(TOTP.generateTOTP256(key32,testTimes[i],"8").equals(sha256Results[i]));
            assert(TOTP.generateTOTP512(key64,testTimes[i],"8").equals(sha512Results[i]));
        }
    }
}
