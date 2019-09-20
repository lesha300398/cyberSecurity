package lesha3003.aes;

public final class Utils {
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
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
