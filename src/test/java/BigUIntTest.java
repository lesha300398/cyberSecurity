import org.junit.Assert;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import lesha3003.digitalsignature.biguint.BigUInt;

public final class BigUIntTest {
    @Test
    public void equalsTrue_test(){
        int intVal = 23983452;
        BigUInt val1 = new BigUInt(intVal);
        BigUInt val2 = new BigUInt(intVal);

        Assert.assertTrue(val1.equals(val2) && val2.equals(val1));
    }
    @Test
    public void equalsFalse_test(){
        int intVal1 = 23983452;
        int intVal2 = 574832913;
        BigUInt val1 = new BigUInt(intVal1);
        BigUInt val2 = new BigUInt(intVal2);

        Assert.assertTrue(!val1.equals(val2) && !val2.equals(val1));
    }
    @Test
    public void greaterThan_equal_test(){
        int intVal = 23983452;
        BigUInt val1 = new BigUInt(intVal);
        BigUInt val2 = new BigUInt(intVal);

        Assert.assertTrue(!val1.greaterThan(val2) && !val2.greaterThan(val1));
    }
    @Test
    public void greaterThan_greater_test(){
        int intVal1 = 23983452;
        int intVal2 = 574832913;
        BigUInt val1 = new BigUInt(intVal1);
        BigUInt val2 = new BigUInt(intVal2);

        Assert.assertTrue(!val1.greaterThan(val2) && val2.greaterThan(val1));
    }
    @Test
    public void greaterThanOrEqual_equal_test(){
        int intVal = 23983452;
        BigUInt val1 = new BigUInt(intVal);
        BigUInt val2 = new BigUInt(intVal);

        Assert.assertTrue(val1.greaterThanOrEquals(val2) && val2.greaterThanOrEquals(val1));
    }
    @Test
    public void greaterThanOrEqual_greater_test(){
        int intVal1 = 23983452;
        int intVal2 = 574832913;
        BigUInt val1 = new BigUInt(intVal1);
        BigUInt val2 = new BigUInt(intVal2);

        Assert.assertTrue(!val1.greaterThanOrEquals(val2) && val2.greaterThanOrEquals(val1));
    }

    @Test
    public void add_test(){
        int intVal1 = 23983452;
        int intVal2 = 574832913;
        BigUInt val1 = new BigUInt(intVal1);
        BigUInt val2 = new BigUInt(intVal2);
        BigUInt expectedSum = new BigUInt(intVal1 + intVal2);

        BigUInt sum1 = val1.add(val2);
        BigUInt sum2 = val2.add(val1);

        Assert.assertTrue(expectedSum.equals(sum1) && expectedSum.equals(sum2));
    }
    @Test
    public void subtract_test(){
        int intVal1 = 23983452;
        int intVal2 = 574832913;
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
    public void multiply_test(){
        int intVal1 = 1000;
        int intVal2 = 1890;
        BigUInt val1 = new BigUInt(intVal1);
        BigUInt val2 = new BigUInt(intVal2);
        BigUInt expectedProduct = new BigUInt(intVal1 * intVal2);

        BigUInt product1 = val1.multiply(val2);
        BigUInt product2 = val2.multiply(val1);

        Assert.assertTrue(expectedProduct.equals(product1) && expectedProduct.equals(product2));
    }
    @Test
    public void mod_test(){
        int intModulus = 100098;
        int intVal = 1890778;
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
    public void powMod_test(){
        int intBase = 2790;
        int intExp = 413;
        int intModulus = 3233;
        int intResult = 65;
        BigUInt base = new BigUInt(intBase);
        BigUInt exp = new BigUInt(intExp);
        BigUInt modulus = new BigUInt(intModulus);
        BigUInt expectedResult = new BigUInt(intResult);

        BigUInt result = base.powMod(exp, modulus);

        Assert.assertTrue(result.equals(expectedResult));
    }
    @Test
    public void divide_test(){
        int intNom = 3008493;
        int intDenom = 99984;
        int intResult = intNom/intDenom;
        int intMod = intNom%intDenom;
        BigUInt nom = new BigUInt(intNom);
        BigUInt denom = new BigUInt(intDenom);
        BigUInt expectedResult = new BigUInt(intResult);
        BigUInt expectedMod = new BigUInt(intMod);

        BigUInt[] result = nom.divide(denom);

        Assert.assertTrue(result[0].equals(expectedResult) && result[1].equals(expectedMod));
    }
    @Test
    public void modInv_test(){
        int intVal = 17;
        int intModulus = 780;
        int intResult = 413;
        BigUInt val = new BigUInt(intVal);
        BigUInt modulus = new BigUInt(intModulus);
        BigUInt expectedResult = new BigUInt(intResult);

        BigUInt result = val.modInv(modulus);

        Assert.assertTrue(result.equals(expectedResult));
    }
    @Test
    public void performance_test() {
        Random random = new Random();
        BigUInt one = BigUInt.probablePrime(3000, random);
        BigUInt two = BigUInt.probablePrime(3000, random);

        long start = System.nanoTime();
        one.add(two);
        two.add(one);
        System.out.printf("Add in %d milliseconds\n", System.nanoTime() - start);

        start = System.nanoTime();
        one.multiply(two);
        two.multiply(one);
        System.out.printf("Multiply in %d milliseconds\n\n", System.nanoTime() - start);


        BigInteger oneB = BigInteger.probablePrime(3000, random);
        BigInteger twoB = BigInteger.probablePrime(3000, random);

        start = System.nanoTime();
        oneB.add(twoB);
        twoB.add(oneB);
        System.out.printf("Add in %d milliseconds\n", System.nanoTime() - start);

        start = System.nanoTime();
        oneB.multiply(twoB);
        twoB.multiply(oneB);
        System.out.printf("Multiply in %d milliseconds\n\n", System.nanoTime() - start);

    }
//    @Test
//    public void prime_test(){
//        for (int k = 0; k < 30; k++) {
//
//
//            Random rnd = new SecureRandom();
//            int intVal = 17;
//            int intModulus = 780;
//            int intResult = 413;
//            BigUInt val = BigUInt.probablePrime(20, rnd);
//
//            for (int i = 2; ; i++) {
//                BigUInt[] res = val.divide(new BigUInt(i));
//                if (res[0].equals(BigUInt.one()) && res[1].equals(BigUInt.zero())) {
//                    break;
//                }
//                Assert.assertFalse(res[1].equals(BigUInt.zero()));
//            }
//        }
//
//    }

}
