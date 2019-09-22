package lesha3003.aes.roundsteps;

import lesha3003.aes.Aes;
import lesha3003.aes.Utils;

public final class MixColumns {
    private MixColumns(){}
    public static void mixColumns(byte[] state) {
        byte[] result = new byte[16];
        for(int i = 0; i < 4; i++) {
            result[4*i] = (byte)(Utils.gfMult((byte) 0x02, state[i*4]) ^ Utils.gfMult((byte) 0x03, state[i*4 + 1]) ^ state[i*4 + 2] ^ state[i*4 + 3]);
            result[4*i+1] = (byte)(state[4*i] ^ Utils.gfMult((byte) 0x02, state[4*i + 1]) ^ Utils.gfMult((byte)0x03, state[4*i + 2]) ^ state[4*i + 3]);
            result[4*i+2] = (byte)(state[4*i] ^ state[4*i + 1] ^ Utils.gfMult((byte)0x02, state[4*i + 2]) ^ Utils.gfMult((byte) 0x03, state[4*i + 3]));
            result[4*i+3] = (byte)(Utils.gfMult((byte) 0x03, state[4*i]) ^ state[4*i + 1] ^ state[4*i + 2] ^ Utils.gfMult((byte) 0x02, state[4*i + 3]));
        }
        for(int i = 0; i< 16; i++) {
            state[i] = result[i];
        }
    }
    public static void invMixColumns(byte[] state) {
        byte[] result = new byte[16];
        for(int i = 0; i< Aes.ROW_COUNT; i++) {
            result[4*i] = (byte)(Utils.gfMult((byte) 14, state[4*i]) ^ Utils.gfMult((byte) 11, state[i*4 + 1]) ^ Utils.gfMult((byte) 13, state[i*4 + 2]) ^ Utils.gfMult((byte) 9, state[i*4 + 3]));
            result[4*i + 1] = (byte)(Utils.gfMult((byte) 9, state[4*i]) ^ Utils.gfMult((byte) 14, state[i*4 + 1]) ^ Utils.gfMult((byte) 11, state[i*4 + 2]) ^ Utils.gfMult((byte) 13, state[i*4 + 3]));
            result[4*i + 2] = (byte)(Utils.gfMult((byte) 13, state[4*i]) ^ Utils.gfMult((byte) 9, state[i*4 + 1]) ^ Utils.gfMult((byte) 14, state[i*4 + 2]) ^ Utils.gfMult((byte) 11, state[i*4 + 3]));
            result[4*i + 3] = (byte)(Utils.gfMult((byte) 11, state[4*i]) ^ Utils.gfMult((byte) 13, state[i*4 + 1]) ^ Utils.gfMult((byte) 9, state[i*4 + 2]) ^ Utils.gfMult((byte) 14, state[i*4 + 3]));
        }
        for(int i = 0; i< 16; i++) {
            state[i] = result[i];
        }
    }
}
