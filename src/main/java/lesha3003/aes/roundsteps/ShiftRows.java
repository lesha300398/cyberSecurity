package lesha3003.aes.roundsteps;

import lesha3003.aes.Aes;

public final class ShiftRows {
    private ShiftRows() {
    }

    public static void shiftRows(byte[] state) {
        byte[] result = new byte[16];
        for (int i = 0; i < 16; i++) {
            result[i] = state[(i % 4 + i / 4) % 4 * 4 + i % 4];
        }
        for (int i = 0; i < 16; i++) {
            state[i] = result[i];
        }
    }

    public static void invShiftRows(byte[] state) {
        byte[] result = new byte[16];
        for (int i = 0; i < 16; i++) {
            result[(i % 4 + i / 4) % 4 * 4 + i % 4] = state[i];
        }
        for (int i = 0; i < 16; i++) {
            state[i] = result[i];
        }
    }
}
