package lesha3003.aes.modes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lesha3003.aes.Aes;
import lesha3003.aes.BlockProvider;
import lesha3003.aes.Utils;
import lesha3003.aes.keyschedule.KeySchedule;
import lesha3003.aes.singleblockcipher.SingleBlockCipher;

public final class Xts {
    public static void encrypt(InputStream inputStream, byte[] key1, byte[] key2, byte[] tweak, Aes.KeySize keySize, OutputStream outputStream) throws IOException {
        if (key1.length != keySize.getKeyLengthBits() / 8 || key2.length != keySize.getKeyLengthBits()/8) {
            throw new IllegalArgumentException("Key size does not correspond to selected KrySize");
        }
        byte[][] subKeys1 = KeySchedule.getSubKeys(key1, keySize);
        byte[][] subKeys2 = KeySchedule.getSubKeys(key2, keySize);

        BlockProvider.BlockSequence blocks = BlockProvider.blocksFromInputStream(inputStream, null);

        byte[] encryptedTweak = SingleBlockCipher.encrypt(keySize, tweak, subKeys2);
        byte[] plain;
        byte[] previousCipher = null;
        long blockCount = 1;
        while (true) {
            if (blockCount % 10000 == 0) {
                System.out.println("Block " + blockCount);
            }
            blockCount++;
            plain = blocks.nextBlock();
            if (plain == null) {
                if (previousCipher != null) {
                    outputStream.write(previousCipher);
                }
                break;
            }
            if (plain.length == 16) {
                if (previousCipher != null) {
                    outputStream.write(previousCipher);
                }
                byte[] encryptedPlain = SingleBlockCipher.encrypt(keySize, Utils.xorBytes(plain, encryptedTweak), subKeys1);
                previousCipher = Utils.xorBytes(encryptedPlain, encryptedTweak);
                encryptedTweak = multByAlpha(encryptedTweak);
            } else {
                if (previousCipher == null) { throw new IOException("Too small plain. Minimum size is 1 block(16 bytes)"); }
                byte[] extendedPlain = new byte[16];
                for (int i = 0; i < plain.length; i++) {
                    extendedPlain[i] = plain[i];
                }
                for (int i = plain.length; i < 16; i++) {
                    extendedPlain[i] = previousCipher[i];
                }
                byte[] encryptedPlain = SingleBlockCipher.encrypt(keySize, Utils.xorBytes(extendedPlain, encryptedTweak), subKeys1);
                outputStream.write(Utils.xorBytes(encryptedPlain, encryptedTweak));
                outputStream.write(previousCipher, 0 , plain.length);
                break;
            }
        }
        outputStream.flush();
        outputStream.close();

    }
    public static void decrypt(InputStream inputStream, byte[] key1, byte[] key2, byte[] tweak, Aes.KeySize keySize, OutputStream outputStream) throws IOException {
        if (key1.length != keySize.getKeyLengthBits() / 8 || key2.length != keySize.getKeyLengthBits()/8) {
            throw new IllegalArgumentException("Key size does not correspond to selected KrySize");
        }
        byte[][] subKeys1 = KeySchedule.getSubKeys(key1, keySize);
        byte[][] subKeys2 = KeySchedule.getSubKeys(key2, keySize);

        BlockProvider.BlockSequence blocks = BlockProvider.blocksFromInputStream(inputStream, null);

        byte[] encryptedTweak = SingleBlockCipher.encrypt(keySize, tweak, subKeys2);
        byte[] cipher;
        byte[] previousPlain = null;
        byte[] previousCipher = null;
        byte[] previousEncryptedTweak = null;
        long blockCount = 1;
        while (true) {
            if (blockCount % 10000 == 0) {
                System.out.println("Block " + blockCount);
            }
            blockCount++;
            cipher = blocks.nextBlock();
            if (cipher == null) {
                if (previousPlain != null) {
                    outputStream.write(previousPlain);
                }
                break;
            }
            if (cipher.length == 16) {
                if (previousPlain != null) {
                    outputStream.write(previousPlain);
                }
                byte[] decryptedCipher = SingleBlockCipher.decrypt(keySize, Utils.xorBytes(cipher, encryptedTweak), subKeys1);
                previousPlain = Utils.xorBytes(decryptedCipher, encryptedTweak);
                previousCipher = cipher.clone();
                previousEncryptedTweak = encryptedTweak.clone();
                encryptedTweak = multByAlpha(encryptedTweak);
            } else {
                if (previousPlain == null || previousCipher == null || previousEncryptedTweak == null) { throw new IOException("Too small cipher. Minimum size is 1 block(16 bytes)"); }
                previousPlain = Utils.xorBytes(SingleBlockCipher.decrypt(keySize, Utils.xorBytes(previousCipher, encryptedTweak), subKeys1), encryptedTweak);
                byte[] extendedCipher = new byte[16];
                for (int i = 0; i < cipher.length; i++) {
                    extendedCipher[i] = cipher[i];
                }
                for (int i = cipher.length; i < 16; i++) {
                    extendedCipher[i] = previousPlain[i];
                }
                byte[] decryptedCipher = SingleBlockCipher.decrypt(keySize, Utils.xorBytes(extendedCipher, previousEncryptedTweak), subKeys1);
                outputStream.write(Utils.xorBytes(decryptedCipher, previousEncryptedTweak));
                outputStream.write(previousPlain, 0 , cipher.length);
                break;
            }
        }
        outputStream.flush();
        outputStream.close();
    }
    // https://onlinelibrary.wiley.com/doi/full/10.1002/sec.999
    private static byte[] multByAlpha(byte[] encryptedTweak) {
        byte[] result = new byte[16];
        result[0] = (byte) ((2*((encryptedTweak[0] & 0xFF) % 128)) ^ (135 * ((encryptedTweak[15] & 0xFF)/128)));
        for(int i = 1; i < 16; i++) {
            result[i] = (byte) ((2 * ((encryptedTweak[i] & 0xFF) % 128)) ^ ((encryptedTweak[i - 1] & 0xFF) / 128));
        }
        return result;
    }
}
