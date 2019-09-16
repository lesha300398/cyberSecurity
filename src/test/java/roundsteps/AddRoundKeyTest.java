package roundsteps;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import lesha3003.aes.roundsteps.AddRoundKey;

public class AddRoundKeyTest {
    @Test
    public void addRoundKey_test(){
        Random random = new Random();

        final byte[][] state = new byte[4][4];
        final byte[][] key = new byte[4][4];
        final byte[][] expectedResult = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            random.nextBytes(state[i]);
            for (int j = 0; j < 4; j++) {
                boolean same = random.nextBoolean();
                key[i][j] = same ? state[i][j] : (byte) ~state[i][j];
                expectedResult[i][j] = same ? (byte) 0x00 : (byte) 0xFF;
            }
        }
        byte[][] result = AddRoundKey.addRoundKey(state, key);
        Assert.assertArrayEquals(expectedResult, result);
    }
}
