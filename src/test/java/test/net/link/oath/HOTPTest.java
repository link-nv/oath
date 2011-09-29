package test.net.link.oath;

import net.link.oath.HOTP;
import org.junit.Test;

public class HOTPTest {

    @Test
    public void appendixDTest() {
        byte[] secret = {
                0x31, 0x32, 0x33, 0x34,
                0x35, 0x36, 0x37, 0x38,
                0x39, 0x30, 0x31, 0x32,
                0x33, 0x34, 0x35, 0x36,
                0x37, 0x38, 0x39, 0x30};

        try {
            assert (HOTP.generateOTP(secret, 0, 6, false, -1).equals("755224"));;
            assert (HOTP.generateOTP(secret, 1, 6, false, -1).equals("287082"));
            assert (HOTP.generateOTP(secret, 2, 6, false, -1).equals("359152"));
            assert (HOTP.generateOTP(secret, 3, 6, false, -1).equals("969429"));
            assert (HOTP.generateOTP(secret, 4, 6, false, -1).equals("338314"));
            assert (HOTP.generateOTP(secret, 5, 6, false, -1).equals("254676"));
            assert (HOTP.generateOTP(secret, 6, 6, false, -1).equals("287922"));
            assert (HOTP.generateOTP(secret, 7, 6, false, -1).equals("162583"));
            assert (HOTP.generateOTP(secret, 8, 6, false, -1).equals("399871"));
            assert (HOTP.generateOTP(secret, 9, 6, false, -1).equals("520489"));
        } catch (Exception e) {
            assert (false);
        }
    }

}
