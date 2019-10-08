import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import lesha3003.sha.Utils;
import lesha3003.sha.sha3_256.Sha3_256;
import lesha3003.sha.sha512.Sha512;

public final class TogetherTest {
    @Test
    public void test() throws IOException {
        byte[] first = new Sha3_256().hash(new ByteArrayInputStream(Utils.stringToByteArray("")));
        byte[] second = Sha512.hash(new ByteArrayInputStream(first));

        System.out.println(Utils.byteArrayToHexString(second));
        Assert.assertTrue(true);
    }
}
