package lesha3003.aes.roundsteps;

import lesha3003.aes.Aes;
import lesha3003.aes.Sbox;

public final class SubBytes {
    private SubBytes(){}
    public static void subBytes(byte[] state) {
        for(int i = 0; i< 16; i++) {
                state[i] = Sbox.sbox(state[i]);
        }
    }
    public static void invSubBytes(byte[] state) {
        for(int i = 0; i< 16; i++) {
            state[i] = Sbox.reverseSbox(state[i]);
        }
    }


}
