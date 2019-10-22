package lesha3003.digitalsignature.rsa;

import java.security.SecureRandom;
import java.util.Random;

import lesha3003.digitalsignature.biguint.BigUInt;

public final class RsaCore {
    private BigUInt sk;
    private BigUInt pk;
    private BigUInt modulus;
    public RsaCore(int lenBits) {
        Random rnd = new SecureRandom();
        BigUInt p = BigUInt.probablePrime(lenBits / 2, rnd);
        BigUInt q = BigUInt.probablePrime(lenBits / 2, rnd);
        BigUInt phi = p.subtract(BigUInt.one()).multiply(q.subtract(BigUInt.one()));

        this.modulus = p.multiply(q);
        this.pk = new BigUInt(65537);
        this.sk = pk.modInv(phi);
    }
    public RsaCore(int pInt, int qInt, int eInt) {
        BigUInt p = new BigUInt(pInt);
        BigUInt q = new BigUInt(qInt);
        BigUInt phi = p.subtract(BigUInt.one()).multiply(q.subtract(BigUInt.one()));

        this.modulus = p.multiply(q);
        this.pk = new BigUInt(eInt);
        this.sk = pk.modInv(phi);
    }

    public byte[] encrypt(byte[] plain) {
        return new BigUInt(plain).powMod(pk, modulus).toByteArray();
    }
    public byte[] decrypt(byte[] cipher) {
        return new BigUInt(cipher).powMod(sk, modulus).toByteArray();
    }
}
