package lesha3003.digitalsignature.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;


public final class RsaCoreBigInteger {
        private BigInteger sk;
        private BigInteger pk;
        private BigInteger modulus;
        public RsaCoreBigInteger(int lenBits) {
            Random rnd = new SecureRandom();
            BigInteger p = BigInteger.probablePrime(lenBits / 2, rnd);
            BigInteger q = BigInteger.probablePrime(lenBits / 2, rnd);
            BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

            this.modulus = p.multiply(q);
            this.pk = new BigInteger("65537");
            this.sk = pk.modInverse(phi);
        }
        public RsaCoreBigInteger(int pInt, int qInt, int eInt) {
            BigInteger p = BigInteger.valueOf(pInt);
            BigInteger q = BigInteger.valueOf(qInt);
            BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

            this.modulus = p.multiply(q);
            this.pk = BigInteger.valueOf(eInt);
            this.sk = pk.modInverse(phi);
        }

        public BigInteger encrypt(BigInteger plain) {
            return plain.modPow(pk, modulus);
        }
        public BigInteger decrypt(BigInteger cipher) {
            return cipher.modPow(sk, modulus);
        }

}
