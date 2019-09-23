package modes;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

import lesha3003.aes.Aes;
import lesha3003.aes.InputStreamProvider;
import lesha3003.aes.Utils;
import lesha3003.aes.modes.Ctr;
import lesha3003.aes.modes.Xts;
import lesha3003.aes.singleblockcipher.SingleBlockCipher;

public class XtsTest {
    @Test
    public void integrity_aes128_completeLast_test() throws IOException {
        byte[] key1 = new byte[16];
        byte[] key2 = new byte[16];
        byte[] tweak = new byte[16];
        byte[] plain = new byte[1024 * 1024];

        Random random = new Random();
        random.nextBytes(key1);
        random.nextBytes(key2);
        random.nextBytes(tweak);
        random.nextBytes(plain);

        ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream(1024 * 1024);
        ByteArrayOutputStream plainOutputStream = new ByteArrayOutputStream(1024 * 1024);
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
        byte[] plain = new byte[1024 * 1024 + 10];

        Random random = new Random();
        random.nextBytes(key1);
        random.nextBytes(key2);
        random.nextBytes(tweak);
        random.nextBytes(plain);

        ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream(1024 * 1024 + 10);
        ByteArrayOutputStream plainOutputStream = new ByteArrayOutputStream(1024 * 1024 + 10);
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
        byte[] plain = new byte[1024 * 1024];

        Random random = new Random();
        random.nextBytes(key1);
        random.nextBytes(key2);
        random.nextBytes(tweak);
        random.nextBytes(plain);

        ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream(1024 * 1024);
        ByteArrayOutputStream plainOutputStream = new ByteArrayOutputStream(1024 * 1024);
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
        byte[] plain = new byte[1024 * 1024 + 10];

        Random random = new Random();
        random.nextBytes(key1);
        random.nextBytes(key2);
        random.nextBytes(tweak);
        random.nextBytes(plain);

        ByteArrayOutputStream cipherOutputStream = new ByteArrayOutputStream(1024 * 1024 + 10);
        ByteArrayOutputStream plainOutputStream = new ByteArrayOutputStream(1024 * 1024 + 10);
        Date start = new Date();
        Xts.encrypt(new ByteArrayInputStream(plain), key1, key2, tweak, Aes.KeySize.AES_256, cipherOutputStream);
        Date encrypted = new Date();
        System.out.printf("encrypted in %d milliseconds\n", encrypted.getTime() - start.getTime());
        Xts.decrypt(new ByteArrayInputStream(cipherOutputStream.toByteArray()), key1, key2, tweak, Aes.KeySize.AES_256, plainOutputStream);
        Date decrypted = new Date();
        System.out.printf("decrypted in %d milliseconds\n", decrypted.getTime() - encrypted.getTime());

        Assert.assertArrayEquals(plain, plainOutputStream.toByteArray());
    }

