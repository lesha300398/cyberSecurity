package lesha3003.aes.roundsteps;

import lesha3003.aes.Aes;
import lesha3003.aes.Utils;

public final class MixColumns {
    private MixColumns(){}
    public static byte[][] mixColumns(byte[][] state) {
        byte[][] result = new byte[Aes.ROW_COUNT][Aes.COLUMN_COUNT];
        for(int i = 0; i< Aes.ROW_COUNT; i++) {
            result[0][i] = (byte)(Utils.gfMult((byte) 0x02, state[0][i]) ^ Utils.gfMult((byte) 0x03, state[1][i]) ^ state[2][i] ^ state[3][i]);
            result[1][i] = (byte)(state[0][i] ^ Utils.gfMult((byte) 0x02, state[1][i]) ^ Utils.gfMult((byte)0x03, state[2][i]) ^ state[3][i]);
            result[2][i] = (byte)(state[0][i] ^ state[1][i] ^ Utils.gfMult((byte)0x02, state[2][i]) ^ Utils.gfMult((byte) 0x03, state[3][i]));
            result[3][i] = (byte)(Utils.gfMult((byte) 0x03, state[0][i]) ^ state[1][i] ^ state[2][i] ^ Utils.gfMult((byte) 0x02, state[3][i]));
        }
        return result;
    }
    public static byte[][] invMixColumns(byte[][] state) {
        byte[][] result = new byte[Aes.ROW_COUNT][Aes.COLUMN_COUNT];
        for(int i = 0; i< Aes.ROW_COUNT; i++) {
            result[0][i] = (byte)(Utils.gfMult((byte) 14, state[0][i]) ^ Utils.gfMult((byte) 11, state[1][i]) ^ Utils.gfMult((byte) 13, state[2][i]) ^ Utils.gfMult((byte) 9, state[3][i]));
            result[1][i] = (byte)(Utils.gfMult((byte) 9, state[0][i]) ^ Utils.gfMult((byte) 14, state[1][i]) ^ Utils.gfMult((byte) 11, state[2][i]) ^ Utils.gfMult((byte) 13, state[3][i]));
            result[2][i] = (byte)(Utils.gfMult((byte) 13, state[0][i]) ^ Utils.gfMult((byte) 9, state[1][i]) ^ Utils.gfMult((byte) 14, state[2][i]) ^ Utils.gfMult((byte) 11, state[3][i]));
            result[3][i] = (byte)(Utils.gfMult((byte) 11, state[0][i]) ^ Utils.gfMult((byte) 13, state[1][i]) ^ Utils.gfMult((byte) 9, state[2][i]) ^ Utils.gfMult((byte) 14, state[3][i]));
        }
        return result;
    }
}
