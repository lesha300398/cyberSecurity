package roundsteps;

import org.junit.Assert;
import org.junit.Test;

import lesha3003.aes.Utils;
import lesha3003.aes.roundsteps.MixColumns;
import lesha3003.aes.roundsteps.ShiftRows;

public class ShiftRowsTest {
    @Test
    public void shiftRows_test() {
        final byte[][] state = {
                {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03},
                {(byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13},
                {(byte) 0x20, (byte) 0x21, (byte) 0x22, (byte) 0x23},
                {(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33}
        };
        final byte[][] expectedResult = {
                {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03},
                {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x10},
                {(byte) 0x22, (byte) 0x23, (byte) 0x20, (byte) 0x21},
                {(byte) 0x33, (byte) 0x30, (byte) 0x31, (byte) 0x32}
        };

        byte[] stateBytes = Utils.matrixToBytes(state);
        ShiftRows.shiftRows(stateBytes);

        Assert.assertArrayEquals(expectedResult, Utils.bytesToMatrix(stateBytes));
    }
    @Test
    public void invShiftRows_test() {
        final byte[][] state = {
                {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03},
                {(byte) 0x11, (byte) 0x12, (byte) 0x13, (byte) 0x10},
                {(byte) 0x22, (byte) 0x23, (byte) 0x20, (byte) 0x21},
                {(byte) 0x33, (byte) 0x30, (byte) 0x31, (byte) 0x32}
        };
        final byte[][] expectedResult = {
                {(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03},
                {(byte) 0x10, (byte) 0x11, (byte) 0x12, (byte) 0x13},
                {(byte) 0x20, (byte) 0x21, (byte) 0x22, (byte) 0x23},
                {(byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33}
        };

        byte[] stateBytes = Utils.matrixToBytes(state);
        ShiftRows.invShiftRows(stateBytes);

        Assert.assertArrayEquals(expectedResult, Utils.bytesToMatrix(stateBytes));
    }
}
