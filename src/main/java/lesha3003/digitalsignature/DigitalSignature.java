package lesha3003.digitalsignature;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import lesha3003.digitalsignature.rsa.Rsa;
import lesha3003.sha.sha512.Sha512;

public class DigitalSignature {

    private Rsa rsa = new Rsa(3078 / 8);

    public DigitalSignature() {

    }
    public byte[] generateSignature(InputStream inputStream) throws IOException {
        return rsa.decrypt(rsa.pad(Sha512.hash(inputStream)));
    }
    public boolean verify(InputStream inputStream, byte[] signature) throws IOException {
       try {
           byte[] hash = rsa.unpad(rsa.encrypt(signature));
           byte[] trueHash = Sha512.hash(inputStream);
           return Arrays.equals(hash, trueHash);
        } catch (Rsa.UnpaddingException ex) {
           return false;
       }
    }
}
