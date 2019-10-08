package lesha3003.sha;


import java.nio.ByteBuffer;


public final class Utils {
    private Utils(){}

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public  static String byteArrayToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public  static String byteArrayToHexStringLittleEndian(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = Integer.reverse(bytes[j] & 0xFF) >>> 24;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static byte[] stringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len];
        for (int i = 0; i < len; i++) {
            data[i] = (byte) s.charAt(i);
        }
        return data;
    }

    /**
     * Converts the given byte array into an long array via big-endian conversion
     * (8 bytes become 1 long).
     *
     * @param bytes The source array.
     * @return The converted array.
     */
    public static long[] bytesToLongs(byte[] bytes) {
        if (bytes.length % Long.BYTES != 0) {
            throw new IllegalArgumentException("byte array length");
        }

        ByteBuffer buf = ByteBuffer.wrap(bytes);
        long[] result = new long[bytes.length / Long.BYTES];
        for (int i = 0; i < result.length; ++i) {
            result[i] = buf.getLong();
        }

        return result;
    }

    /**
     * Converts the given long array into a byte array via big-endian conversion
     * (1 long becomes 8 bytes).
     *
     * @param longs The source array.
     * @return The converted array.
     */
    public static byte[] longsToBytes(long[] longs) {
        ByteBuffer buf = ByteBuffer.allocate(longs.length * Long.BYTES);

        for (int i = 0; i < longs.length; ++i) {
            buf.putLong(longs[i]);
        }
        long x = 1;

        return buf.array();
    }
}
