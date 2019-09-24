package roundsteps;

import org.junit.Assert;
import org.junit.Test;

import lesha3003.aes.Utils;
import lesha3003.aes.roundsteps.SubBytes;

public class SubBytesTest {
    // expectedResult using https://en.wikipedia.org/wiki/Rijndael_S-box
    @Test
    public void subBytes_test() {
        final byte[][] state = {
                {(byte) 0x00, (byte) 0x0D, (byte) 0x8E, (byte) 0xFD},
                {(byte) 0x66, (byte) 0x08, (byte) 0xE8, (byte) 0xDF},
                {(byte) 0xBB, (byte) 0x80, (byte) 0x3B, (byte) 0xF8},
                {(byte) 0xFF, (byte) 0xD0, (byte) 0xB3, (byte) 0x8F}
        };
        final byte[][] expectedResult = {
                {(byte) 0x63, (byte) 0xD7, (byte) 0x19, (byte) 0x54},
                {(byte) 0x33, (byte) 0x30, (byte) 0x9B, (byte) 0x9E},
                {(byte) 0xEA, (byte) 0xCD, (byte) 0xE2, (byte) 0x41},
                {(byte) 0x16, (byte) 0x70, (byte) 0x6D, (byte) 0x73}
        };

        byte[] stateBytes = Utils.matrixToBytes(state);
        SubBytes.subBytes(stateBytes);
        Assert.assertArrayEquals(expectedResult, Utils.bytesToMatrix(stateBytes));
    }
    @Test
    public void invSubBytes_test() {
        final byte[][] state = {
                {(byte) 0x63, (byte) 0xD7, (byte) 0x19, (byte) 0x54},
                {(byte) 0x33, (byte) 0x30, (byte) 0x9B, (byte) 0x9E},
                {(byte) 0xEA, (byte) 0xCD, (byte) 0xE2, (byte) 0x41},
                {(byte) 0x16, (byte) 0x70, (byte) 0x6D, (byte) 0x73}
        };
        final byte[][] expectedResult = {
                {(byte) 0x00, (byte) 0x0D, (byte) 0x8E, (byte) 0xFD},
                {(byte) 0x66, (byte) 0x08, (byte) 0xE8, (byte) 0xDF},
                {(byte) 0xBB, (byte) 0x80, (byte) 0x3B, (byte) 0xF8},
                {(byte) 0xFF, (byte) 0xD0, (byte) 0xB3, (byte) 0x8F}
        };
        byte[] stateBytes = Utils.matrixToBytes(state);
        SubBytes.invSubBytes(stateBytes);
        Assert.assertArrayEquals(expectedResult, Utils.bytesToMatrix(stateBytes));
    }
}
