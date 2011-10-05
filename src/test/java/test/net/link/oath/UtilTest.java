package test.net.link.oath;

import net.link.oath.Util;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testZeroPad() {
        byte[] dst = new byte[4];
        byte[] src = { 0x1, 0x2};
        byte[] expected = {0x1, 0x2, 0x0, 0x0};

        assert(compareBytes(Util.zeroPad(src,dst),expected));
    }

    private boolean compareBytes(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }

}
