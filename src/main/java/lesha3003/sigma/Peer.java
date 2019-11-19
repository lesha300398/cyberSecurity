package lesha3003.sigma;

import java.nio.ByteBuffer;
import java.security.SecureRandom;

import lesha3003.digitalsignature.DigitalSignature;
import lesha3003.ellipticcurves.Point;

public class Peer {
    protected DigitalSignature signature = new DigitalSignature();

    protected long id = new SecureRandom().nextLong();
    protected byte[] idBytes = ByteBuffer.allocate(16).putLong(id).array();

    protected long coef = new SecureRandom().nextLong();
    protected Point g, gthis, gother, gxy;
    protected long otherId;

    public Peer() {
        PublicKeyProvider.registerPk(id, signature.pk(), signature.modulus());
    }

}
