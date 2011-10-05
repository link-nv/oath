package net.link.oath;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OCRA {

    private OCRASuite ocraSuite;
    private byte[] key;
    private int counterLookahead;
    private int timeTolerance;
    private int timeDrift;

    /**
     * Initializes OCRA generator/validator
     *
     * @param ocraSuite        the ocraSuite that determines the policy
     * @param key              the secret key
     * @param counterLookahead the number of steps the client's counter may run ahead
     * @param timeTolerance    the number of timesteps the client's clock can be out of sync
     * @param timeDrift        the number of timesteps the client's clock is already out of sync
     */
    public OCRA(OCRASuite ocraSuite, byte[] key, int counterLookahead, int timeTolerance, int timeDrift) {
        this.ocraSuite = ocraSuite;
        this.key = key;
        this.counterLookahead = counterLookahead;
        this.timeTolerance = timeTolerance;
        this.timeDrift = timeDrift;
    }

    private static final int[] DIGITS_POWER
            // 0 1  2   3    4     5      6       7        8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};


    /**
     * This method generates an OCRA value for the given
     * set of parameters.
     *
     * @param counter            the counter that changes on a per use
     *                           basis
     * @param question           the challenge question
     * @param password           a password that can be used
     * @param sessionInformation Static information that identifies
     *                           the current session
     * @param timeStamp          a value that reflects a time in millis
     * @return A numeric String in base 10 that includes
     *         truncationDigits digits
     * @throws InvalidSessionException  is thrown when the session does not comply to the ocra suite spec
     * @throws InvalidQuestionException is thrown when the question does not comply to the ocrasuite spec
     * @throws InvalidHashException     is thrown when the required password hashing algorithm is not found
     */
    public String generate(
            long counter,
            String question,
            String password,
            String sessionInformation,
            long timeStamp) throws InvalidHashException, InvalidQuestionException, InvalidSessionException {

        // How many digits should we return
        int codeDigits = this.ocraSuite.getCryptoFunction().getTruncation();

        // The size of the byte array message to be encrypted
        // Counter
        String counterString = "";
        int counterLength = 0;
        if (ocraSuite.getDataMode().isCounterMode()) {
            counterString = String.format("%H", counter);
            while (counterString.length() < 16) {
                counterString = "0" + counterString;
            }
            counterLength = 8;
        }

        // Question
        // Question to byte conversion is very badly (as in "not") defined by the spec
        // I guess the reference implementation is the spec here
        String questionConverted = "";
        if (ocraSuite.getDataMode().getqMode().getqType() == QMode.QType.A) {
            questionConverted = Util.asHex(question.getBytes());
        } else if (ocraSuite.getDataMode().getqMode().getqType() == QMode.QType.N) {
            questionConverted = Util.toKey(question);
        } else if (ocraSuite.getDataMode().getqMode().getqType() == QMode.QType.H) {
            // even the reference implementation does not specify this
            // so this is a best guess
            questionConverted = question;
        }
        while (questionConverted.length() < 256) {
            questionConverted = questionConverted + "0";
        }
        byte[] resultQuestion = Util.hexStr2Bytes(questionConverted);
        if (resultQuestion.length > 128) throw new InvalidQuestionException("Question (challenge) too large");
        int questionLength = 128;

        // Password - sha1
        PMode pMode = ocraSuite.getDataMode().getpMode();
        byte[] resultPassword = {};
        if (null != pMode) {
            try {
                MessageDigest digest = MessageDigest.getInstance(pMode.getHash().toString());
                resultPassword = digest.digest(password.getBytes());
            } catch (NoSuchAlgorithmException e) {
                throw new InvalidHashException("Could not initialize the password hashing algorithm", e);
            }
        }

        // sessionInformation
        byte[] resultSession = {};
        int sessionInformationLength = 0;
        if (ocraSuite.getDataMode().getsMode() != null) {
            sessionInformationLength = ocraSuite.getDataMode().getsMode().getLength();
            try {
                resultSession = sessionInformation.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                // this can't happen
                throw new RuntimeException("This can't happen", e);
            }
            if (resultSession.length != sessionInformationLength) {
                throw new InvalidSessionException("The session should have a length of " +
                        sessionInformationLength + " bytes when UTF-8 encoded");
            }
        }

        // TimeStamp
        String timeStampString = "";
        int timeStampLength = 0;
        TMode tMode = ocraSuite.getDataMode().gettMode();
        if (tMode != null) {
            long step = tMode.getSize() * tMode.gettType().millis();
            timeStampString = String.format("%H", timeStamp / step);
            while (timeStampString.length() < 16)
                timeStampString = "0" + timeStampString;
            timeStampLength = 8;
        }

        int ocraSuiteLength = ocraSuite.toString().length();

        // Remember to add "1" for the "00" byte delimiter
        byte[] msg = new byte[ocraSuiteLength +
                counterLength +
                resultQuestion.length +
                resultPassword.length +
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
            bArray = Util.hexStr2Bytes(counterString);
            System.arraycopy(bArray, 0, msg, ocraSuiteLength + 1,
                    bArray.length);
        }

        // Put the bytes of "question" to the message
        System.arraycopy(resultQuestion, 0, msg, ocraSuiteLength + 1 +
                counterLength, resultQuestion.length);


        // Put the bytes of "password" to the message
        if (resultPassword.length > 0) {
            System.arraycopy(resultPassword, 0, msg, ocraSuiteLength + 1 +
                    counterLength + questionLength, resultPassword.length);

        }

        // Put the bytes of "sessionInformation" to the message
        if (sessionInformationLength > 0)

        {
            System.arraycopy(resultSession, 0, msg, ocraSuiteLength + 1 +
                    counterLength + questionLength +
                    resultPassword.length, resultSession.length);
        }

        // Put the bytes of "time" to the message
        // Input is text value of minutes
        if (timeStampLength > 0)

        {
            bArray = Util.hexStr2Bytes(timeStampString);
            System.arraycopy(bArray, 0, msg, ocraSuiteLength + 1 +
                    counterLength + questionLength +
                    resultPassword.length + sessionInformationLength,
                    bArray.length);
        }

        byte[] hash = Util.hmac_sha("Hmac" + ocraSuite.getCryptoFunction().getHash().toString(), key, msg);

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

    /**
     * This function validates a received response
     *
     * @param counter  the counter value of the client
     * @param question the challenge
     * @param password the password
     * @param session  the session information
     * @param time     the current time in millis
     * @param response the response sent by the client
     * @return an object which contains a new counter and a new timeDrift for the client.
     *         These values should be stored if they are used in the policy.
     * @throws InvalidResponseException when the response is out of reach of the lookahead and time tolerance ranges.
     * @throws InvalidHashException     when the password hashing function is not found
     * @throws InvalidQuestionException when the question does not comply to the OCRA suite policy
     * @throws InvalidSessionException  when the session does not comply to the OCRA suite policy
     */
    public OCRAState validate(long counter, String question, String password,
                              String session, long time, String response)
            throws InvalidResponseException, InvalidHashException, InvalidQuestionException, InvalidSessionException {
        int i = 0;
        OCRAState result = new OCRAState();
        long stepSize = 0;
        TMode tMode = ocraSuite.getDataMode().gettMode();
        if (tMode != null) stepSize = tMode.gettType().millis() * tMode.getSize();

        while (i++ <= counterLookahead) {
            for (int t = -timeTolerance; t <= timeTolerance; t++) {
                long testTime = time + (t * timeDrift * stepSize);
                if (generate(counter, question, password, session, testTime).equals(response)) {
                    result.newCounter = counter + i;
                    result.timeSkew = t;
                    return result;
                }
            }
        }
        throw new InvalidResponseException("Provided OCRA response is outside the lookahead and time tolerance ranges");
    }
}
