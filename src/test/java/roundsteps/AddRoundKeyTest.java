package roundsteps;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import lesha3003.aes.roundsteps.AddRoundKey;

public class AddRoundKeyTest {
    @Test
    public void addRoundKey_test(){
        Random random = new Random();

        final byte[] state = new byte[16];
        final byte[] key = new byte[16];
        final byte[] expectedResult = new byte[16];

            random.nextBytes(state);
            for (int i = 0; i < 16; i++) {
                boolean same = random.nextBoolean();
                key[i] = same ? state[i] : (byte) ~state[i];
                expectedResult[i] = same ? (byte) 0x00 : (byte) 0xFF;
            }

        AddRoundKey.addRoundKey(state, key);
        Assert.assertArrayEquals(expectedResult, state);
    }
}
