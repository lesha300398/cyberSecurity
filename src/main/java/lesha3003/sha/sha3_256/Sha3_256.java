package lesha3003.sha.sha3_256;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import lesha3003.sha.BlockProvider;
import lesha3003.sha.Utils;
import lesha3003.sha.padding.Padding;

public final class Sha3_256 {
    private static final int WIDTH = 200; // in bytes, e.g. 1600 bits
    private static final int DM = 5; // dimension of longs

    private static final int NR = 24; // number of rounds

    private static final int RATE = 136;
    private static final int CAPACITY = WIDTH - RATE;

    private static final int HASH_WIDTH = 256 / 8;

    // precomputed round constants needed by the step mapping Iota
    private static final long[] RC_CONSTANTS = {
            0x01L, 0x8082L, 0x800000000000808aL,
            0x8000000080008000L, 0x808bL, 0x80000001L,
            0x8000000080008081L, 0x8000000000008009L, 0x8aL,
            0x88L, 0x80008009L, 0x8000000aL,
            0x8000808bL, 0x800000000000008bL, 0x8000000000008089L,
            0x8000000000008003L, 0x8000000000008002L, 0x8000000000000080L,
            0x800aL, 0x800000008000000aL, 0x8000000080008081L,
            0x8000000000008080L, 0x80000001L, 0x8000000080008008L,
    };

    private byte[] stateBytes = new byte[WIDTH];
    private long[] stateLongs = new long[DM*DM];


    public byte[] hash(InputStream is) throws IOException {
        BlockProvider.BlockSequence blockSequence = new BlockProvider(RATE, new Sha3_256Padding()).blocksFromInputStream(is);
        byte[] block;

        for (int i = 0; i < WIDTH; i++) {
            stateBytes[i] = (byte) 0x00;
        }

        while ((block = blockSequence.nextBlock()) != null) {
            for (int i = 0; i < RATE; i++) {
                stateBytes[i] ^= block[i];
            }
            keccak();
        }
        byte[] hash = new byte[HASH_WIDTH];
        for (int i = 0, j = 0;;) {
            hash[j] = stateBytes[j];
            i++;
            j++;
            if (i >= HASH_WIDTH) {
                break;
            }
            if (j >= RATE) {
                j = 0;
                keccak();
            }
        }
        return hash;
    }


    private static class Sha3_256Padding implements Padding {

        public byte[] getPaddedBlock(byte[] block) {
            if (block.length == RATE - 1) {
                byte[] paddedBlock = new byte[RATE];
                System.arraycopy(block, 0, paddedBlock, 0, block.length);
                paddedBlock[RATE - 1] = (byte) 0b01100001;
                return paddedBlock;
            }


            // new message length: original + 1-bit and padding + 1-bit
            int newMessageLength = block.length + 2;
            int padBytes = RATE - (newMessageLength % RATE);
            newMessageLength += padBytes;

            // copy message to extended array
            final byte[] paddedMessage = new byte[newMessageLength];
            System.arraycopy(block, 0, paddedMessage, 0, block.length);

            // write 1-bit
            paddedMessage[block.length] = (byte) 0b01100000;

            // skip padBytes many bytes (they are already 0)

            // write 1-bit
            paddedMessage[newMessageLength-1] = (byte) 0b00000001;
            return paddedMessage;
        }
    }

    private void keccak() {
        toLongState();
        for (int round = 0; round < NR; round++) {
            iota(chi(pi(rho(theta(stateLongs)))), round);
        }
        toByteState();
    }

    private void toLongState() {
//        if (bytes.length % Long.BYTES != 0) {
//            throw new IllegalArgumentException("byte array length");
//        }

        ByteBuffer buf = ByteBuffer.wrap(stateBytes);
//        buf.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < stateLongs.length; ++i) {
            stateLongs[i] = buf.getLong();
        }

    }


    private void toByteState() {
        ByteBuffer buf = ByteBuffer.wrap(stateBytes);
//        buf.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < stateLongs.length; ++i) {
            buf.putLong(stateLongs[i]);
        }
    }

    private long[] theta(long[] state) {
        long c0 = state[0]^state[5]^state[10]^state[15]^state[20];
        long c1 = state[1]^state[6]^state[11]^state[16]^state[21];
        long c2 = state[2]^state[7]^state[12]^state[17]^state[22];
        long c3 = state[3]^state[8]^state[13]^state[18]^state[23];
        long c4 = state[4]^state[9]^state[14]^state[19]^state[24];
        long d0 = c4 ^ Long.rotateRight(c1, 1);
        long d1 = c0 ^ Long.rotateRight(c2, 1);
        long d2 = c1 ^ Long.rotateRight(c3, 1);
        long d3 = c2 ^ Long.rotateRight(c4, 1);
        long d4 = c3 ^ Long.rotateRight(c0, 1);

//        long c0 = state[0]^state[1]^state[2]^state[3]^state[4];
//        long c1 = state[5]^state[6]^state[7]^state[8]^state[9];
//        long c2 = state[10]^state[11]^state[12]^state[13]^state[14];
//        long c3 = state[15]^state[16]^state[17]^state[18]^state[19];
//        long c4 = state[20]^state[21]^state[2]^state[23]^state[24];
//        long d0 = c4 ^ Long.rotateLeft(c1, 1);
//        long d1 = c0 ^ Long.rotateLeft(c2, 1);
//        long d2 = c1 ^ Long.rotateLeft(c3, 1);
//        long d3 = c2 ^ Long.rotateLeft(c4, 1);
//        long d4 = c3 ^ Long.rotateLeft(c0, 1);
        for (int i = 0; i < state.length; i += 5) {
            state[i] ^= d0;
            state[i+1] ^= d1;
            state[i+2] ^= d2;
            state[i+3] ^= d3;
            state[i+4] ^= d4;
        }
        return state;
    }
    private long[] rho(long[] state) {
//        for(int i = 0, rot = 0; i < state.length; i++) {
//            rot += i;
//            state[i] = Long.rotateRight(state[i], rot);
//        }
        int i = 0, j = 1;
        for (int t = 0; t < 24; t++) {
            state[5*i + j] = Long.rotateRight(state[5*i + j], (t+1)*(t+2)/2);
            int oldi = i, oldj = j;
            j = oldi;
            i = (2*oldj + 3*oldi) % 5;
        }
        return state;
    }
    private long[] pi(long[] state) {
        long[] a = new long[state.length];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
//                a[5*j + (2*i + 3*j) % 5] = state[5*i + j];
                a[5*i + j] = state[5*j + ((3*i + j) % 5)];
            }
        }
        System.arraycopy(a, 0, state, 0 ,a.length);
        toByteState();
        System.out.print(Utils.byteArrayToHexStringLittleEndian(stateBytes));
        return state;
    }
    private long[] chi(long[] state) {
        for (int i = 0; i < 5; i++) {
            int base = 5*i;
            long s0 = state[base];
            long s1 = state[base + 1];
            state[base] ^= (~state[base + 1] & state[base + 2]);
            state[base + 1] ^= (~state[base + 2] & state[base + 3]);
            state[base + 2] ^= (~state[base + 3] & state[base + 4]);
            state[base + 3] ^= (~state[base + 4] & s0);
            state[base + 4] ^= (~s0 & s1);
        }
        return state;
    }
    private long[] iota(long[] state, int round) {
        state[0] ^= RC_CONSTANTS[round];
        return state;
    }

}
