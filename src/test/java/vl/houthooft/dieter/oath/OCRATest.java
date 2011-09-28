package vl.houthooft.dieter.oath;

import org.junit.Test;

import java.math.BigInteger;

//TODO: implement OCR unit test based on test code in RFC
public class OCRATest {

    private String key20 = "3132333435363738393031323334353637383930";
    private String key32 = key20 + "313233343536373839303132";
    private String key64 = key20 + key20 + key20 + "31323334";

    private String pass = "7110eda4d09e062aa5e4a390b0a572ac0d2c0220";

    private static String toKey(String key) {
        return new String((new BigInteger(key, 10))
                .toString(16)).toUpperCase();
    }

    private static String asHex(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;

        for (i = 0; i < buf.length; i++) {
            if (((int) buf[i] & 0xff) < 0x10)
                strbuf.append("0");
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
        }
        return strbuf.toString();
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

        ocraSuite = "OCRA-1:HOTP-SHA256-8:QN08-PSHA1";

        assert (OCRA.generateOCRA(ocraSuite, key32, "", toKey("00000000"), pass, "", "").equals("83238735"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", toKey("11111111"), pass, "", "").equals("01501458"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", toKey("22222222"), pass, "", "").equals("17957585"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", toKey("33333333"), pass, "", "").equals("86776967"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", toKey("44444444"), pass, "", "").equals("86807031"));

        ocraSuite = "OCRA-1:HOTP-SHA512-8:C-QN08";

        assert (OCRA.generateOCRA(ocraSuite, key64, "00000", toKey("00000000"), "", "", "").equals("07016083"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "00001", toKey("11111111"), "", "", "").equals("63947962"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "00002", toKey("22222222"), "", "", "").equals("70123924"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "00003", toKey("33333333"), "", "", "").equals("25341727"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "00004", toKey("44444444"), "", "", "").equals("33203315"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "00005", toKey("55555555"), "", "", "").equals("34205738"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "00006", toKey("66666666"), "", "", "").equals("44343969"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "00007", toKey("77777777"), "", "", "").equals("51946085"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "00008", toKey("88888888"), "", "", "").equals("20403879"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "00009", toKey("99999999"), "", "", "").equals("31409299"));

        ocraSuite = "OCRA-1:HOTP-SHA512-8:QN08-T1M";

        assert (OCRA.generateOCRA(ocraSuite, key64, "", toKey("00000000"), "", "", "132d0b6").equals("95209754"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", toKey("11111111"), "", "", "132d0b6").equals("55907591"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", toKey("22222222"), "", "", "132d0b6").equals("22048402"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", toKey("33333333"), "", "", "132d0b6").equals("24218844"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", toKey("44444444"), "", "", "132d0b6").equals("36209546"));
    }

    @Test
    public void testMutual() {
        String ocraSuite = "OCRA-1:HOTP-SHA256-8:QA08";

        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("CLI22220SRV11110".getBytes()), "", "", "").equals("28247970"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("CLI22221SRV11111".getBytes()), "", "", "").equals("01984843"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("CLI22222SRV11112".getBytes()), "", "", "").equals("65387857"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("CLI22223SRV11113".getBytes()), "", "", "").equals("03351211"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("CLI22224SRV11114".getBytes()), "", "", "").equals("83412541"));

        ocraSuite = "OCRA-1:HOTP-SHA256-8:QA08";

        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("SRV11110CLI22220".getBytes()), "", "", "").equals("15510767"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("SRV11111CLI22221".getBytes()), "", "", "").equals("90175646"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("SRV11112CLI22222".getBytes()), "", "", "").equals("33777207"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("SRV11113CLI22223".getBytes()), "", "", "").equals("95285278"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("SRV11114CLI22224".getBytes()), "", "", "").equals("28934924"));

        ocraSuite = "OCRA-1:HOTP-SHA512-8:QA08";

        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("CLI22220SRV11110".getBytes()), "", "", "").equals("79496648"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("CLI22221SRV11111".getBytes()), "", "", "").equals("76831980"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("CLI22222SRV11112".getBytes()), "", "", "").equals("12250499"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("CLI22223SRV11113".getBytes()), "", "", "").equals("90856481"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("CLI22224SRV11114".getBytes()), "", "", "").equals("12761449"));

        ocraSuite = "OCRA-1:HOTP-SHA512-8:QA08-PSHA1";

        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("SRV11110CLI22220".getBytes()), pass, "", "").equals("18806276"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("SRV11111CLI22221".getBytes()), pass, "", "").equals("70020315"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("SRV11112CLI22222".getBytes()), pass, "", "").equals("01600026"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("SRV11113CLI22223".getBytes()), pass, "", "").equals("18951020"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("SRV11114CLI22224".getBytes()), pass, "", "").equals("32528969"));
    }

    @Test
    public void testSignature() {
        String ocraSuite = "OCRA-1:HOTP-SHA256-8:QA08";

        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("SIG10000".getBytes()), "", "", "").equals("53095496"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("SIG11000".getBytes()), "", "", "").equals("04110475"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("SIG12000".getBytes()), "", "", "").equals("31331128"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("SIG13000".getBytes()), "", "", "").equals("76028668"));
        assert (OCRA.generateOCRA(ocraSuite, key32, "", asHex("SIG14000".getBytes()), "", "", "").equals("46554205"));

        ocraSuite = "OCRA-1:HOTP-SHA512-8:QA10-T1M";

        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("SIG1000000".getBytes()), "", "", "132d0b6").equals("77537423"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("SIG1100000".getBytes()), "", "", "132d0b6").equals("31970405"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("SIG1200000".getBytes()), "", "", "132d0b6").equals("10235557"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("SIG1300000".getBytes()), "", "", "132d0b6").equals("95213541"));
        assert (OCRA.generateOCRA(ocraSuite, key64, "", asHex("SIG1400000".getBytes()), "", "", "132d0b6").equals("65360607"));
    }
}
