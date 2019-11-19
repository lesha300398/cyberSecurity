package sigmai;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import lesha3003.sigma.sigmai.SigmaISession;

public final class SigmaITest {
    @Test
    public void test() throws IOException {
        SigmaISession session = new SigmaISession();

        Assert.assertTrue(session.init());
    }
}