    // http://libeccio.di.unisa.it/Crypto14/Lab/p1619.pdf
    @Test
    public void completeLastBlock_aes128_test1() throws IOException {
        testXtsEncryption(Aes.KeySize.AES_128,
                "27182818284590452353602874713526",
                "31415926535897932384626433832795",
                "01000000000000000000000000000000",

                "27a7479befa1d476489f308cd4cfa6e2a96e4bbe3208ff25287dd3819616e89c" +
                        "c78cf7f5e543445f8333d8fa7f56000005279fa5d8b5e4ad40e736ddb4d35412" +
                        "328063fd2aab53e5ea1e0a9f332500a5df9487d07a5c92cc512c8866c7e860ce" +
                        "93fdf166a24912b422976146ae20ce846bb7dc9ba94a767aaef20c0d61ad0265" +
                        "5ea92dc4c4e41a8952c651d33174be51a10c421110e6d81588ede82103a252d8" +
                        "a750e8768defffed9122810aaeb99f9172af82b604dc4b8e51bcb08235a6f434" +
                        "1332e4ca60482a4ba1a03b3e65008fc5da76b70bf1690db4eae29c5f1badd03c" +
                        "5ccf2a55d705ddcd86d449511ceb7ec30bf12b1fa35b913f9f747a8afd1b130e" +
                        "94bff94effd01a91735ca1726acd0b197c4e5b03393697e126826fb6bbde8ecc" +
                        "1e08298516e2c9ed03ff3c1b7860f6de76d4cecd94c8119855ef5297ca67e9f3" +
                        "e7ff72b1e99785ca0a7e7720c5b36dc6d72cac9574c8cbbc2f801e23e56fd344" +
                        "b07f22154beba0f08ce8891e643ed995c94d9a69c9f1b5f499027a78572aeebd" +
                        "74d20cc39881c213ee770b1010e4bea718846977ae119f7a023ab58cca0ad752" +
                        "afe656bb3c17256a9f6e9bf19fdd5a38fc82bbe872c5539edb609ef4f79c203e" +
                        "bb140f2e583cb2ad15b4aa5b655016a8449277dbd477ef2c8d6c017db738b18d" +
                        "eb4a427d1923ce3ff262735779a418f20a282df920147beabe421ee5319d0568",

                "264d3ca8512194fec312c8c9891f279fefdd608d0c027b60483a3fa811d65ee5" +
                        "9d52d9e40ec5672d81532b38b6b089ce951f0f9c35590b8b978d175213f329bb" +
                        "1c2fd30f2f7f30492a61a532a79f51d36f5e31a7c9a12c286082ff7d2394d18f" +
                        "783e1a8e72c722caaaa52d8f065657d2631fd25bfd8e5baad6e527d763517501" +
                        "c68c5edc3cdd55435c532d7125c8614deed9adaa3acade5888b87bef641c4c99" +
                        "4c8091b5bcd387f3963fb5bc37aa922fbfe3df4e5b915e6eb514717bdd2a7407" +
                        "9a5073f5c4bfd46adf7d282e7a393a52579d11a028da4d9cd9c77124f9648ee3" +
                        "83b1ac763930e7162a8d37f350b2f74b8472cf09902063c6b32e8c2d9290cefb" +
                        "d7346d1c779a0df50edcde4531da07b099c638e83a755944df2aef1aa31752fd" +
                        "323dcb710fb4bfbb9d22b925bc3577e1b8949e729a90bbafeacf7f7879e7b114" +
                        "7e28ba0bae940db795a61b15ecf4df8db07b824bb062802cc98a9545bb2aaeed" +
                        "77cb3fc6db15dcd7d80d7d5bc406c4970a3478ada8899b329198eb61c193fb62" +
                        "75aa8ca340344a75a862aebe92eee1ce032fd950b47d7704a3876923b4ad6284" +
                        "4bf4a09c4dbe8b4397184b7471360c9564880aedddb9baa4af2e75394b08cd32" +
                        "ff479c57a07d3eab5d54de5f9738b8d27f27a9f0ab11799d7b7ffefb2704c95c" +
                        "6ad12c39f1e867a4b7b1d7818a4b753dfd2a89ccb45e001a03a867b187f225dd"

        );
    }

