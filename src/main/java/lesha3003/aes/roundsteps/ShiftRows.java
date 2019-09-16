package lesha3003.aes.roundsteps;

import lesha3003.aes.Aes;

public final class ShiftRows {
    public static byte[][] shiftRows(byte[][] state) {
        byte[][] result = new byte[Aes.ROW_COUNT][Aes.COLUMN_COUNT];
        for(int i = 0; i< Aes.ROW_COUNT; i++) {
            for(int j = 0; j<Aes.COLUMN_COUNT; j++) {

                result[i][j] = state[i][(i+j)%Aes.ROW_COUNT];
            }
        }
        return result;
    }
    public static byte[][] invShiftRows(byte[][] state) {
        byte[][] result = new byte[Aes.ROW_COUNT][Aes.COLUMN_COUNT];
        for(int i = 0; i< Aes.ROW_COUNT; i++) {
            for(int j = 0; j<Aes.COLUMN_COUNT; j++) {

                result[i][(i+j)%Aes.ROW_COUNT] = state[i][j];
            }
        }
        return result;
    }
}
