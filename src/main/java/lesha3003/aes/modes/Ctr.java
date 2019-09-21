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
    public static void encrypt(InputStream inputStream, byte[] key, Aes.KeySize keySize, OutputStream outputStream) {
        if (key.length != keySize.getKeyLengthBits() / 8) {
            throw new IllegalArgumentException("Key size does not correspond to selected KrySize");
        }
        byte[][][] subKeys = KeySchedule.getSubKeys(key, keySize);
        long timestamp  = (new Date()).getTime();
        int seconds = (int) timestamp/1000;
        int milliseconds = (int) timestamp%1000;
        int random = (new Random()).nextInt();
        byte[] nonce = {
                (byte) milliseconds, (byte) (milliseconds >>> 8),
                (byte) random, (byte) (random >>> 8),
                (byte) seconds, (byte) (seconds >>> 8), (byte) (seconds >>> 16), (byte) (seconds >>> 24),
        };

        Enumeration<byte[]> blocks = BlockProvider.blocksFromInputStream(inputStream, null);
        try {

            outputStream.write(nonce);
            long counter = 0;
            while (blocks.hasMoreElements()) {
                byte[] counterBlock = getCounterBlock(nonce, counter);
                byte[] outputBlock = Utils.matrixToBytes(SingleBlockCipher.encrypt(keySize, Utils.bytesToMatrix(counterBlock), subKeys));

                byte[] cipher = Utils.xorBytes(blocks.nextElement(), outputBlock);
                outputStream.write(cipher);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            System.out.print(ex);
        }
    }

    public static void decrypt(InputStream inputStream, byte[] key, Aes.KeySize keySize, OutputStream outputStream) {
        if (key.length != keySize.getKeyLengthBits() / 8) {
            throw new IllegalArgumentException("Key size does not correspond to selected KrySize");
        }
        byte[][][] subKeys = KeySchedule.getSubKeys(key, keySize);
        byte[] nonce = new byte[8];
        try {
            inputStream.read(nonce);
            Enumeration<byte[]> blocks = BlockProvider.blocksFromInputStream(inputStream, null);

            long counter = 0;
            while (blocks.hasMoreElements()) {
                byte[] counterBlock = getCounterBlock(nonce, counter);
                byte[] outputBlock = Utils.matrixToBytes(SingleBlockCipher.encrypt(keySize, Utils.bytesToMatrix(counterBlock), subKeys));

                byte[] plain = Utils.xorBytes(blocks.nextElement(), outputBlock);
                outputStream.write(plain);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException ex) {
            System.out.print(ex);
        }
    }
    private static byte[] getCounterBlock(byte[] nonce, long counter) {
        if (nonce.length != 8) {
            throw new IllegalArgumentException("Nonce has incorrect size");
        }
        byte[] counterBytes = Utils.longToBytes(counter);
        byte[] block = new byte[16];
        for (int i = 0; i < 8; i++) {
            block[i] = nonce[i];
            block[8+i] = counterBytes[i];
        }
        return block;
    }
}
