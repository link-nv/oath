package test.net.link.oath;

import net.link.oath.OCRA;
import net.link.oath.OCRAState;
import net.link.oath.OCRASuite;
import net.link.oath.Util;
import org.junit.Test;

import java.math.BigInteger;

public class OCRATest {

    private String key20 = "12345678901234567890";
    private String key32 = key20 + "123456789012";
    private String key64 = key20 + key20 + key20 + "1234";

    private String pass = "1234";

    @Test
    public void testOneWay() throws Exception {
        OCRASuite ocraSuite = new OCRASuite("OCRA-1:HOTP-SHA1-6:QN08");
        OCRA ocra = new OCRA(ocraSuite,key20.getBytes(),0,0,0);

        OCRAState state = ocra.validate(0, "00000000", "", "", 0, "237653");
        assert (state.newCounter == 1 && state.timeSkew == 0);
        assert (ocra.generate(0, "11111111", "", "", 0).equals("243178"));
        assert (ocra.generate(0, "22222222", "", "", 0).equals("653583"));
        assert (ocra.generate(0, "33333333", "", "", 0).equals("740991"));
        assert (ocra.generate(0, "44444444", "", "", 0).equals("608993"));
        assert (ocra.generate(0, "55555555", "", "", 0).equals("388898"));
        assert (ocra.generate(0, "66666666", "", "", 0).equals("816933"));
        assert (ocra.generate(0, "77777777", "", "", 0).equals("224598"));
        assert (ocra.generate(0, "88888888", "", "", 0).equals("750600"));
        assert (ocra.generate(0, "99999999", "", "", 0).equals("294470"));

        ocraSuite = new OCRASuite("OCRA-1:HOTP-SHA256-8:C-QN08-PSHA1");
        ocra = new OCRA(ocraSuite,key32.getBytes(),0,0,0);

        assert (ocra.generate(0, "12345678", pass, "", 0).equals("65347737"));
        assert (ocra.generate(1, "12345678", pass, "", 0).equals("86775851"));
        assert (ocra.generate(2, "12345678", pass, "", 0).equals("78192410"));
        assert (ocra.generate(3, "12345678", pass, "", 0).equals("71565254"));
        assert (ocra.generate(4, "12345678", pass, "", 0).equals("10104329"));
        assert (ocra.generate(5, "12345678", pass, "", 0).equals("65983500"));
        assert (ocra.generate(6, "12345678", pass, "", 0).equals("70069104"));
        assert (ocra.generate(7, "12345678", pass, "", 0).equals("91771096"));
        assert (ocra.generate(8, "12345678", pass, "", 0).equals("75011558"));
        assert (ocra.generate(9, "12345678", pass, "", 0).equals("08522129"));

        ocraSuite = new OCRASuite("OCRA-1:HOTP-SHA256-8:QN08-PSHA1");
        ocra = new OCRA(ocraSuite,key32.getBytes(),0,0,0);

        assert (ocra.generate(0, "00000000", pass, "", 0).equals("83238735"));
        assert (ocra.generate(0, "11111111", pass, "", 0).equals("01501458"));
        assert (ocra.generate(0, "22222222", pass, "", 0).equals("17957585"));
        assert (ocra.generate(0, "33333333", pass, "", 0).equals("86776967"));
        assert (ocra.generate(0, "44444444", pass, "", 0).equals("86807031"));

        ocraSuite = new OCRASuite("OCRA-1:HOTP-SHA512-8:C-QN08");
        ocra = new OCRA(ocraSuite,key64.getBytes(),0,0,0);

        assert (ocra.generate(0, "00000000", "", "", 0).equals("07016083"));
        assert (ocra.generate(1, "11111111", "", "", 0).equals("63947962"));
        assert (ocra.generate(2, "22222222", "", "", 0).equals("70123924"));
        assert (ocra.generate(3, "33333333", "", "", 0).equals("25341727"));
        assert (ocra.generate(4, "44444444", "", "", 0).equals("33203315"));
        assert (ocra.generate(5, "55555555", "", "", 0).equals("34205738"));
        assert (ocra.generate(6, "66666666", "", "", 0).equals("44343969"));
        assert (ocra.generate(7, "77777777", "", "", 0).equals("51946085"));
        assert (ocra.generate(8, "88888888", "", "", 0).equals("20403879"));
        assert (ocra.generate(9, "99999999", "", "", 0).equals("31409299"));

        ocraSuite = new OCRASuite("OCRA-1:HOTP-SHA512-8:QN08-T1M");
        ocra = new OCRA(ocraSuite,key64.getBytes(),0,0,0);

        assert (ocra.generate(0, "00000000", "", "", 1206446760000l).equals("95209754"));
        assert (ocra.generate(0, "11111111", "", "", 1206446760000l).equals("55907591"));
        assert (ocra.generate(0, "22222222", "", "", 1206446760000l).equals("22048402"));
        assert (ocra.generate(0, "33333333", "", "", 1206446760000l).equals("24218844"));
        assert (ocra.generate(0, "44444444", "", "", 1206446760000l).equals("36209546"));
    }

