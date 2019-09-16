package lesha3003.aes.roundsteps;

import lesha3003.aes.Aes;

public final class MixColumns {
    private static byte mult(byte a, byte b) {
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
    public static byte[][] mixColumns(byte[][] state) {
        byte[][] result = new byte[Aes.ROW_COUNT][Aes.COLUMN_COUNT];
        for(int i = 0; i< Aes.ROW_COUNT; i++) {
            result[0][i] = (byte)(mult((byte) 0x02, state[0][i]) ^ mult((byte) 0x03, state[1][i]) ^ state[2][i] ^ state[3][i]);
            result[1][i] = (byte)(state[0][i] ^ mult((byte) 0x02, state[1][i]) ^ mult((byte)0x03, state[2][i]) ^ state[3][i]);
            result[2][i] = (byte)(state[0][i] ^ state[1][i] ^ mult((byte)0x02, state[2][i]) ^ mult((byte) 0x03, state[3][i]));
            result[3][i] = (byte)(mult((byte) 0x03, state[0][i]) ^ state[1][i] ^ state[2][i] ^ mult((byte) 0x02, state[3][i]));
        }
        return result;
    }
    public static byte[][] invMixColumns(byte[][] state) {
        byte[][] result = new byte[Aes.ROW_COUNT][Aes.COLUMN_COUNT];
        for(int i = 0; i< Aes.ROW_COUNT; i++) {
            result[0][i] = (byte)(mult((byte) 14, state[0][i]) ^ mult((byte) 11, state[1][i]) ^ mult((byte) 13, state[2][i]) ^ mult((byte) 9, state[3][i]));
            result[1][i] = (byte)(mult((byte) 9, state[0][i]) ^ mult((byte) 14, state[1][i]) ^ mult((byte) 11, state[2][i]) ^ mult((byte) 13, state[3][i]));
            result[2][i] = (byte)(mult((byte) 13, state[0][i]) ^ mult((byte) 9, state[1][i]) ^ mult((byte) 14, state[2][i]) ^ mult((byte) 11, state[3][i]));
            result[3][i] = (byte)(mult((byte) 11, state[0][i]) ^ mult((byte) 13, state[1][i]) ^ mult((byte) 9, state[2][i]) ^ mult((byte) 14, state[3][i]));
        }
        return result;
    }
}
