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
            HOTP hotp = new HOTP(secret,6,false,-1,0);
            assert (hotp.validate(0,"755224") == 1);
            assert (hotp.validate(1,"287082") == 2);
            assert (hotp.generateOTP(2).equals("359152"));
            assert (hotp.generateOTP(3).equals("969429"));
            assert (hotp.generateOTP(4).equals("338314"));
            assert (hotp.generateOTP(5).equals("254676"));
            assert (hotp.generateOTP(6).equals("287922"));
            assert (hotp.generateOTP(7).equals("162583"));
            assert (hotp.generateOTP(8).equals("399871"));
            assert (hotp.generateOTP(9).equals("520489"));
        } catch (Exception e) {
            assert (false);
        }
    }

}
