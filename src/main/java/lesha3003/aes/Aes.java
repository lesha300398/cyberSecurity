package lesha3003.aes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import lesha3003.aes.modes.Ctr;

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

    public void encrypt(byte[] key, InputStream inputStream, OutputStream outputStream) {
        switch (mode) {
            case CTR:
                Ctr.encrypt(inputStream, key, keySize, outputStream);
                break;
            default:
                return;
        }
    }
    public String encrypt(byte[] key, String hexString) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        encrypt(key, InputStreamProvider.fromHexString(hexString), outputStream);
        return Utils.byteArrayToHexString(outputStream.toByteArray());
    }
    public byte[] encrypt(byte[] key, byte[] plain) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        encrypt(key, InputStreamProvider.fromBytes(plain), outputStream);
        return outputStream.toByteArray();
    }
    public void encrypt(byte[] key, File input, File output) throws FileNotFoundException {

    }
}
