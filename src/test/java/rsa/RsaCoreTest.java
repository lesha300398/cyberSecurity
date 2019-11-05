package rsa;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import lesha3003.digitalsignature.biguint.BigUInt;
import lesha3003.digitalsignature.rsa.RsaCore;
import lesha3003.digitalsignature.rsa.RsaCoreBigInteger;

public final class RsaCoreTest {
    @Test
    public void encrypt_test() {
        RsaCore rsa = new RsaCore(11, 23, 3);
        BigUInt expectedCipher = new BigUInt(250);

        byte[] cipher = rsa.encrypt(new BigUInt(57).toByteArray());

        Assert.assertTrue(new BigUInt(cipher).equals(expectedCipher));
    }
    @Test
    public void decrypt_test() {
        RsaCore rsa = new RsaCore(11, 23, 3);
        BigUInt expectedPlain = new BigUInt(57);

        byte[] plain = rsa.decrypt(new BigUInt(250).toByteArray());

        Assert.assertTrue(new BigUInt(plain).equals(expectedPlain));
    }

    @Test
    public void integrity_test() {
        RsaCore rsa = new RsaCore(3000);
        System.out.println("RSA ready");
        byte[] expectedPlain = new byte[100];
        Random random = new SecureRandom();
        random.nextBytes(expectedPlain);
        expectedPlain[0] &= 0b0111;
        System.out.println("Plain ready");


        byte[] cipher = rsa.encrypt(expectedPlain);
        System.out.println("Cipher ready");
        byte[] decrypted = rsa.decrypt(cipher);

        Assert.assertArrayEquals(expectedPlain, decrypted);
    }
    @Test
    public void integrityBigInteger_test() {
        RsaCoreBigInteger rsa = new RsaCoreBigInteger(3000);
        System.out.println("RSA ready");
        Random random = new SecureRandom();
        BigInteger expectedPlain = new BigInteger(1000, random);
        System.out.println("Plain ready");


        BigInteger cipher = rsa.encrypt(expectedPlain);
        System.out.println("Cipher ready");
        BigInteger decrypted = rsa.decrypt(cipher);

        Assert.assertArrayEquals(expectedPlain.toByteArray(), decrypted.toByteArray());
    }

}
