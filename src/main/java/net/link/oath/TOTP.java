package net.link.oath;

public class TOTP {

    private byte[] key;
    private int returnDigits;
    private Hash hash;
    private int step;
    private int tolerance;
    private int drift;

    /**
     * Instantiate a new TOTP validator/generator
     *
     * @param key          the shared secret
     * @param returnDigits number of digits to return
     * @param step         the size of one timestep
     * @param tolerance    the number of timesteps a sender can be out of sync
     * @param drift        the known number of timesteps the sender is out of sync (can be negative)
     * @throws InvalidKeyException when the key length is not of size 20, 32 or 64 bytes
     */
    public TOTP(byte[] key, int returnDigits, int step, int tolerance, int drift) throws InvalidKeyException {
        if (key.length == 20) this.hash = Hash.SHA1;
        else if (key.length == 32) this.hash = Hash.SHA256;
        else if (key.length == 64) this.hash = Hash.SHA512;
        else throw new InvalidKeyException("Key length not supported, use a key of size of 20, 32 or 64 bytes");
        this.key = key;
        this.returnDigits = returnDigits;
        this.step = step;
        this.tolerance = tolerance;
        this.drift = drift;
    }

    private static final int[] DIGITS_POWER
            // 0 1  2   3    4     5      6       7        8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    /**
     * This method generates a TOTP value
     *
     * @param time the current time in millis
     * @return a numeric String in base 10 that includes
     *         truncationDigits digits
     */
    public String generate(long time) {

        long resultTime = time / (1000 * step);

        String hexTime = String.format("%H", resultTime);
        while (hexTime.length() < 16) {
            hexTime = "0" + hexTime;
        }

        // Get the HEX in a Byte[]
        byte[] msg = Util.hexStr2Bytes(hexTime);
        byte[] hash = Util.hmac_sha("Hmac" + this.hash.toString(), this.key, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary =
                ((hash[offset] & 0x7f) << 24) |
                        ((hash[offset + 1] & 0xff) << 16) |
                        ((hash[offset + 2] & 0xff) << 8) |
                        (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[returnDigits];

        String result = Integer.toString(otp);
        while (result.length() < returnDigits) {
            result = "0" + result;
        }
        return result;
    }

    /**
     * Validates a received response. The method returns a number of timesteps the sender is out-of-synch. The return
     * value can be stored by the service provider to update the drift factor for this client
     *
     * @param time     the current time in millis
     * @param response the response that was sent by the client
     * @return the number of timesteps the sender is out-of-sync (can be negative) this should be added to the
     *         stored drift factor
     * @throws InvalidResponseException when the response was not within reach
     */
    public int validate(long time, String response) throws InvalidResponseException {
        for (int i = -tolerance; i <= tolerance; i++)
            if (generate(time + (i * drift * step * 1000)).equals(response)) return i;
        throw new InvalidResponseException("Response value out of reach");
    }
}