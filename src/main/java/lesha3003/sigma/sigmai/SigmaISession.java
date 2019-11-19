package lesha3003.sigma.sigmai;

import java.io.IOException;

import lesha3003.ellipticcurves.Point;

public final class SigmaISession {
    private SigmaIPeer a = new SigmaIPeer();
    private SigmaIPeer b = new SigmaIPeer();

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
