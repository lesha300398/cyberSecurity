package lesha3003.aes.keyschedule;

import lesha3003.aes.Aes;
import lesha3003.aes.Sbox;

public final class KeySchedule {
    private KeySchedule(){}
    public static byte[][] getSubKeys(byte[] key, Aes.KeySize keySize) {
        if (key.length != keySize.getKeyLengthBits() / 8 ) {
            throw new IllegalArgumentException("Key has incorrect length");
        }
        int constCount;
        int subKeyCount;
        int keyWordCount = keySize.getKeyLengthBits() / 32;
        switch (keySize) {
            case AES_128:
                constCount = 11;
                subKeyCount = 11;
                break;
            case AES_192:
                constCount = 9;
                subKeyCount = 13;
                break;
            case AES_256:
                constCount = 8;
                subKeyCount = 15;
                break;
            default:
                constCount = 0;
                subKeyCount = 0;
                break;
        }
        byte[][] keyWords = new byte[keyWordCount][4];
        for (int i = 0; i < keyWordCount; i++) {
            for (int j = 0; j < 4; j++) {
                keyWords[i][j] = key[i * 4 + j];
            }
        }
        byte[][] roundConstants = getRoundConstants(constCount);
        byte[][] wordsOfSubKeys = new byte[subKeyCount*4][4];
        for (int i = 0; i < wordsOfSubKeys.length; i++) {
            if (i < keyWordCount) {
                wordsOfSubKeys[i] = keyWords[i];
            } else if (i % keyWordCount == 0) {
                wordsOfSubKeys[i] = xorWords(
                        xorWords(
                                wordsOfSubKeys[i-keyWordCount],
                                subWord(rotWord(wordsOfSubKeys[i-1]))
                        ),
                        roundConstants[i/keyWordCount]
                );
            } else if (keyWordCount > 6 && (i % keyWordCount == 4)) {
                wordsOfSubKeys[i] = xorWords(wordsOfSubKeys[i-keyWordCount], subWord(wordsOfSubKeys[i-1]));
            } else {
                wordsOfSubKeys[i] = xorWords(wordsOfSubKeys[i-keyWordCount], wordsOfSubKeys[i-1]);
            }
        }
        byte[][] subKeys = new byte[subKeyCount][16];
        for (int i = 0; i< subKeyCount; i++) {
            for (int j = 0; j< 4; j++){
                for (int k = 0; k < 4; k++){
                    subKeys[i][j*4 + k] = wordsOfSubKeys[i*4 + j][k];
                }
            }
        }
        return subKeys;
    }

    private static byte[][] getRoundConstants(int count) {
        byte[][] result = new byte[count][4];
        for (int i = 0; i < count; i++) {
            byte rc;
            if (i == 0) {
                rc = 0x00;
            } else if (i == 1) {
                rc = 0x01;
            } else if ((result[i-1][0] & 0xFF) < 0x80) {
                rc = (byte) (result[i-1][0] * 2);
            } else {
                rc = (byte) ((result[i-1][0] * 2) ^ 0x1B);
            }
            result[i][0] = rc;
            result[i][1] = 0x00;
            result[i][2] = 0x00;
            result[i][3] = 0x00;
        }
        return result;
    }

    private static byte[] rotWord(byte[] word) {
        if (word.length != 4) {
            throw new IllegalArgumentException("Word has incorrect length");
        }
        return new byte[] {word[1], word[2], word[3], word[0]};
    }
    private static byte[] subWord(byte[] word) {
        if (word.length != 4) {
            throw new IllegalArgumentException("Word has incorrect length");
        }
        byte[] result = new byte[4];
        for(int i = 0; i < result.length; i++) {
            result[i] = Sbox.sbox(word[i]);
        }
        return result;
    }
    private static byte[] xorWords(byte[] word1, byte[] word2) {
        if (word1.length != 4 || word2.length != 4) {
            throw new IllegalArgumentException("Words have incorrect length");
        }
        byte[] result = new byte[4];
        for(int i = 0; i < result.length; i++) {
            result[i] = (byte) (word1[i] ^ word2[i]);
        }
        return result;
    }
}
