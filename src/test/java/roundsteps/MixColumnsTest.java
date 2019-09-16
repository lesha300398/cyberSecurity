package roundsteps;

import org.junit.Assert;
import org.junit.Test;

import lesha3003.aes.roundsteps.MixColumns;

public class MixColumnsTest {
    // test cases from https://en.wikipedia.org/wiki/Rijndael_MixColumns
    @Test
    public void mixColumns_test() {
        final byte[][] state = {
                {(byte) 0xDB, (byte) 0xF2, (byte) 0xD4, (byte) 0xC6},
                {(byte) 0x13, (byte) 0x0A, (byte) 0xD4, (byte) 0xC6},
                {(byte) 0x53, (byte) 0x22, (byte) 0xD4, (byte) 0xC6},
                {(byte) 0x45, (byte) 0x5C, (byte) 0xD5, (byte) 0xC6}
        };
        final byte[][] expectedResult = {
                {(byte) 0x8E, (byte) 0x9F, (byte) 0xD5, (byte) 0xC6},
                {(byte) 0x4D, (byte) 0xDC, (byte) 0xD5, (byte) 0xC6},
                {(byte) 0xA1, (byte) 0x58, (byte) 0xD7, (byte) 0xC6},
                {(byte) 0xBC, (byte) 0x9D, (byte) 0xD6, (byte) 0xC6}
        };

        byte[][] result = MixColumns.mixColumns(state);

        Assert.assertArrayEquals(expectedResult,result);
    }
    @Test
    public void invMixColumns_test() {
        final byte[][] state = {
                {(byte) 0x8E, (byte) 0x9F, (byte) 0xD5, (byte) 0xC6},
                {(byte) 0x4D, (byte) 0xDC, (byte) 0xD5, (byte) 0xC6},
                {(byte) 0xA1, (byte) 0x58, (byte) 0xD7, (byte) 0xC6},
                {(byte) 0xBC, (byte) 0x9D, (byte) 0xD6, (byte) 0xC6}
        };
        final byte[][] expectedResult = {
                {(byte) 0xDB, (byte) 0xF2, (byte) 0xD4, (byte) 0xC6},
                {(byte) 0x13, (byte) 0x0A, (byte) 0xD4, (byte) 0xC6},
                {(byte) 0x53, (byte) 0x22, (byte) 0xD4, (byte) 0xC6},
                {(byte) 0x45, (byte) 0x5C, (byte) 0xD5, (byte) 0xC6}
        };

        byte[][] result = MixColumns.invMixColumns(state);

        Assert.assertArrayEquals(expectedResult,result);
    }
}
