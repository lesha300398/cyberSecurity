package lesha3003.sha.sha512;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import lesha3003.sha.BlockProvider;
import lesha3003.sha.padding.Padding;

public final class Sha512 {


    private static final long[] K = {
            0x428a2f98d728ae22L, 0x7137449123ef65cdL, 0xb5c0fbcfec4d3b2fL, 0xe9b5dba58189dbbcL, 0x3956c25bf348b538L,
            0x59f111f1b605d019L, 0x923f82a4af194f9bL, 0xab1c5ed5da6d8118L, 0xd807aa98a3030242L, 0x12835b0145706fbeL,
            0x243185be4ee4b28cL, 0x550c7dc3d5ffb4e2L, 0x72be5d74f27b896fL, 0x80deb1fe3b1696b1L, 0x9bdc06a725c71235L,
            0xc19bf174cf692694L, 0xe49b69c19ef14ad2L, 0xefbe4786384f25e3L, 0x0fc19dc68b8cd5b5L, 0x240ca1cc77ac9c65L,
            0x2de92c6f592b0275L, 0x4a7484aa6ea6e483L, 0x5cb0a9dcbd41fbd4L, 0x76f988da831153b5L, 0x983e5152ee66dfabL,
            0xa831c66d2db43210L, 0xb00327c898fb213fL, 0xbf597fc7beef0ee4L, 0xc6e00bf33da88fc2L, 0xd5a79147930aa725L,
            0x06ca6351e003826fL, 0x142929670a0e6e70L, 0x27b70a8546d22ffcL, 0x2e1b21385c26c926L, 0x4d2c6dfc5ac42aedL,
            0x53380d139d95b3dfL, 0x650a73548baf63deL, 0x766a0abb3c77b2a8L, 0x81c2c92e47edaee6L, 0x92722c851482353bL,
            0xa2bfe8a14cf10364L, 0xa81a664bbc423001L, 0xc24b8b70d0f89791L, 0xc76c51a30654be30L, 0xd192e819d6ef5218L,
            0xd69906245565a910L, 0xf40e35855771202aL, 0x106aa07032bbd1b8L, 0x19a4c116b8d2d0c8L, 0x1e376c085141ab53L,
            0x2748774cdf8eeb99L, 0x34b0bcb5e19b48a8L, 0x391c0cb3c5c95a63L, 0x4ed8aa4ae3418acbL, 0x5b9cca4f7763e373L,
            0x682e6ff3d6b2b8a3L, 0x748f82ee5defb2fcL, 0x78a5636f43172f60L, 0x84c87814a1f0ab72L, 0x8cc702081a6439ecL,
            0x90befffa23631e28L, 0xa4506cebde82bde9L, 0xbef9a3f7b2c67915L, 0xc67178f2e372532bL, 0xca273eceea26619cL,
            0xd186b8c721c0c207L, 0xeada7dd6cde0eb1eL, 0xf57d4f7fee6ed178L, 0x06f067aa72176fbaL, 0x0a637dc5a2c898a6L,
            0x113f9804bef90daeL, 0x1b710b35131c471bL, 0x28db77f523047d84L, 0x32caab7b40c72493L, 0x3c9ebe0a15c9bebcL,
            0x431d67c49c100d4cL, 0x4cc5d4becb3e42b6L, 0x597f299cfc657e2aL, 0x5fcb6fab3ad6faecL, 0x6c44198c4a475817L};

    private static final long[] H0 = {
            0x6a09e667f3bcc908L, 0xbb67ae8584caa73bL, 0x3c6ef372fe94f82bL, 0xa54ff53a5f1d36f1L,
            0x510e527fade682d1L, 0x9b05688c2b3e6c1fL, 0x1f83d9abfb41bd6bL, 0x5be0cd19137e2179L};

    // working arrays
    private static final long[] W = new long[80];
    private static final long[] H = new long[8];
    private static final long[] TEMP = new long[8];

    private static final int blockBits = 1024;
    private static final int blockBytes = blockBits / 8;

