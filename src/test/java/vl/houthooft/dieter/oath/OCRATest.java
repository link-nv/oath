package vl.houthooft.dieter.oath;

import org.junit.Test;

import java.math.BigInteger;

//TODO: implement OCR unit test based on test code in RFC
public class OCRATest {

    private String key20 = "3132333435363738393031323334353637383930";
    private String key32 = key20 + "313233343536373839303132";
    private String key64 = key20 + key20 + key20 + "31323334";

    private String toKey(String key) {
        return new String((new BigInteger(key, 10))
                .toString(16)).toUpperCase();
    }

    //TODO: not complete yet
    @Test
    public void testOneWay() {
        String ocraSuite = "OCRA-1:HOTP-SHA1-6:QN08";

        assert (OCRA.generateOCRA(ocraSuite, key20, "", toKey("00000000"), "", "", "").equals("237653"));
        assert (OCRA.generateOCRA(ocraSuite, key20, "", toKey("11111111"), "", "", "").equals("243178"));
        assert (OCRA.generateOCRA(ocraSuite, key20, "", toKey("22222222"), "", "", "").equals("653583"));
        assert (OCRA.generateOCRA(ocraSuite, key20, "", toKey("33333333"), "", "", "").equals("740991"));
        assert (OCRA.generateOCRA(ocraSuite, key20, "", toKey("44444444"), "", "", "").equals("608993"));
        assert (OCRA.generateOCRA(ocraSuite, key20, "", toKey("55555555"), "", "", "").equals("388898"));
        assert (OCRA.generateOCRA(ocraSuite, key20, "", toKey("66666666"), "", "", "").equals("816933"));
        assert (OCRA.generateOCRA(ocraSuite, key20, "", toKey("77777777"), "", "", "").equals("224598"));
        assert (OCRA.generateOCRA(ocraSuite, key20, "", toKey("88888888"), "", "", "").equals("750600"));
        assert (OCRA.generateOCRA(ocraSuite, key20, "", toKey("99999999"), "", "", "").equals("294470"));

        ocraSuite = "OCRA-1:HOTP-SHA256-8:C-QN08-PSHA1";
        String pass = "7110eda4d09e062aa5e4a390b0a572ac0d2c0220";

        assert (OCRA.generateOCRA(ocraSuite, key32, "0", toKey("12345678"), pass, "", "").equals("65347737"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "1", toKey("12345678"), pass, "", "").equals("86775851"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "2", toKey("12345678"), pass, "", "").equals("78192410"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "3", toKey("12345678"), pass, "", "").equals("71565254"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "4", toKey("12345678"), pass, "", "").equals("10104329"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "5", toKey("12345678"), pass, "", "").equals("65983500"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "6", toKey("12345678"), pass, "", "").equals("70069104"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "7", toKey("12345678"), pass, "", "").equals("91771096"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "8", toKey("12345678"), pass, "", "").equals("75011558"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "9", toKey("12345678"), pass, "", "").equals("08522129"));
    }

}
