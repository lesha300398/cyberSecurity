package lesha3003.aes.singleblockcipher;

import lesha3003.aes.Aes;
import lesha3003.aes.roundsteps.AddRoundKey;
import lesha3003.aes.roundsteps.MixColumns;
import lesha3003.aes.roundsteps.ShiftRows;
import lesha3003.aes.roundsteps.SubBytes;

public final class SingleBlockCipher {
    public static byte[][] encrypt(Aes.KeySize keySize, byte[][] plain, byte[][][] subKeys) {
        int rounds;
        switch (keySize) {
            case AES_128:
                rounds = 10;
                break;
            case AES_192:
                rounds = 12;
                break;
            case AES_256:
                rounds = 14;
                break;
            default:
                rounds = 0;
                break;
        }
        if (subKeys.length != rounds + 1) {
            throw new IllegalArgumentException("Subkey count does not correspond to AES keySize");
        }
        byte[][] state = plain;
        state = AddRoundKey.addRoundKey(state, subKeys[0]);
        for (int i = 1; i < rounds; i++) {
            state = SubBytes.subBytes(state);
            state = ShiftRows.shiftRows(state);
            state = MixColumns.mixColumns(state);
            state = AddRoundKey.addRoundKey(state, subKeys[i]);
        }
        state = SubBytes.subBytes(state);
        state = ShiftRows.shiftRows(state);
        state = AddRoundKey.addRoundKey(state, subKeys[rounds]);
        return state;
    }

    public static byte[][] decrypt(Aes.KeySize keySize, byte[][] cipher, byte[][][] subKeys) {
        int rounds;
        switch (keySize) {
            case AES_128:
                rounds = 10;
                break;
            case AES_192:
                rounds = 12;
                break;
            case AES_256:
                rounds = 14;
                break;
            default:
                rounds = 0;
                break;
        }
        if (subKeys.length != rounds + 1) {
            throw new IllegalArgumentException("Subkey count does not correspond to AES keySize");
        }
        byte[][] state = cipher;
        state = AddRoundKey.addRoundKey(state, subKeys[rounds]);
        state = ShiftRows.invShiftRows(state);
        state = SubBytes.invSubBytes(state);
        for (int i = rounds - 1; i >= 1; i--) {
            state = AddRoundKey.addRoundKey(state, subKeys[i]);
            state = MixColumns.invMixColumns(state);
            state = ShiftRows.invShiftRows(state);
            state = SubBytes.invSubBytes(state);
        }
        state = AddRoundKey.addRoundKey(state, subKeys[0]);
        return state;
    }
}
