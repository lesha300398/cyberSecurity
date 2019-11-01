import org.junit.Assert;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import lesha3003.digitalsignature.biguint.BigUInt;

public final class BigUIntTest {
    @Test
    public void equalsTrue_test() {
        long intVal = 2398345227836582L;
        BigUInt val1 = new BigUInt(intVal);
        BigUInt val2 = new BigUInt(intVal);

        Assert.assertTrue(val1.equals(val2) && val2.equals(val1));
    }

    @Test
    public void equalsFalse_test() {
        long intVal1 = 2398345298372523L;
        long intVal2 = 57483291327285L;
        BigUInt val1 = new BigUInt(intVal1);
        BigUInt val2 = new BigUInt(intVal2);

        Assert.assertTrue(!val1.equals(val2) && !val2.equals(val1));
    }

    @Test
    public void greaterThan_equal_test() {
        long intVal = 239834522345566L;
        BigUInt val1 = new BigUInt(intVal);
        BigUInt val2 = new BigUInt(intVal);

        Assert.assertTrue(!val1.greaterThan(val2) && !val2.greaterThan(val1));
    }

    @Test
    public void greaterThan_greater_test() {
        long intVal1 = 23983452387465L;
        long intVal2 = 5748329138273623567L;
        BigUInt val1 = new BigUInt(intVal1);
        BigUInt val2 = new BigUInt(intVal2);

        Assert.assertTrue(!val1.greaterThan(val2) && val2.greaterThan(val1));
    }

    @Test
    public void greaterThanOrEqual_equal_test() {
        long intVal = 239834526789023425L;
        BigUInt val1 = new BigUInt(intVal);
        BigUInt val2 = new BigUInt(intVal);

        Assert.assertTrue(val1.greaterThanOrEquals(val2) && val2.greaterThanOrEquals(val1));
    }

    @Test
    public void greaterThanOrEqual_greater_test() {
        long intVal1 = 239834528473549L;
        long intVal2 = 574832913487835324L;
        BigUInt val1 = new BigUInt(intVal1);
        BigUInt val2 = new BigUInt(intVal2);

        Assert.assertTrue(!val1.greaterThanOrEquals(val2) && val2.greaterThanOrEquals(val1));
    }

    @Test
    public void add_test() {
        long intVal1 = (long) Integer.MAX_VALUE * 20;
        long intVal2 = (long) Integer.MAX_VALUE * 1000;
        BigUInt val1 = new BigUInt(intVal1);
        BigUInt val2 = new BigUInt(intVal2);
        BigUInt expectedSum = new BigUInt(intVal1 + intVal2);

        BigUInt sum1 = val1.add(val2);
        BigUInt sum2 = val2.add(val1);

        Assert.assertTrue(expectedSum.equals(sum1) && expectedSum.equals(sum2));
    }

    @Test
    public void subtract_test() {
        long intVal1 = (long) Integer.MAX_VALUE * 20;
        long intVal2 = (long) Integer.MAX_VALUE * 1000;
        final BigUInt val1 = new BigUInt(intVal1);
        final BigUInt val2 = new BigUInt(intVal2);
        BigUInt expectedDiff = new BigUInt(intVal2 - intVal1);

        BigUInt diff = val2.subtract(val1);
        ThrowingRunnable wrongDiff = new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                val1.subtract(val2);
            }
        };


        Assert.assertTrue(expectedDiff.equals(diff));
        Assert.assertThrows(IllegalArgumentException.class, wrongDiff);
    }

    @Test
    public void multiply_test() {
        long intVal1 = (long) Integer.MAX_VALUE + 1;
        long intVal2 = (long) Integer.MAX_VALUE + 2;
        long prod = intVal1 * intVal2;
        BigUInt val1 = new BigUInt(intVal1);
        BigUInt val2 = new BigUInt(intVal2);
        BigUInt expectedProduct = new BigUInt(intVal1 * intVal2);

        BigUInt product1 = val1.multiply(val2);
        BigUInt product2 = val2.multiply(val1);

        Assert.assertTrue(expectedProduct.equals(product1) && expectedProduct.equals(product2));
    }

    @Test
    public void mod_test() {
        long intModulus = (long) Integer.MAX_VALUE + 1;
        long intVal = (long) Integer.MAX_VALUE * 2934879234L;
        BigUInt val = new BigUInt(intVal);
        BigUInt modulus = new BigUInt(intModulus);
        BigUInt expectedResult = new BigUInt(intVal % intModulus);

        BigUInt result = val.mod(modulus);

        Assert.assertTrue(expectedResult.equals(result));
    }

    //    @Test
