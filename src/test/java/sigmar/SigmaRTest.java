package sigmar;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import lesha3003.sigma.sigmar.SigmaRSession;


public final class SigmaRTest {
    @Test
    public void test() throws IOException {
        SigmaRSession session = new SigmaRSession();

        Assert.assertTrue(session.init());
    }
}
