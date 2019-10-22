package lesha3003.digitalsignature;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import lesha3003.digitalsignature.rsa.RsaCoreBigInteger;
import lesha3003.sha.sha512.Sha512;

public class DigitalSignature {

    private RsaCoreBigInteger rsa = new RsaCoreBigInteger(3078);

    public DigitalSignature() {

    }
    public byte[] generateSignature(InputStream inputStream) throws IOException {
        return rsa.decrypt(new BigInteger(Sha512.hash(inputStream))).toByteArray();
    }
    public boolean verify(InputStream inputStream, byte[] signature) throws IOException {
        return rsa.encrypt(new BigInteger(signature)).toByteArray() == Sha512.hash(inputStream);
    }
}
