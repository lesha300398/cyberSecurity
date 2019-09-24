package lesha3003.aes.modes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

import lesha3003.aes.Aes;
import lesha3003.aes.BlockProvider;
import lesha3003.aes.Utils;
import lesha3003.aes.keyschedule.KeySchedule;
import lesha3003.aes.singleblockcipher.SingleBlockCipher;

public final class Ctr {
    public static void encrypt(InputStream inputStream, byte[] key, Aes.KeySize keySize, OutputStream outputStream) throws IOException {
        if (key.length != keySize.getKeyLengthBits() / 8) {
            throw new IllegalArgumentException("Key size does not correspond to selected KrySize");
        }
        byte[][] subKeys = KeySchedule.getSubKeys(key, keySize);
        long timestamp = (new Date()).getTime();
        int seconds = (int) timestamp / 1000;
        int milliseconds = (int) timestamp % 1000;
        int random = (new Random()).nextInt();
        byte[] nonce = {
                (byte) milliseconds, (byte) (milliseconds >>> 8),
                (byte) random, (byte) (random >>> 8),
                (byte) seconds, (byte) (seconds >>> 8), (byte) (seconds >>> 16), (byte) (seconds >>> 24),
        };

        BlockProvider.BlockSequence blocks = BlockProvider.blocksFromInputStream(inputStream, null);

        outputStream.write(nonce);
        long counter = 0;
        byte[] plain;
        while ((plain = blocks.nextBlock()) != null) {
            byte[] counterBlock = getCounterBlock(nonce, counter);
            byte[] outputBlock = SingleBlockCipher.encrypt(keySize, counterBlock, subKeys);

            byte[] cipher = Utils.xorBytes(plain, outputBlock);
            outputStream.write(cipher);
            counter++;
            if (counter % 10000 == 0) {
                System.out.println("Block " + counter);
            }
        }
        outputStream.flush();
        outputStream.close();
    }

    public static void decrypt(InputStream inputStream, byte[] key, Aes.KeySize keySize, OutputStream outputStream) throws IOException {
        if (key.length != keySize.getKeyLengthBits() / 8) {
            throw new IllegalArgumentException("Key size does not correspond to selected KrySize");
        }
        byte[][] subKeys = KeySchedule.getSubKeys(key, keySize);
        byte[] nonce = new byte[8];
        inputStream.read(nonce);
        BlockProvider.BlockSequence blocks = BlockProvider.blocksFromInputStream(inputStream, null);

        long counter = 0;
        byte[] cipher;
        while ((cipher = blocks.nextBlock()) != null) {
            byte[] counterBlock = getCounterBlock(nonce, counter);
            byte[] outputBlock = SingleBlockCipher.encrypt(keySize, counterBlock, subKeys);

            byte[] plain = Utils.xorBytes(cipher, outputBlock);
            outputStream.write(plain);
            counter++;
            if (counter % 10000 == 0) {
                System.out.println("Block " + counter);
            }
        }
        outputStream.flush();
        outputStream.close();

    }

    private static byte[] getCounterBlock(byte[] nonce, long counter) {
        if (nonce.length != 8) {
            throw new IllegalArgumentException("Nonce has incorrect size");
        }
        byte[] counterBytes = Utils.longToBytes(counter);
        byte[] block = new byte[16];
        for (int i = 0; i < 8; i++) {
            block[i] = nonce[i];
            block[8 + i] = counterBytes[i];
        }
        return block;
    }
}
