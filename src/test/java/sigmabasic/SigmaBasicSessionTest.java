package sigmabasic;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import lesha3003.sigma.sigmabasic.SigmaBasicSession;

public class SigmaBasicSessionTest {
    @Test
    public void test() throws IOException {
        SigmaBasicSession session = new SigmaBasicSession();

        Assert.assertTrue(session.init());
    }
}
