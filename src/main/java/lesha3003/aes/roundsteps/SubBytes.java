package lesha3003.aes.roundsteps;

import lesha3003.aes.Aes;
import lesha3003.aes.Sbox;

public final class SubBytes {

    public static byte[][] subBytes(byte[][] state) {
        byte[][] result = new byte[Aes.ROW_COUNT][Aes.COLUMN_COUNT];
        for(int i = 0; i< Aes.ROW_COUNT; i++) {
            for(int j = 0; j<Aes.COLUMN_COUNT; j++) {
                result[i][j] = Sbox.sbox(state[i][j]);
            }
        }
        return result;
    }
    public static byte[][] invSubBytes(byte[][] state) {
        byte[][] result = new byte[Aes.ROW_COUNT][Aes.COLUMN_COUNT];
        for(int i = 0; i< Aes.ROW_COUNT; i++) {
            for(int j = 0; j<Aes.COLUMN_COUNT; j++) {
                result[i][j] = Sbox.reverseSbox(state[i][j]);
            }
        }
        return result;
    }


}