    @Test
    public void completeLastBlock_aes256_test1() throws IOException {
        testXtsEncryption(Aes.KeySize.AES_256,
                "2718281828459045235360287471352662497757247093699959574966967627",
                "3141592653589793238462643383279502884197169399375105820974944592",
                "ff000000000000000000000000000000",
                "000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f" +
                        "202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f" +
                        "404142434445464748494a4b4c4d4e4f505152535455565758595a5b5c5d5e5f" +
                        "606162636465666768696a6b6c6d6e6f707172737475767778797a7b7c7d7e7f" +
                        "808182838485868788898a8b8c8d8e8f909192939495969798999a9b9c9d9e9f" +
                        "a0a1a2a3a4a5a6a7a8a9aaabacadaeafb0b1b2b3b4b5b6b7b8b9babbbcbdbebf" +
                        "c0c1c2c3c4c5c6c7c8c9cacbcccdcecfd0d1d2d3d4d5d6d7d8d9dadbdcdddedf" +
                        "e0e1e2e3e4e5e6e7e8e9eaebecedeeeff0f1f2f3f4f5f6f7f8f9fafbfcfdfeff" +
                        "000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f" +
                        "202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f" +
                        "404142434445464748494a4b4c4d4e4f505152535455565758595a5b5c5d5e5f" +
                        "606162636465666768696a6b6c6d6e6f707172737475767778797a7b7c7d7e7f" +
                        "808182838485868788898a8b8c8d8e8f909192939495969798999a9b9c9d9e9f" +
                        "a0a1a2a3a4a5a6a7a8a9aaabacadaeafb0b1b2b3b4b5b6b7b8b9babbbcbdbebf" +
                        "c0c1c2c3c4c5c6c7c8c9cacbcccdcecfd0d1d2d3d4d5d6d7d8d9dadbdcdddedf" +
                        "e0e1e2e3e4e5e6e7e8e9eaebecedeeeff0f1f2f3f4f5f6f7f8f9fafbfcfdfeff",


                "1c3b3a102f770386e4836c99e370cf9bea00803f5e482357a4ae12d414a3e63b" +
                        "5d31e276f8fe4a8d66b317f9ac683f44680a86ac35adfc3345befecb4bb188fd" +
                        "5776926c49a3095eb108fd1098baec70aaa66999a72a82f27d848b21d4a741b0" +
                        "c5cd4d5fff9dac89aeba122961d03a757123e9870f8acf1000020887891429ca" +
                        "2a3e7a7d7df7b10355165c8b9a6d0a7de8b062c4500dc4cd120c0f7418dae3d0" +
                        "b5781c34803fa75421c790dfe1de1834f280d7667b327f6c8cd7557e12ac3a0f" +
                        "93ec05c52e0493ef31a12d3d9260f79a289d6a379bc70c50841473d1a8cc81ec" +
                        "583e9645e07b8d9670655ba5bbcfecc6dc3966380ad8fecb17b6ba02469a020a" +
                        "84e18e8f84252070c13e9f1f289be54fbc481457778f616015e1327a02b140f1" +
                        "505eb309326d68378f8374595c849d84f4c333ec4423885143cb47bd71c5edae" +
                        "9be69a2ffeceb1bec9de244fbe15992b11b77c040f12bd8f6a975a44a0f90c29" +
                        "a9abc3d4d893927284c58754cce294529f8614dcd2aba991925fedc4ae74ffac" +
                        "6e333b93eb4aff0479da9a410e4450e0dd7ae4c6e2910900575da401fc07059f" +
                        "645e8b7e9bfdef33943054ff84011493c27b3429eaedb4ed5376441a77ed4385" +
                        "1ad77f16f541dfd269d50d6a5f14fb0aab1cbb4c1550be97f7ab4066193c4caa" +
                        "773dad38014bd2092fa755c824bb5e54c4f36ffda9fcea70b9c6e693e148c151"

        );
    }

    @Test
    public void incompleteLastBlock_aes128_test1() throws IOException {
        testXtsEncryption(Aes.KeySize.AES_128,
                "fffefdfcfbfaf9f8f7f6f5f4f3f2f1f0",
                "bfbebdbcbbbab9b8b7b6b5b4b3b2b1b0",
                "9a785634120000000000000000000000",
                "000102030405060708090a0b0c0d0e0f10",
                "6c1625db4671522d3d7599601de7ca09ed");
    }
    private void testXtsEncryption(Aes.KeySize keySize, String key1Hex, String key2Hex, String tweakHex, String plainHex, String expectedCipherHex) throws IOException {
        byte[] key1 = Utils.hexStringToByteArray(key1Hex);
        byte[] key2 = Utils.hexStringToByteArray(key2Hex);
        byte[] tweak = Utils.hexStringToByteArray(tweakHex);

        InputStream plainStream = InputStreamProvider.fromHexString(plainHex);

        byte[] expectedCipher = Utils.hexStringToByteArray(expectedCipherHex);

        ByteArrayOutputStream cipherStream = new ByteArrayOutputStream();
        Xts.encrypt(plainStream, key1, key2, tweak, keySize, cipherStream);

//        System.out.print(Utils.byteArrayToHexString(cipherStream.toByteArray()));
        Assert.assertArrayEquals(expectedCipher, cipherStream.toByteArray());
    }
}
