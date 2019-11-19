package lesha3003.sigma.sigmar;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import lesha3003.aes.Aes;
import lesha3003.digitalsignature.DigitalSignature;
import lesha3003.ellipticcurves.Point;
import lesha3003.ellipticcurves.Utils;
import lesha3003.sha.sha3_256.Sha3_256;
import lesha3003.sha.sha512.Sha512;
import lesha3003.sigma.Peer;
import lesha3003.sigma.PublicKeyProvider;
import lesha3003.sigma.hmac.HmacSha3;

public final class SigmaRPeer extends Peer {
    private Aes aes = new Aes(Aes.KeySize.AES_256, Aes.Mode.CTR);


    Point startFirst(Point g) {
        this.g = g;
        this.gthis = g.multiplyLong(coef);
        return gthis;
    }

    void finishFirst(Point g, Point gx) {
        this.g = g;
        this.gthis = g.multiplyLong(coef);
        this.gother = gx;
        this.gxy = gother.multiplyLong(coef);
    }

    Point startSecond(Point g) {
        this.g = g;
        this.gthis = g.multiplyLong(coef);
        return gthis;
    }

    void finishSecond(Point g, Point gy) {
        this.g = g;
        this.gthis = g.multiplyLong(coef);
        this.gother = gy;
        this.gxy = gother.multiplyLong(coef);
    }


    byte[] startThird() throws IOException {
        byte[] toSign = Utils.concat(gother.toByteArray(), gthis.toByteArray());
        return aes.encrypt(Utils.concatAll(idBytes, signature.generateSignature(toSign), HmacSha3.hmac(idBytes, getKm())), getKe(), null, null);
    }
    boolean finishThird(byte[] payload) throws IOException {
        payload = aes.decrypt(payload, getKe(), null, null);
        byte[] otherIdBytes = Arrays.copyOfRange(payload, 0,16);

        otherId = ByteBuffer.wrap(otherIdBytes).getLong();
        byte[] otherIdMac = Arrays.copyOfRange(payload, payload.length - HmacSha3.outputSize, payload.length);
        byte[] signatureBytes = Arrays.copyOfRange(payload, 16, payload.length - HmacSha3.outputSize);

        if (!Arrays.equals(otherIdMac, HmacSha3.hmac(otherIdBytes, getKm()))) {
            return false;
        }

        if(!DigitalSignature.verify(Utils.concat(gthis.toByteArray(), gother.toByteArray()), signatureBytes, PublicKeyProvider.getPk(otherId), PublicKeyProvider.getModulus(otherId))){
            return false;
        }

        return true;
    }
    byte[] startFourth() throws IOException {
        byte[] toSign = Utils.concat(gother.toByteArray(), gthis.toByteArray());
        return aes.encrypt(Utils.concatAll(idBytes, signature.generateSignature(toSign), HmacSha3.hmac(idBytes, getKmPrime())), getKePrime(), null, null);
    }
    boolean finishFourth(byte[] payload) throws IOException {
        payload = aes.decrypt(payload, getKePrime(), null, null);
        byte[] otherIdBytes = Arrays.copyOfRange(payload, 0,16);

        otherId = ByteBuffer.wrap(otherIdBytes).getLong();
        byte[] otherIdMac = Arrays.copyOfRange(payload, payload.length - HmacSha3.outputSize, payload.length);
        byte[] signatureBytes = Arrays.copyOfRange(payload, 16, payload.length - HmacSha3.outputSize);

        if (!Arrays.equals(otherIdMac, HmacSha3.hmac(otherIdBytes, getKmPrime()))) {
            return false;
        }

        if(!DigitalSignature.verify(Utils.concat(gthis.toByteArray(), gother.toByteArray()), signatureBytes, PublicKeyProvider.getPk(otherId), PublicKeyProvider.getModulus(otherId))){
            return false;
        }

        return true;
    }

    private byte[] getKm() throws IOException {
        return Sha512.hash(gxy.toByteArray());
    }

    private byte[] getKe() throws IOException {
        return new Sha3_256().hash(gxy.toByteArray());
    }

    private byte[] getKmPrime() throws IOException {
        byte[] bytes = gxy.toByteArray();
        byte[] newBytes = new byte[bytes.length];
        System.arraycopy(bytes, 0 , newBytes, bytes.length / 2, bytes.length / 2);
        System.arraycopy(bytes, bytes.length / 2 , newBytes, 0, bytes.length / 2);
        return Sha512.hash(newBytes);
    }

    private byte[] getKePrime() throws IOException {
        byte[] bytes = gxy.toByteArray();
        byte[] newBytes = new byte[bytes.length];
        System.arraycopy(bytes, 0 , newBytes, bytes.length / 2, bytes.length / 2);
        System.arraycopy(bytes, bytes.length / 2 , newBytes, 0, bytes.length / 2);
        return new Sha3_256().hash(newBytes);
    }


}