    /**
     * Hashes the given message with SHA-256 and returns the hash.
     *
     * @param input The input stream to hash.
     * @return The hash's bytes.
     */
    public static byte[] hash(InputStream input) throws IOException {
        Sha512Padding padding = new Sha512Padding();
        BlockProvider.BlockSequence blockSequence = new BlockProvider(blockBytes, padding).blocksFromInputStream(input);
        // let H = H0
        System.arraycopy(H0, 0, H, 0, H0.length);

        // initialize all words
        byte[] block;
        while ((block = blockSequence.nextBlock()) != null) {
            padding.addTotalUnpaddedLength(block.length);

            long[] words = toLongArray(block);

            // initialize W from the block's words
            System.arraycopy(words, 0, W, 0, 16);
            for (int t = 16; t < W.length; ++t) {
                W[t] = smallSig1(W[t - 2]) + W[t - 7] + smallSig0(W[t - 15])
                        + W[t - 16];
            }

            // let TEMP = H
            System.arraycopy(H, 0, TEMP, 0, H.length);

            // operate on TEMP
            for (int t = 0; t < W.length; ++t) {
                long t1 = TEMP[7] + bigSig1(TEMP[4])
                        + ch(TEMP[4], TEMP[5], TEMP[6]) + K[t] + W[t];
                long t2 = bigSig0(TEMP[0]) + maj(TEMP[0], TEMP[1], TEMP[2]);
                System.arraycopy(TEMP, 0, TEMP, 1, TEMP.length - 1);
                TEMP[4] += t1;
                TEMP[0] = t1 + t2;
            }

            // add values in TEMP to values in H
            for (int t = 0; t < H.length; ++t) {
                H[t] += TEMP[t];
            }


        }

        return toByteArray(H);
    }

    private static class Sha512Padding implements Padding {
        private BigInteger totalUnpaddedLengthBits = new BigInteger("0");

        void addTotalUnpaddedLength(int length) {
            totalUnpaddedLengthBits = totalUnpaddedLengthBits.add(new BigInteger(String.valueOf(length*8)));
        }

        public byte[] getPaddedBlock(byte[] block) {
            addTotalUnpaddedLength(block.length);

            // new message length: original + 1-bit and padding + 16-byte length
            int newMessageLength = block.length + 1 + 16;
            int padBytes = blockBytes - (newMessageLength % blockBytes);
            newMessageLength += padBytes;

            // copy message to extended array
            final byte[] paddedMessage = new byte[newMessageLength];
            System.arraycopy(block, 0, paddedMessage, 0, block.length);

            // write 1-bit
            paddedMessage[block.length] = (byte) 0b10000000;

            // skip padBytes many bytes (they are already 0)

            // write 8-byte integer describing the original message length
            int lenPos = block.length + 1 + padBytes;
            byte[] countBytes = new byte[16];
            for (int i = 0; i < countBytes.length; ++i) {
                countBytes[i] = 0;
            }
            byte[] bigIntBytes = totalUnpaddedLengthBits.toByteArray();
            System.arraycopy(bigIntBytes, 0, countBytes, countBytes.length - bigIntBytes.length, bigIntBytes.length);
            System.arraycopy(countBytes, 0, paddedMessage, lenPos, countBytes.length);
            return paddedMessage;

        }
    }

    /**
     * Converts the given byte array into an long array via big-endian conversion
     * (8 bytes become 1 long).
     *
     * @param bytes The source array.
     * @return The converted array.
     */
    private static long[] toLongArray(byte[] bytes) {
        if (bytes.length % Long.BYTES != 0) {
            throw new IllegalArgumentException("byte array length");
        }

        ByteBuffer buf = ByteBuffer.wrap(bytes);
        long[] result = new long[bytes.length / Long.BYTES];
        for (int i = 0; i < result.length; ++i) {
            result[i] = buf.getLong();
        }

        return result;
    }

    /**
     * Converts the given long array into a byte array via big-endian conversion
     * (1 long becomes 8 bytes).
     *
     * @param longs The source array.
     * @return The converted array.
     */
    private static byte[] toByteArray(long[] longs) {
        ByteBuffer buf = ByteBuffer.allocate(longs.length * Long.BYTES);
        for (int i = 0; i < longs.length; ++i) {
            buf.putLong(longs[i]);
        }

        return buf.array();
    }

    private static long ch(long x, long y, long z) {
        return (x & y) | ((~x) & z);
    }

    private static long maj(long x, long y, long z) {
        return (x & y) | (x & z) | (y & z);
    }

    private static long bigSig0(long x) {
        return Long.rotateRight(x, 28) ^ Long.rotateRight(x, 34)
                ^ Long.rotateRight(x, 39);
    }

    private static long bigSig1(long x) {
        return Long.rotateRight(x, 14) ^ Long.rotateRight(x, 18)
                ^ Long.rotateRight(x, 41);
    }

    private static long smallSig0(long x) {
        return Long.rotateRight(x, 1) ^ Long.rotateRight(x, 8)
                ^ (x >>> 7);
    }

    private static long smallSig1(long x) {
        return Long.rotateRight(x, 19) ^ Long.rotateRight(x, 61)
                ^ (x >>> 6);
    }
}
