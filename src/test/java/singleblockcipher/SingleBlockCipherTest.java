package singleblockcipher;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import lesha3003.aes.Aes;
import lesha3003.aes.Utils;
import lesha3003.aes.keyschedule.KeySchedule;
import lesha3003.aes.singleblockcipher.SingleBlockCipher;

public class SingleBlockCipherTest {
    @Test
    public void integrity_aes128_test() {
        Random random = new Random();

        final byte[][] plain = new byte[4][4];
        final byte[] key = new byte[16];
        for (int i = 0; i < 4; i++) {
            random.nextBytes(plain[i]);
            random.nextBytes(key);
        }

        byte[][][] subKeys = KeySchedule.getSubKeys(key, Aes.KeySize.AES_128);

        byte[][] cipher = SingleBlockCipher.encrypt(Aes.KeySize.AES_128, plain, subKeys);
        byte[][] decipher = SingleBlockCipher.decrypt(Aes.KeySize.AES_128, cipher, subKeys);

        Assert.assertArrayEquals(plain, decipher);

    }
    @Test
    public void integrity_aes192_test() {
        Random random = new Random();

        final byte[][] plain = new byte[4][4];
        final byte[] key = new byte[24];
        for (int i = 0; i < 4; i++) {
            random.nextBytes(plain[i]);
            random.nextBytes(key);
        }

        byte[][][] subKeys = KeySchedule.getSubKeys(key, Aes.KeySize.AES_192);

        byte[][] cipher = SingleBlockCipher.encrypt(Aes.KeySize.AES_192, plain, subKeys);
        byte[][] decipher = SingleBlockCipher.decrypt(Aes.KeySize.AES_192, cipher, subKeys);

        Assert.assertArrayEquals(plain, decipher);

    }
    @Test
    public void integrity_aes256_test() {
        Random random = new Random();

        final byte[][] plain = new byte[4][4];
        final byte[] key = new byte[32];
        for (int i = 0; i < 4; i++) {
            random.nextBytes(plain[i]);
            random.nextBytes(key);
        }

        byte[][][] subKeys = KeySchedule.getSubKeys(key, Aes.KeySize.AES_256);

        byte[][] cipher = SingleBlockCipher.encrypt(Aes.KeySize.AES_256, plain, subKeys);
        byte[][] decipher = SingleBlockCipher.decrypt(Aes.KeySize.AES_256, cipher, subKeys);

        Assert.assertArrayEquals(plain, decipher);

    }
    // test vectors https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Algorithm-Validation-Program/documents/aes/AESAVS.pdf
    @Test
    public void encrypt_aes128_keyZeros_test1() {
        testEncrypt(Aes.KeySize.AES_128,
                "f34481ec3cc627bacd5dc3fb08f273e6",
                "00000000000000000000000000000000",
                "0336763e966d92595a567cc9ce537f5e");
    }
    @Test
    public void encrypt_aes128_keyZeros_test2() {
        testEncrypt(Aes.KeySize.AES_128,
                "b26aeb1874e47ca8358ff22378f09144",
                "00000000000000000000000000000000",
                "459264f4798f6a78bacb89c15ed3d601");
    }
    @Test
    public void encrypt_aes192_keyZeros_test1() {
        testEncrypt(Aes.KeySize.AES_192,
                "1b077a6af4b7f98229de786d7516b639",
                "000000000000000000000000000000000000000000000000",
                "275cfc0413d8ccb70513c3859b1d0f72");
    }
    @Test
    public void encrypt_aes192_keyZeros_test2() {
        testEncrypt(Aes.KeySize.AES_192,
                "51719783d3185a535bd75adc65071ce1",
                "000000000000000000000000000000000000000000000000",
                "4f354592ff7c8847d2d0870ca9481b7c");
    }
    @Test
    public void encrypt_aes256_keyZeros_test1() {
        testEncrypt(Aes.KeySize.AES_256,
                "014730f80ac625fe84f026c60bfd547d",
                "0000000000000000000000000000000000000000000000000000000000000000",
                "5c9d844ed46f9885085e5d6a4f94c7d7");
    }
    @Test
    public void encrypt_aes256_keyZeros_test2() {
        testEncrypt(Aes.KeySize.AES_256,
                "761c1fe41a18acf20d241650611d90f1",
                "0000000000000000000000000000000000000000000000000000000000000000",
                "623a52fcea5d443e48d9181ab32c7421");
    }
    @Test
    public void encrypt_aes128_plainZeros_test1() {
        testEncrypt(Aes.KeySize.AES_128,
                "00000000000000000000000000000000",
                "10a58869d74be5a374cf867cfb473859",
                "6d251e6944b051e04eaa6fb4dbf78465");
    }
    @Test
    public void encrypt_aes128_plainZeros_test2() {
        testEncrypt(Aes.KeySize.AES_128,
                "00000000000000000000000000000000",
                "b6364ac4e1de1e285eaf144a2415f7a0",
                "5d9b05578fc944b3cf1ccf0e746cd581");
    }
    @Test
    public void encrypt_aes192_plainZeros_test1() {
        testEncrypt(Aes.KeySize.AES_192,
                "00000000000000000000000000000000",
                "e9f065d7c13573587f7875357dfbb16c53489f6a4bd0f7cd",
                "0956259c9cd5cfd0181cca53380cde06");
    }
    @Test
    public void encrypt_aes192_plainZeros_test2() {
        testEncrypt(Aes.KeySize.AES_192,
                "00000000000000000000000000000000",
                "a8a282ee31c03fae4f8e9b8930d5473c2ed695a347e88b7c",
                "93f3270cfc877ef17e106ce938979cb0");
    }
    @Test
    public void encrypt_aes256_plainZeros_test1() {
        testEncrypt(Aes.KeySize.AES_256,
                "00000000000000000000000000000000",
                "c47b0294dbbbee0fec4757f22ffeee3587ca4730c3d33b691df38bab076bc558",
                "46f2fb342d6f0ab477476fc501242c5f");
    }
    @Test
    public void encrypt_aes256_plainZeros_test2() {
        testEncrypt(Aes.KeySize.AES_256,
                "00000000000000000000000000000000",
                "c1cc358b449909a19436cfbb3f852ef8bcb5ed12ac7058325f56e6099aab1a1c",
                "352065272169abf9856843927d0674fd");
    }
    @Test
    public void decrypt_aes128_keyZeros_test() {
        testDecrypt(Aes.KeySize.AES_128,
                "92beedab1895a94faa69b632e5cc47ce",
                "00000000000000000000000000000000",
                "cb9fceec81286ca3e989bd979b0cb284");
    }
    @Test
    public void decrypt_aes192_keyZeros_test() {
        testDecrypt(Aes.KeySize.AES_192,
                "067cd9d3749207791841562507fa9626",
                "000000000000000000000000000000000000000000000000",
                "941a4773058224e1ef66d10e0a6ee782");
    }
    @Test
    public void decrypt_aes256_keyZeros_test() {
        testDecrypt(Aes.KeySize.AES_256,
                "38f2c7ae10612415d27ca190d27da8b4",
                "0000000000000000000000000000000000000000000000000000000000000000",
                "8a560769d605868ad80d819bdba03771");
    }
    @Test
    public void decrypt_aes128_plainZeros_test() {
        testDecrypt(Aes.KeySize.AES_128,
                "a303d940ded8f0baff6f75414cac5243",
                "b69418a85332240dc82492353956ae0c",
                "00000000000000000000000000000000");
    }
    @Test
    public void decrypt_aes192_plainZeros_test() {
        testDecrypt(Aes.KeySize.AES_192,
                "9b9fdd1c5975655f539998b306a324af",
                "ee053aa011c8b428cdcc3636313c54d6a03cac01c71579d6",
                "00000000000000000000000000000000");
    }
    @Test
    public void decrypt_aes256_plainZeros_test() {
        testDecrypt(Aes.KeySize.AES_256,
                "798c7c005dee432b2c8ea5dfa381ecc3",
                "90143ae20cd78c5d8ebdd6cb9dc1762427a96c78c639bccc41a61424564eafe1",
                "00000000000000000000000000000000");
    }
    private void testEncrypt(Aes.KeySize keySize, String plainHexString, String keyHexString, String expectedCipherHexString) {
        byte[] key = Utils.hexStringToByteArray(keyHexString);
        byte[][] plain = Utils.bytesToMatrix(Utils.hexStringToByteArray(plainHexString));
        byte[][] expectedCipher = Utils.bytesToMatrix(Utils.hexStringToByteArray(expectedCipherHexString));

        byte[][][] subKeys = KeySchedule.getSubKeys(key, keySize);
        byte[][] cipher = SingleBlockCipher.encrypt(keySize, plain, subKeys);

        Assert.assertArrayEquals(expectedCipher, cipher);
    }
    private void testDecrypt(Aes.KeySize keySize, String cipherHexString, String keyHexString, String expectedPlainHexString) {
        byte[] key = Utils.hexStringToByteArray(keyHexString);
        byte[][] cipher = Utils.bytesToMatrix(Utils.hexStringToByteArray(cipherHexString));
        byte[][] expectedPlain = Utils.bytesToMatrix(Utils.hexStringToByteArray(expectedPlainHexString));

        byte[][][] subKeys = KeySchedule.getSubKeys(key, keySize);
        byte[][] plain = SingleBlockCipher.decrypt(keySize, cipher, subKeys);

        Assert.assertArrayEquals(expectedPlain, plain);
    }
}
