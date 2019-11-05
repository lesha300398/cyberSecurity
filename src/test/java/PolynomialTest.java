import org.junit.Assert;
import org.junit.Test;

import lesha3003.ellipticcurves.Polynomial;
import lesha3003.ellipticcurves.Utils;

public final class PolynomialTest {
    @Test
    public void add_test() {
        Polynomial first = new Polynomial(new byte[] { 0b01111101, 0b01100110, 0b00000000, (byte)0b10111010, 0b00011010 });
        Polynomial second = new Polynomial(new byte[] {  0b01100111, 0b00000010, (byte)0b10111010 });

        Polynomial expectedSum = new Polynomial(Utils.xorBytes(first.toByteArray(), second.toByteArray()));

        Polynomial sum = first.add(second);

        Assert.assertTrue(sum.equals(expectedSum));
    }
    @Test
    public void multiply_test() {
        Polynomial first = Polynomial.ONE.shiftLeft(190);
        Polynomial second = Polynomial.ONE.shiftLeft(13).add(Polynomial.ONE);

        Polynomial expectedProduct = Polynomial.ONE.shiftLeft(190).add(Polynomial.ONE.shiftLeft(21)).add(Polynomial.ONE.shiftLeft(12));

        Polynomial product = first.multiply(second);

        Assert.assertTrue(product.equals(expectedProduct));
    }
    @Test
    public void inv_test() {
        Polynomial first = Polynomial.ONE.shiftLeft(190).add(Polynomial.ONE.shiftLeft(8));

        Polynomial expectedResult = Polynomial.ONE.shiftLeft(1);

        Polynomial inv = first.inv();

        Assert.assertTrue(inv.equals(expectedResult));
    }

    @Test
    public void square_test() {
        Polynomial first = Polynomial.ONE.shiftLeft(50).add(Polynomial.ONE.shiftLeft(40));


        Polynomial expectedResult = Polynomial.ONE.shiftLeft(100).add(Polynomial.ONE.shiftLeft(80));

        Polynomial square = first.square();

        Assert.assertTrue(square.equals(expectedResult));
    }
}