    @Test
    public void testMutual() throws Exception {
        OCRASuite ocraSuite = new OCRASuite("OCRA-1:HOTP-SHA256-8:QA08");
        OCRA ocra = new OCRA(ocraSuite,key32.getBytes(),0,0,0);

        assert (ocra.generate(0, "CLI22220SRV11110", "", "", 0).equals("28247970"));
        assert (ocra.generate(0, "CLI22221SRV11111", "", "", 0).equals("01984843"));
        assert (ocra.generate(0, "CLI22222SRV11112", "", "", 0).equals("65387857"));
        assert (ocra.generate(0, "CLI22223SRV11113", "", "", 0).equals("03351211"));
        assert (ocra.generate(0, "CLI22224SRV11114", "", "", 0).equals("83412541"));

        ocraSuite = new OCRASuite("OCRA-1:HOTP-SHA256-8:QA08");
        ocra = new OCRA(ocraSuite,key32.getBytes(),0,0,0);

        assert (ocra.generate(0,"SRV11110CLI22220", "", "", 0).equals("15510767"));
        assert (ocra.generate(0, "SRV11111CLI22221", "", "", 0).equals("90175646"));
        assert (ocra.generate(0, "SRV11112CLI22222", "", "", 0).equals("33777207"));
        assert (ocra.generate(0, "SRV11113CLI22223", "", "", 0).equals("95285278"));
        assert (ocra.generate(0, "SRV11114CLI22224", "", "", 0).equals("28934924"));

        ocraSuite = new OCRASuite("OCRA-1:HOTP-SHA512-8:QA08");
        ocra = new OCRA(ocraSuite,key64.getBytes(),0,0,0);

        assert (ocra.generate(0,"CLI22220SRV11110", "", "", 0).equals("79496648"));
        assert (ocra.generate(0, "CLI22221SRV11111", "", "", 0).equals("76831980"));
        assert (ocra.generate(0, "CLI22222SRV11112", "", "", 0).equals("12250499"));
        assert (ocra.generate(0, "CLI22223SRV11113", "", "", 0).equals("90856481"));
        assert (ocra.generate(0, "CLI22224SRV11114", "", "", 0).equals("12761449"));

        ocraSuite = new OCRASuite("OCRA-1:HOTP-SHA512-8:QA08-PSHA1");
        ocra = new OCRA(ocraSuite,key64.getBytes(),0,0,0);

        assert (ocra.generate(0,"SRV11110CLI22220", pass, "", 0).equals("18806276"));
        assert (ocra.generate(0, "SRV11111CLI22221", pass, "", 0).equals("70020315"));
        assert (ocra.generate(0, "SRV11112CLI22222", pass, "", 0).equals("01600026"));
        assert (ocra.generate(0, "SRV11113CLI22223", pass, "", 0).equals("18951020"));
        assert (ocra.generate(0, "SRV11114CLI22224", pass, "", 0).equals("32528969"));
    }

    @Test
    public void testSignature() throws Exception {
        OCRASuite ocraSuite = new OCRASuite("OCRA-1:HOTP-SHA256-8:QA08");
        OCRA ocra = new OCRA(ocraSuite,key32.getBytes(),0,0,0);

        assert (ocra.generate(0,"SIG10000", "", "", 0).equals("53095496"));
        assert (ocra.generate(0, "SIG11000", "", "", 0).equals("04110475"));
        assert (ocra.generate(0, "SIG12000", "", "", 0).equals("31331128"));
        assert (ocra.generate(0, "SIG13000", "", "", 0).equals("76028668"));
        assert (ocra.generate(0, "SIG14000", "", "", 0).equals("46554205"));

        ocraSuite = new OCRASuite("OCRA-1:HOTP-SHA512-8:QA10-T1M");
        ocra = new OCRA(ocraSuite,key64.getBytes(),0,0,0);

        assert (ocra.generate(0,"SIG1000000", "", "", 1206446760000l).equals("77537423"));
        assert (ocra.generate(0, "SIG1100000", "", "", 1206446760000l).equals("31970405"));
        assert (ocra.generate(0, "SIG1200000", "", "", 1206446760000l).equals("10235557"));
        assert (ocra.generate(0, "SIG1300000", "", "", 1206446760000l).equals("95213541"));
        assert (ocra.generate(0, "SIG1400000", "", "", 1206446760000l).equals("65360607"));
    }
}
