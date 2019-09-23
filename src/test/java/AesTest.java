import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import lesha3003.aes.Aes;
import lesha3003.aes.modes.Ctr;

public class AesTest {
    @Test
    public void integrity_bytes_aes128_ctr_test() throws IOException {
        Aes aes = new Aes(Aes.KeySize.AES_128, Aes.Mode.CTR);
        byte[] key = new byte[16];
        byte[] plain = new byte[1024*1024];

        Random random = new Random();
        random.nextBytes(key);
        random.nextBytes(plain);

        Date start = new Date();
        byte[] cipher = aes.encrypt(plain, key, null, null);

        Date encrypted = new Date();
        System.out.printf("encrypted in %d milliseconds\n", encrypted.getTime() - start.getTime());

        byte[] decryptedPlain = aes.decrypt(cipher, key, null, null);

        Date decrypted = new Date();
        System.out.printf("decrypted in %d milliseconds\n", decrypted.getTime() - encrypted.getTime());

        Assert.assertArrayEquals(plain, decryptedPlain);
    }
    @Test
    public void integrity_bytes_aes192_ctr_test() throws IOException {
        Aes aes = new Aes(Aes.KeySize.AES_192, Aes.Mode.CTR);
        byte[] key = new byte[24];
        byte[] plain = new byte[1024*1024];

        Random random = new Random();
        random.nextBytes(key);
        random.nextBytes(plain);

        Date start = new Date();
        byte[] cipher = aes.encrypt(plain, key, null, null);

        Date encrypted = new Date();
        System.out.printf("encrypted in %d milliseconds\n", encrypted.getTime() - start.getTime());

        byte[] decryptedPlain = aes.decrypt(cipher, key, null, null);

        Date decrypted = new Date();
        System.out.printf("decrypted in %d milliseconds\n", decrypted.getTime() - encrypted.getTime());

        Assert.assertArrayEquals(plain, decryptedPlain);
    }
    @Test
    public void integrity_bytes_aes256_ctr_test() throws IOException {
        Aes aes = new Aes(Aes.KeySize.AES_256, Aes.Mode.CTR);
        byte[] key = new byte[32];
        byte[] plain = new byte[1024*1024];

        Random random = new Random();
        random.nextBytes(key);
        random.nextBytes(plain);

        Date start = new Date();
        byte[] cipher = aes.encrypt(plain, key, null, null);

        Date encrypted = new Date();
        System.out.printf("encrypted in %d milliseconds\n", encrypted.getTime() - start.getTime());

        byte[] decryptedPlain = aes.decrypt(cipher, key, null, null);

        Date decrypted = new Date();
        System.out.printf("decrypted in %d milliseconds\n", decrypted.getTime() - encrypted.getTime());

        Assert.assertArrayEquals(plain, decryptedPlain);
    }
}
