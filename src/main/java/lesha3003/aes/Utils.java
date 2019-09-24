package lesha3003.aes;


import java.nio.ByteBuffer;


public final class Utils {
    private Utils(){}
    public static byte[][] bytesToMatrix(byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException("Wrong length");
        }
        byte[][] result = new byte[4][4];
        for (int i = 0; i < bytes.length; i++) {
            result[i%4][i/4] = bytes[i];
        }
        return result;
    }
    public static byte[] matrixToBytes(byte[][] matrix) {
//        if (matrix.length != 4 || matrix[0].length != 4) {
//            throw  new IllegalArgumentException("Wrong matrix dimensions");
//        }
        byte[] result = new byte[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[j*4 + i] = matrix[i][j];
            }
        }
        return result;
    }
    public static byte gfMult(byte a, byte b) {
        byte p = 0;

        for (int counter = 0; counter < 8; counter++) {
            if ((b & 1) != 0) {
                p ^= a;
            }

            boolean hi_bit_set = (a & 0x80) != 0;
            a <<= 1;
            if (hi_bit_set) {
                a ^= 0x11B; /* x^8 + x^4 + x^3 + x + 1 */
            }
            b >>= 1;
        }

        return p;
    }
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
    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    /**
     * Returns byte array containing bytes of b1 XORed  with bytes of b2. Length of resulting array is length of b1.
     * @param b1
     * @param b2
     * @return
     */
    public static byte[] xorBytes(byte[] b1, byte[] b2) {
        if (b1.length > b2.length) {
            throw new IllegalArgumentException("b1.length > b2.length");
        }
        int length = b1.length;
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = (byte) (b1[i] ^ b2[i]);
        }
        return result;
    }
}
