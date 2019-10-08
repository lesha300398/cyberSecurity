package sha3_256;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import lesha3003.sha.Utils;
import lesha3003.sha.sha3_256.Sha3_256;

public final class Sha3_256Test {
    //https://www.di-mgt.com.au/sha_testvectors.html
    //https://csrc.nist.gov/projects/cryptographic-standards-and-guidelines/example-values
    @Test
    public void empty_test() throws IOException {
        testString("",
                "a7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a");
    }

    @Test
    public void abc_test() throws IOException {
        testString("abc",
                "3a985da74fe225b2045c172d6bd390bd855f086e3e9d525b46bfe24511431532");
    }

    @Test
    public void a_test() throws IOException {
        testString("a",
                "80084bf2fba02475726feb2cab2d8215eab14bc6bdd8bfb2c8151257032ecd8b");
    }

    @Test
    public void message448_test() throws IOException {
        testString("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq",
                "41c0dba2a9d6240849100376a8235e2c82e1b9998a999e21db32dd97496d3376");
    }

    @Test
    public void million_test() throws IOException {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 1000000; i++) {
            s.append('a');
        }
        testString(s.toString(),
                "5c8875ae474a3634ba4fd55ec85bffd661f32aca75c6d699d0cdcb6c115891c1");
    }

    @Test
    public void fox_test() throws IOException {
        testString("The quick brown fox jumps over the lazy dog",
                "69070dda01975c8c120c3aada1b282394e7f032fa9cf32f4cb2259a0897dfc04");
    }

    @Test
    public void byte_test() throws IOException {
        testHexString("e9",
                        "f0d04dd1e6cfc29a4460d521796852f25d9ef8d28b44ee91ff5b759d72c1e6d6");
    }

    private static void testString(String string, String expectedHash) throws IOException {
        byte[] hash = new Sha3_256().hash(new ByteArrayInputStream(Utils.stringToByteArrayLittleEndian(string)));
        Assert.assertEquals(expectedHash.toLowerCase(),
                Utils.byteArrayToHexStringLittleEndian(hash).toLowerCase());
    }

    private static void testHexString(String string, String expectedHash) throws IOException {
        byte[] hash = new Sha3_256().hash(new ByteArrayInputStream(Utils.hexStringToByteArrayLittleEndian(string)));
        Assert.assertEquals(expectedHash.toLowerCase(),
                Utils.byteArrayToHexStringLittleEndian(hash).toLowerCase());
    }

}
