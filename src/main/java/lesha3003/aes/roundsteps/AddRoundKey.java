package lesha3003.aes.roundsteps;

import lesha3003.aes.Aes;

public final class AddRoundKey {
    public static byte[][] addRoundKey(byte[][] state, byte[][] subKey) {
        byte[][] result = new byte[Aes.ROW_COUNT][Aes.COLUMN_COUNT];
        for(int i = 0; i< Aes.ROW_COUNT; i++) {
            for(int j = 0; j<Aes.COLUMN_COUNT; j++) {
                result[i][j] = (byte)(state[i][j] ^ subKey[i][j]);
            }
        }
        return result;
    }
}
