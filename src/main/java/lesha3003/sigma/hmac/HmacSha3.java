package lesha3003.sigma.hmac;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Arrays;

import lesha3003.sha.sha3_256.Sha3_256;
import lesha3003.sigma.Utils;

public final class HmacSha3 {
    private static final int blockSize = 136;
    public static final int outputSize = 256/8;

    private static final byte opad = (byte)0x5c;
    private static final byte ipad = (byte)0x36;



    public static byte[] hmac(InputStream message, byte[] key) throws IOException {
        Sha3_256 sha = new Sha3_256();
        if (key.length > blockSize) {
            key = sha.hash(key);
        }
        if (key.length < blockSize) {
            key = Arrays.copyOf(key, blockSize);
        }
        byte[] oKey = new byte[blockSize];
        byte[] iKey = new byte[blockSize];
        for (int i = 0; i < blockSize; i++) {
            oKey[i] = (byte) (key[i] ^ opad);
            iKey[i] = (byte) (key[i] ^ ipad);
        }

        return sha.hash(Utils.concat(oKey, sha.hash(new SequenceInputStream(new ByteArrayInputStream(iKey), message))));
    }
    public static byte[] hmac(byte[] message, byte[] key) throws IOException {
        return hmac(new ByteArrayInputStream(message), key);
    }

}
