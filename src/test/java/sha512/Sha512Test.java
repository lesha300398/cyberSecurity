package sha512;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import lesha3003.sha.Utils;
import lesha3003.sha.sha512.Sha512;

public final class Sha512Test {
    // https://www.di-mgt.com.au/sha_testvectors.html
    @Test
    public void empty_test() throws IOException {
        testString("",
                "cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e");
    }
    @Test
    public void abc_test() throws IOException {
        testString("abc",
                "ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f");

    }
    @Test
    public void message448_test() throws IOException {
        testString("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq",
                "204a8fc6dda82f0a0ced7beb8e08a41657c16ef468b228a8279be331a703c33596fd15c13b1b07f9aa1d3bea57789ca031ad85c7a71dd70354ec631238ca3445");
    }
    @Test
    public void million_test() throws IOException {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < 1000000; i++) {
            s.append('a');
        }
        testString(s.toString(),
                "e718483d0ce769644e2e42c7bc15b4638e1f98b13b2044285632a803afa973ebde0ff244877ea60a4cb0432ce577c31beb009c5c2c49aa2e4eadb217ad8cc09b");
    }

    private static void testString(String string, String expectedHash) throws IOException {
        byte[] hash = Sha512.hash(new ByteArrayInputStream(Utils.stringToByteArray(string)));
        Assert.assertEquals(expectedHash,
                Utils.byteArrayToHexString(hash).toLowerCase());
    }
    private static void testHexString(String string, String expectedHash) throws IOException {
        byte[] hash = Sha512.hash(new ByteArrayInputStream(Utils.hexStringToByteArray(string)));
        Assert.assertEquals(expectedHash,
                Utils.byteArrayToHexString(hash).toLowerCase());
    }
}
