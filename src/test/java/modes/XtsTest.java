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
import lesha3003.aes.modes.Xts;

public class XtsTest {
    @Test
    public void integrity_aes128_completeLast_test() throws IOException {
        byte[] key1 = new byte[16];
        byte[] key2 = new byte[16];
        byte[] tweak = new byte[16];
        byte[] plain = new byte[1024*1024];

        Random random = new Random();
        random.nextBytes(key1);
        random.nextBytes(key2);
        random.nextBytes(tweak);
        random.nextBytes(plain);

        ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream(1024*1024);
        ByteArrayOutputStream plainOutputStream = new ByteArrayOutputStream(1024*1024);
        Date start = new Date();
        Xts.encrypt(new ByteArrayInputStream(plain), key1, key2, tweak, Aes.KeySize.AES_128, cipherOutputStream);
        Date encrypted = new Date();
        System.out.printf("encrypted in %d milliseconds\n", encrypted.getTime() - start.getTime());
        Xts.decrypt(new ByteArrayInputStream(cipherOutputStream.toByteArray()), key1, key2, tweak, Aes.KeySize.AES_128, plainOutputStream);
        Date decrypted = new Date();
        System.out.printf("decrypted in %d milliseconds\n", decrypted.getTime() - encrypted.getTime());

        Assert.assertArrayEquals(plain, plainOutputStream.toByteArray());
    }
    @Test
    public void integrity_aes128_incompleteLast_test() throws IOException {
        byte[] key1 = new byte[16];
        byte[] key2 = new byte[16];
        byte[] tweak = new byte[16];
        byte[] plain = new byte[1024*1024 + 10];

        Random random = new Random();
        random.nextBytes(key1);
        random.nextBytes(key2);
        random.nextBytes(tweak);
        random.nextBytes(plain);

        ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream(1024*1024 + 10);
        ByteArrayOutputStream plainOutputStream = new ByteArrayOutputStream(1024*1024 + 10);
        Date start = new Date();
        Xts.encrypt(new ByteArrayInputStream(plain), key1, key2, tweak, Aes.KeySize.AES_128, cipherOutputStream);
        Date encrypted = new Date();
        System.out.printf("encrypted in %d milliseconds\n", encrypted.getTime() - start.getTime());
        Xts.decrypt(new ByteArrayInputStream(cipherOutputStream.toByteArray()), key1, key2, tweak, Aes.KeySize.AES_128, plainOutputStream);
        Date decrypted = new Date();
        System.out.printf("decrypted in %d milliseconds\n", decrypted.getTime() - encrypted.getTime());

        Assert.assertArrayEquals(plain, plainOutputStream.toByteArray());
    }
    @Test
    public void integrity_aes256_completeLast_test() throws IOException {
        byte[] key1 = new byte[32];
        byte[] key2 = new byte[32];
        byte[] tweak = new byte[16];
        byte[] plain = new byte[1024*1024];

        Random random = new Random();
        random.nextBytes(key1);
        random.nextBytes(key2);
        random.nextBytes(tweak);
        random.nextBytes(plain);

        ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream(1024*1024);
        ByteArrayOutputStream plainOutputStream = new ByteArrayOutputStream(1024*1024);
        Date start = new Date();
        Xts.encrypt(new ByteArrayInputStream(plain), key1, key2, tweak, Aes.KeySize.AES_256, cipherOutputStream);
        Date encrypted = new Date();
        System.out.printf("encrypted in %d milliseconds\n", encrypted.getTime() - start.getTime());
        Xts.decrypt(new ByteArrayInputStream(cipherOutputStream.toByteArray()), key1, key2, tweak, Aes.KeySize.AES_256, plainOutputStream);
        Date decrypted = new Date();
        System.out.printf("decrypted in %d milliseconds\n", decrypted.getTime() - encrypted.getTime());

        Assert.assertArrayEquals(plain, plainOutputStream.toByteArray());
    }
    @Test
    public void integrity_aes256_incompleteLast_test() throws IOException {
        byte[] key1 = new byte[32];
        byte[] key2 = new byte[32];
        byte[] tweak = new byte[16];
        byte[] plain = new byte[1024*1024 + 10];

        Random random = new Random();
        random.nextBytes(key1);
        random.nextBytes(key2);
        random.nextBytes(tweak);
        random.nextBytes(plain);

        ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream(1024*1024 + 10);
        ByteArrayOutputStream plainOutputStream = new ByteArrayOutputStream(1024*1024 + 10);
        Date start = new Date();
        Xts.encrypt(new ByteArrayInputStream(plain), key1, key2, tweak, Aes.KeySize.AES_256, cipherOutputStream);
        Date encrypted = new Date();
        System.out.printf("encrypted in %d milliseconds\n", encrypted.getTime() - start.getTime());
        Xts.decrypt(new ByteArrayInputStream(cipherOutputStream.toByteArray()), key1, key2, tweak, Aes.KeySize.AES_256, plainOutputStream);
        Date decrypted = new Date();
        System.out.printf("decrypted in %d milliseconds\n", decrypted.getTime() - encrypted.getTime());

        Assert.assertArrayEquals(plain, plainOutputStream.toByteArray());
    }
}
