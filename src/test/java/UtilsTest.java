import org.junit.Assert;
import org.junit.Test;

import lesha3003.aes.Utils;

public class UtilsTest {
    @Test
    public void bytesToMatrix_test(){
        byte[] bytes = new byte[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        byte[][] expectedMatrix = new byte[][] {
                {0, 4, 8, 12},
                {1, 5, 9, 13},
                {2, 6, 10, 14},
                {3, 7, 11, 15}
        };

        byte[][] matrix = Utils.bytesToMatrix(bytes);

        Assert.assertArrayEquals(expectedMatrix, matrix);
    }

    @Test
    public void matrixToBytes_test() {
        byte[][] matrix = new byte[][] {
                {0, 4, 8, 12},
                {1, 5, 9, 13},
                {2, 6, 10, 14},
                {3, 7, 11, 15}
        };
        byte[] expectedBytes = new byte[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};

        byte[] bytes = Utils.matrixToBytes(matrix);

        Assert.assertArrayEquals(expectedBytes, bytes);
    }
    @Test
    public void hexToByteArray_test() {
        String string = "00fdadf01273";
        byte[] expectedBytes = {(byte) 0x00, (byte) 0xfd, (byte) 0xad, (byte) 0xf0, (byte) 0x12, (byte) 0x73 };

        byte[] bytes = Utils.hexStringToByteArray(string);

        Assert.assertArrayEquals(expectedBytes, bytes);
    }
}
