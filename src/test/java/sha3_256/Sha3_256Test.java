package sha3_256;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import lesha3003.sha.Utils;
import lesha3003.sha.sha3_256.Sha3_256;

public final class Sha3_256Test {
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
    public void message448_test() throws IOException {
        testString("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq",
                "41c0dba2a9d6240849100376a8235e2c82e1b9998a999e21db32dd97496d3376");
    }
    @Test
    public void million_test() throws IOException {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < 1000000; i++) {
            s.append('a');
        }
        testString(s.toString(),
                "5c8875ae474a3634ba4fd55ec85bffd661f32aca75c6d699d0cdcb6c115891c1");
    }

    private static void testString(String string, String expectedHash) throws IOException {
        byte[] hash = new Sha3_256().hash(new ByteArrayInputStream(Utils.stringToByteArray(string)));
        Assert.assertEquals(expectedHash,
                Utils.byteArrayToHexStringLittleEndian(hash).toLowerCase());
    }
    private static void testHexString(String string, String expectedHash) throws IOException {
        byte[] hash = new Sha3_256().hash(new ByteArrayInputStream(Utils.hexStringToByteArray(string)));
        Assert.assertEquals(expectedHash,
                Utils.byteArrayToHexStringLittleEndian(hash).toLowerCase());
    }
    public static byte reverseBitsByte(byte x) {
        int intSize = 8;
        byte y=0;
        for(int position=intSize-1; position>0; position--){
            y+=((x&1)<<position);
            x >>= 1;
        }
        return y;
    }
}
