package lesha3003.sigma.sigmar;

import java.io.IOException;

import lesha3003.ellipticcurves.Point;

public final class SigmaRSession {
    private SigmaRPeer a = new SigmaRPeer();
    private SigmaRPeer b = new SigmaRPeer();

    public Point point = new Point();

    public boolean init() throws IOException  {
        b.finishFirst(point, a.startFirst(point));

        a.finishSecond(point, b.startSecond(point));

        if(!b.finishThird(a.startThird())){
            return false;
        }
        if(!a.finishFourth(b.startFourth())){
            return false;
        }

        return true;
    }

}
