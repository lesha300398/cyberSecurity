package lesha3003.ellipticcurves;

import java.security.SecureRandom;
import java.util.Random;

public final class Point {
    private Polynomial x, y;

    public static final Point Infinity = new Point(Polynomial.ZERO, Polynomial.ZERO);

    public static final Polynomial A = Polynomial.ONE;
    public static final Polynomial B = new Polynomial(Utils.hexStringToByteArray("7BC86E2102902EC4D5890E8B6B4981ff27E0482750FEFC03"));

    public Polynomial getX() {
        return x;
    }

    public Polynomial getY() {
        return y;
    }

    private Point(Polynomial x, Polynomial y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Point other) {
        return this.x.equals(other.x) && this.y.equals(other.y);
    }

    public Point add(Point other) {
        if (this.equals(Infinity)) {
            return other;
        }
        if (other.equals(Infinity)) {
            return this;
        }
        if (this.x.equals(other.x)) {
            if (this.y.equals(other.x.add(other.y)) || other.y.equals(this.x.add(this.y))) {
                return Infinity;
            }

            if (this.y.equals(other.y)) {
                return this.doublePoint();
            }

            throw new IllegalArgumentException();
        } else {
            Polynomial lambda = this.y.add(other.y).divide(this.x.add(other.x));
            Polynomial newX = lambda.square().add(lambda).add(this.x).add(other.x).add(A);
            Polynomial newY = lambda.multiply(this.x.add(newX)).add(newX).add(this.y);
            return new Point(newX, newY);
        }

    }

    public Point doublePoint() {
        if (this.x.equals(Polynomial.ZERO)) {
            throw new IllegalArgumentException();
        }
        Polynomial mu = this.x.add(this.y.divide(this.x));
        Polynomial newX = mu.square().add(mu).add(A);
        Polynomial newY = this.x.square().add(mu.add(Polynomial.ONE).multiply(newX));
        return new Point(newX, newY);

    }

    public Point multiplyInt(int val) {
        Point result = Infinity;
        Point temp = this;
        for (int i = 0; i < 32; i++) {
            if ((val & (1 << i)) != 0) {
                result = result.add(temp);
            }
            temp = temp.doublePoint();
        }
        return result;
    }

    public Point() {
        Random rnd = new SecureRandom();
        byte[] bytes = new byte[20];
        while (true) {
            Polynomial potentialX = null;
            Polynomial u = null;
            do {
                rnd.nextBytes(bytes);
                Polynomial pol = new Polynomial(bytes);
                if (trace(pol).equals(Polynomial.ZERO)) {
                    potentialX = pol;
                } else if (trace(pol).equals(Polynomial.ONE)) {
                    u = pol;
                } else {
                    throw new IllegalStateException();
                }
            } while (potentialX == null || u == null);
            this.x = potentialX;

            Polynomial xsq = x.square();
            Polynomial d = x.add(A).add(B.divide(xsq));

            Polynomial uSum = u;
            Polynomial uTemp = u;
            Polynomial dTemp = d.square();
            this.y = Polynomial.ONE;
            for (int i = 1; i < 192 - 1; i++) {
                this.y = y.add(dTemp.multiply(uSum));
                uTemp = uTemp.square();
                uSum = uSum.add(uTemp);
                dTemp = dTemp.square();
            }
            y = y.multiply(x);
            Polynomial lhs = y.square().add(y.multiply(x));
            Polynomial rhs = xsq.multiply(x).add(xsq.multiply(Point.A)).add(Point.B);
            if (lhs.equals(rhs)) {
                break;
            }
        }
    }

    private static Polynomial trace(Polynomial pol) {
        Polynomial result = Polynomial.ZERO;
        Polynomial temp = pol;
        for (int i = 0; i < 192 - 1; i++) {
            result = result.add(temp);
            temp = temp.square();
        }
        return result;
    }
}
