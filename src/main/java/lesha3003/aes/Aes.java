package lesha3003.aes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lesha3003.aes.modes.Ctr;
import lesha3003.aes.modes.Xts;

public final class Aes {
    public static final int COLUMN_COUNT = 4;
    public static final int ROW_COUNT = 4;
    public static final int BLOCK_SIZE = 16;

    public enum KeySize {
        AES_128,
        AES_192,
        AES_256;

        public int getKeyLengthBits() {
            switch (this) {
                case AES_128:
                    return 128;
                case AES_192:
                    return 192;
                case AES_256:
                    return 256;
                default:
                    //unreachable
                    return 0;
            }
        }
    }

    public enum Mode {
        CTR,
        XTS;
    }

    public KeySize keySize;
    public Mode mode;


    public Aes(KeySize keySize, Mode mode) {
        this.keySize = keySize;
        this.mode = mode;
    }

    public void encrypt(InputStream inputStream, OutputStream outputStream, byte[] key, byte[] secondKey, byte[] tweak) throws IOException {
        switch (mode) {
            case CTR:
                Ctr.encrypt(inputStream, key, keySize, outputStream);
                break;
            case XTS:
                if (secondKey == null || tweak == null) {
                    throw new IllegalArgumentException("For XTS second key and tweak must not be null");
                }
                Xts.encrypt(inputStream, key, secondKey, tweak, keySize, outputStream);
            default:
        }
    }

    public String encrypt(String hexString, byte[] key, byte[] secondKey, byte[] tweak) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        encrypt(InputStreamProvider.fromHexString(hexString), outputStream, key, secondKey, tweak);
        return Utils.byteArrayToHexString(outputStream.toByteArray());
    }

    public byte[] encrypt(byte[] plain, byte[] key, byte[] secondKey, byte[] tweak) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        encrypt(InputStreamProvider.fromBytes(plain), outputStream, key, secondKey, tweak);
        return outputStream.toByteArray();
    }

    public void encrypt(File input, File output, byte[] key, byte[] secondKey, byte[] tweak) throws IOException {
        encrypt(new BufferedInputStream(new FileInputStream(input), 4096), new BufferedOutputStream(new FileOutputStream(output), 4096), key, secondKey, tweak);
    }

    public void decrypt(InputStream inputStream, OutputStream outputStream, byte[] key, byte[] secondKey, byte[] tweak) throws IOException {
        switch (mode) {
            case CTR:
                Ctr.decrypt(inputStream, key, keySize, outputStream);
                break;
            case XTS:
                if (secondKey == null || tweak == null) {
                    throw new IllegalArgumentException("For XTS second key and tweak must not be null");
                }
                Xts.decrypt(inputStream, key, secondKey, tweak, keySize, outputStream);
            default:
        }
    }

    public String decrypt(String hexString, byte[] key, byte[] secondKey, byte[] tweak) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        decrypt(InputStreamProvider.fromHexString(hexString), outputStream, key, secondKey, tweak);
        return Utils.byteArrayToHexString(outputStream.toByteArray());
    }

    public byte[] decrypt(byte[] cipher, byte[] key, byte[] secondKey, byte[] tweak) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        decrypt(InputStreamProvider.fromBytes(cipher), outputStream, key, secondKey, tweak);
        return outputStream.toByteArray();
    }

    public void decrypt(File input, File output, byte[] key, byte[] secondKey, byte[] tweak) throws IOException {
        decrypt(new BufferedInputStream(new FileInputStream(input), 4096), new BufferedOutputStream(new FileOutputStream(output), 4096), key, secondKey, tweak);
    }
}
