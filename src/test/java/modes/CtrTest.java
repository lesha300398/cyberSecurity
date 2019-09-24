package modes;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import lesha3003.aes.Aes;
import lesha3003.aes.modes.Ctr;

public class CtrTest {
    @Test
    public void integrity_aes128_test() throws IOException {
        byte[] key = new byte[16];
        byte[] plain = new byte[1024*1024];

        Random random = new Random();
        random.nextBytes(key);
        random.nextBytes(plain);

        ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream(1024*1024 + 8);
        ByteArrayOutputStream plainOutputStream = new ByteArrayOutputStream(1024*1024);
        Date start = new Date();
        Ctr.encrypt(new ByteArrayInputStream(plain),key, Aes.KeySize.AES_128, cipherOutputStream);
        Date encrypted = new Date();
        System.out.printf("encrypted in %d milliseconds\n", encrypted.getTime() - start.getTime());
        Ctr.decrypt(new ByteArrayInputStream(cipherOutputStream.toByteArray()), key, Aes.KeySize.AES_128, plainOutputStream);
        Date decrypted = new Date();
        System.out.printf("decrypted in %d milliseconds\n", decrypted.getTime() - encrypted.getTime());

        Assert.assertArrayEquals(plain, plainOutputStream.toByteArray());
    }
    @Test
    public void integrity_aes192_test() throws IOException {
        byte[] key = new byte[24];
        byte[] plain = new byte[1024*1024];

        Random random = new Random();
        random.nextBytes(key);
        random.nextBytes(plain);

        ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream(1024*1024 + 8);
        ByteArrayOutputStream plainOutputStream = new ByteArrayOutputStream(1024*1024);
        Date start = new Date();
        Ctr.encrypt(new ByteArrayInputStream(plain),key, Aes.KeySize.AES_192, cipherOutputStream);
        Date encrypted = new Date();
        System.out.printf("encrypted in %d milliseconds\n", encrypted.getTime() - start.getTime());
        Ctr.decrypt(new ByteArrayInputStream(cipherOutputStream.toByteArray()), key, Aes.KeySize.AES_192, plainOutputStream);
        Date decrypted = new Date();
        System.out.printf("decrypted in %d milliseconds\n", decrypted.getTime() - encrypted.getTime());

        Assert.assertArrayEquals(plain, plainOutputStream.toByteArray());
    }
    @Test
    public void integrity_aes256_test() throws IOException {
        byte[] key = new byte[32];
        byte[] plain = new byte[1024*1024];

        Random random = new Random();
        random.nextBytes(key);
        random.nextBytes(plain);

        ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream(1024*1024 + 8);
        ByteArrayOutputStream plainOutputStream = new ByteArrayOutputStream(1024*1024);
        Date start = new Date();
        Ctr.encrypt(new ByteArrayInputStream(plain),key, Aes.KeySize.AES_256, cipherOutputStream);
        Date encrypted = new Date();
        System.out.printf("encrypted in %d milliseconds\n", encrypted.getTime() - start.getTime());
        Ctr.decrypt(new ByteArrayInputStream(cipherOutputStream.toByteArray()), key, Aes.KeySize.AES_256, plainOutputStream);
        Date decrypted = new Date();
        System.out.printf("decrypted in %d milliseconds\n", decrypted.getTime() - encrypted.getTime());

        Assert.assertArrayEquals(plain, plainOutputStream.toByteArray());
    }
}
