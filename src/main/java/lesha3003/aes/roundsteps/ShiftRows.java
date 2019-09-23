package lesha3003.aes.roundsteps;

import lesha3003.aes.Aes;

public final class ShiftRows {
    private ShiftRows() {
    }

    public static void shiftRows(byte[] state) {
        byte temp;
        temp = state[1];
        state[1] = state[5];
        state[5] = state[9];
        state[9] = state[13];
        state[13] = temp;

        temp = state[2];
        state[2] = state[10];
        state[10] = temp;
        temp = state[6];
        state[6] = state[14];
        state[14] = temp;

        temp = state[15];
        state[15] = state[11];
        state[11] = state[7];
        state[7] = state[3];
        state[3] = temp;

//        byte[] result = new byte[16];
//        for (int i = 0; i < 16; i++) {
//            result[i] = state[(i % 4 + i / 4) % 4 * 4 + i % 4];
//        }
//        for (int i = 0; i < 16; i++) {
//            state[i] = result[i];
//        }
    }

    public static void invShiftRows(byte[] state) {
        byte temp;
        temp = state[13];
        state[13] = state[9];
        state[9] = state[5];
        state[5] = state[1];
        state[1] = temp;

        temp = state[2];
        state[2] = state[10];
        state[10] = temp;
        temp = state[6];
        state[6] = state[14];
        state[14] = temp;

        temp = state[3];
        state[3] = state[7];
        state[7] = state[11];
        state[11] = state[15];
        state[15] = temp;

//        byte[] result = new byte[16];
//        for (int i = 0; i < 16; i++) {
//            result[(i % 4 + i / 4) % 4 * 4 + i % 4] = state[i];
//        }
//        for (int i = 0; i < 16; i++) {
//            state[i] = result[i];
//        }
    }
}