//    public void divideThisByTwo_test(){
//        int intVal = 300;
//        BigUInt val = new BigUInt(intVal);
//        BigUInt expectedResult = new BigUInt(intVal / 2);
//
//        BigUInt result = val.divideThisByTwo();
//
//        Assert.assertTrue(expectedResult.equals(result));
//    }
    //https://en.wikipedia.org/wiki/RSA_(cryptosystem)
    @Test
    public void powMod_test() {
//        BigInteger b1 = BigInteger.valueOf(89358492031L);
//        BigInteger b2 = BigInteger.valueOf(3849208424820298502L);
//        BigInteger b3 = BigInteger.valueOf(5784390233111L);
//        BigInteger b4 = b1.modPow(b2,b3);
        long intBase = 89358492031L;
        long intExp = 3849208424820298502L;
        long intModulus = 5784390233111L;
        long intResult = 972957354483L;
        BigUInt base = new BigUInt(intBase);
        BigUInt exp = new BigUInt(intExp);
        BigUInt modulus = new BigUInt(intModulus);
        BigUInt expectedResult = new BigUInt(intResult);

        BigUInt result = base.powMod(exp, modulus);

        Assert.assertTrue(result.equals(expectedResult));
    }

    @Test
    public void divide_test() {
        long intNom = (long) Integer.MAX_VALUE * 189327123L + 7236481L;
        long intDenom = (long) Integer.MAX_VALUE + 99984L;
        long intResult = intNom / intDenom;
        long intMod = intNom % intDenom;
        BigUInt nom = new BigUInt(intNom);
        BigUInt denom = new BigUInt(intDenom);
        BigUInt expectedResult = new BigUInt(intResult);
        BigUInt expectedMod = new BigUInt(intMod);

        BigUInt[] result = nom.divide(denom);

        Assert.assertTrue(result[0].equals(expectedResult) && result[1].equals(expectedMod));
    }

    @Test
    public void modInv_test() {
//        BigInteger b1 = BigInteger.valueOf(89358492031L);
//        BigInteger b2 = BigInteger.valueOf(3849208424820298502L);
//        BigInteger b3 = b1.modInverse(b2);
        long intVal = 89358492031L;
        long intModulus = 3849208424820298502L;
        long intResult = 1156557425131638887L;
        BigUInt val = new BigUInt(intVal);
        BigUInt modulus = new BigUInt(intModulus);
        BigUInt expectedResult = new BigUInt(intResult);

        BigUInt result = val.modInv(modulus);

        Assert.assertTrue(result.equals(expectedResult));
    }

    @Test
    public void performance_test() {
        Random random = new Random();
        BigInteger oneJ = new BigInteger(3000, random);
        BigInteger twoJ = new BigInteger(3000, random);
//        System.out.println(oneJ.toString());
//        System.out.println(twoJ.toString());
        BigUInt one = new BigUInt(oneJ.toByteArray());
        BigUInt two = new BigUInt(twoJ.toByteArray());

        long start = System.nanoTime();
        one.add(two);
        two.add(one);
        System.out.printf("Add in %d nanoseconds\n", System.nanoTime() - start);

        start = System.nanoTime();
        one.multiply(two);
        two.multiply(one);
        System.out.printf("Multiply in %d nanoseconds\n\n", System.nanoTime() - start);


        start = System.nanoTime();
        oneJ.add(twoJ);
        twoJ.add(oneJ);
        System.out.printf("Add in %d nanoseconds\n", System.nanoTime() - start);

        start = System.nanoTime();
        oneJ.multiply(twoJ);
        twoJ.multiply(oneJ);
        System.out.printf("Multiply in %d nanoseconds\n\n", System.nanoTime() - start);

    }

    @Test
    public void prime_test() {
        for (int k = 0; k < 30; k++) {


            Random rnd = new SecureRandom();
            BigUInt val = BigUInt.probablePrime(20, rnd);

            for (int i = 2; ; i++) {
                BigUInt[] res = val.divide(new BigUInt(i));
                if (res[0].equals(BigUInt.one()) && res[1].equals(BigUInt.zero())) {
                    break;
                }
                Assert.assertFalse(res[1].equals(BigUInt.zero()));
            }
        }

    }
    @Test
    public void bytes_test() {
        Random rnd = new SecureRandom();
        BigInteger bigInt = BigInteger.probablePrime(64, rnd);

        byte[] initialArray = bigInt.toByteArray();
        BigUInt bigUInt = new BigUInt(initialArray);
        byte[] newArray = bigUInt.toByteArray();

        Assert.assertArrayEquals(initialArray, newArray);
    }

}
