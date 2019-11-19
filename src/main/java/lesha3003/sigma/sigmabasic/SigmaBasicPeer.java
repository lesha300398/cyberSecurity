package lesha3003.sigma.sigmabasic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

import lesha3003.digitalsignature.DigitalSignature;
import lesha3003.ellipticcurves.Point;
import lesha3003.ellipticcurves.Utils;
import lesha3003.sha.sha512.Sha512;
import lesha3003.sigma.Peer;
import lesha3003.sigma.PublicKeyProvider;
import lesha3003.sigma.hmac.HmacSha3;

public final class SigmaBasicPeer extends Peer {


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

    byte[][] startSecond() throws IOException {
        byte[][] result = new byte[2][];
        result[0] = gthis.toByteArray();
        byte[] toSign = Utils.concat(gother.toByteArray(), gthis.toByteArray());
        result[1] = Utils.concatAll(idBytes, signature.generateSignature(toSign), HmacSha3.hmac(idBytes, getKm()));
        return result;
    }

    boolean finishSecond(byte[][] payload) throws IOException {
        byte[] gotherBytes = payload[0];
        this.gother = new Point(gotherBytes);
        this.gxy = gother.multiplyLong(coef);
        byte[] otherIdBytes = Arrays.copyOfRange(payload[1], 0,16);

        otherId = ByteBuffer.wrap(otherIdBytes).getLong();
        byte[] otherIdMac = Arrays.copyOfRange(payload[1], payload[1].length - HmacSha3.outputSize, payload[1].length);
        byte[] signatureBytes = Arrays.copyOfRange(payload[1], 16, payload[1].length - HmacSha3.outputSize);

        byte[] km = getKm();
        byte[] mac = HmacSha3.hmac(otherIdBytes, km);
        if (!Arrays.equals(otherIdMac, mac)) {
            return false;
        }

        if(!DigitalSignature.verify(Utils.concat(gthis.toByteArray(), gother.toByteArray()), signatureBytes, PublicKeyProvider.getPk(otherId), PublicKeyProvider.getModulus(otherId))){
            return false;
        }

        return true;

    }

    byte[] startThird() throws IOException {
        byte[] toSign = Utils.concat(gother.toByteArray(), gthis.toByteArray());
        return Utils.concatAll(idBytes, signature.generateSignature(toSign), HmacSha3.hmac(idBytes, getKm()));
    }
    boolean finishThird(byte[] payload) throws IOException {
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

    private byte[] getKm() throws IOException {
        return Sha512.hash(gxy.toByteArray());
    }


}
