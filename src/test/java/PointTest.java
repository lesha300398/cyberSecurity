import org.junit.Assert;
import org.junit.Test;

import lesha3003.ellipticcurves.Point;
import lesha3003.ellipticcurves.Polynomial;

public final class PointTest {
    @Test
    public void generation_test() {
        Point point = new Point();

        Polynomial lhs = point.getY().square().add(point.getY().multiply(point.getX()));
        Polynomial rhs = point.getX().square().multiply(point.getX()).add(point.getX().square().multiply(Point.A)).add(Point.B);
        boolean equals = lhs.equals(rhs);
        Assert.assertTrue(equals);
    }
    @Test
    public void add_test() {
        Point p1 = new Point();
        Point p2 = new Point();

        long start = System.nanoTime();
        Point sum = p1.add(p2);
        System.out.printf("Add in %d nanoseconds\n", System.nanoTime() - start);

        Assert.assertTrue(true);
    }
    @Test
    public void double_test() {
        Point p1 = new Point();

        long start = System.nanoTime();
        Point sum = p1.doublePoint();
        System.out.printf("Double in %d nanoseconds\n", System.nanoTime() - start);

        Assert.assertTrue(true);
    }
    @Test
    public void multiplyInt_test() {
        Point p1 = new Point();
        int val = Integer.MAX_VALUE;

        long start = System.nanoTime();
        Point sum = p1.multiplyInt(val);
        System.out.printf("Int multiply in %d nanoseconds\n", System.nanoTime() - start);

        Assert.assertTrue(true);
    }
}
