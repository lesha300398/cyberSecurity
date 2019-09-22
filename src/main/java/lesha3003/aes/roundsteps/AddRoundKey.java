package lesha3003.aes.roundsteps;

import lesha3003.aes.Aes;

public final class AddRoundKey {
    private AddRoundKey(){}
    public static void addRoundKey(byte[] state, byte[] subKey) {
        for(int i = 0; i< state.length; i++) {

                state[i] = (byte)(state[i] ^ subKey[i]);

        }
    }
}
