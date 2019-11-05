import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import lesha3003.digitalsignature.DigitalSignature;
import lesha3003.digitalsignature.biguint.BigUInt;
import lesha3003.digitalsignature.rsa.RsaCore;

public class DigitalSignatureTest {
    @Test
    public void generateAndVerify_test() throws IOException {
        DigitalSignature digitalSignature = new DigitalSignature();

        byte[] data = new byte[1024];
        byte[] invalidSignature = new byte[100];
        Random random = new SecureRandom();
        random.nextBytes(data);
        random.nextBytes(invalidSignature);
//
//        BigInteger integer = new BigInteger(1000, random);
//        Assert.assertEquals(integer, new BigInteger(integer.toByteArray()));

        InputStream inputStream = new ByteArrayInputStream(data);
        byte[] validSignature = digitalSignature.generateSignature(inputStream);


        InputStream toVerify = new ByteArrayInputStream(data);

        boolean verifiedValid = digitalSignature.verify(toVerify, validSignature);

        toVerify = new ByteArrayInputStream(data);

        boolean verifiedInvalid = digitalSignature.verify(toVerify, invalidSignature);


        Assert.assertTrue(verifiedValid);
        Assert.assertFalse(verifiedInvalid);
    }
//    @Test
//    public void decrypt_test() {
//        RsaCore rsa = new RsaCore(11, 23, 3);
//        BigUInt expectedPlain = new BigUInt(57);
//
//        byte[] plain = rsa.decrypt(new BigUInt(250).toByteArray());
//
//        Assert.assertTrue(new BigUInt(plain).equals(expectedPlain));
//    }

}
