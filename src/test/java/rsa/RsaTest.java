package rsa;

import org.junit.Assert;
import org.junit.Test;

import lesha3003.digitalsignature.rsa.Rsa;

public final class RsaTest {
    @Test
    public void pad_test() {
        byte[] message = new byte[] { 0x5c, 0x5c, 0x5c};
        Rsa rsa = new Rsa(8);

        byte[] padded = rsa.pad(message);

        Assert.assertArrayEquals(new byte[]{0x00,0x01,(byte)0xFF, (byte)0xFF, 0x00, 0x5c, 0x5c, 0x5c}, padded);
    }
    @Test
    public void unpad_test() throws Rsa.UnpaddingException {
        byte[] padded = new byte[] {0x00, 0x01, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x00, 0x5c, 0x5c, 0x5c};
        Rsa rsa = new Rsa(10);

        byte[] message = rsa.unpad(padded);

        Assert.assertArrayEquals(new byte[]{0x5c, 0x5c, 0x5c}, message);
    }

    @Test
    public void integrity_test() throws Rsa.UnpaddingException {
        byte[] plain = new byte[100];
        Rsa rsa = new Rsa(200);

        byte[] cipher = rsa.encrypt(plain);
        byte[] decrypted = rsa.decryptAndUnpad(cipher);

        Assert.assertArrayEquals(plain, decrypted);
    }
}
