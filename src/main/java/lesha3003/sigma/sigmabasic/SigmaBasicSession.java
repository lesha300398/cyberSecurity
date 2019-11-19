package lesha3003.sigma.sigmabasic;

import java.io.IOException;

import lesha3003.ellipticcurves.Point;

public final class SigmaBasicSession {
    private SigmaBasicPeer a = new SigmaBasicPeer();
    private SigmaBasicPeer b = new SigmaBasicPeer();

    public Point point = new Point();

    public boolean init() throws IOException  {
        b.finishFirst(point, a.startFirst(point));

        if (!a.finishSecond(b.startSecond())) {
            return false;
        }

        if(!b.finishThird(a.startThird())){
            return false;
        }

        return true;
    }

}
