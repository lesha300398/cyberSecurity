package lesha3003.digitalsignature.biguint;


import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

public final class BigUInt {
    private final static int BASE_BITS = 32;
    private final static int BASE_INT = (int) Math.pow(2, BASE_BITS);

    //    private boolean negative = false;
    private int[] digits;

    private static BigUInt ZERO = new BigUInt(0);
    private static BigUInt ONE = new BigUInt(1);
    private static BigUInt TWO = new BigUInt(2);
//    private static BigUInt BASE = new BigUInt(BASE_INT, new ArrayList<Integer>(1));

    public static BigUInt zero() {
        return ZERO;
    }

    public static BigUInt one() {
        return ONE;
    }

    public static BigUInt two() {
        return TWO;
    }

    private static int[] trimZeroes(int[] digits) {
        if (digits[digits.length - 1] != 0 || digits.length == 1) {
            return digits;
        }
        int newSize = digits.length;

        while (newSize > 1 && digits[newSize - 1] == 0) {
            newSize--;
        }
        int[] temp = new int[newSize];
        System.arraycopy(digits, 0, temp, 0, newSize);
        return temp;
    }


    public BigUInt(int value) {
        this.digits = new int[]{value};
    }

    public BigUInt(long value) {
        if (value >>> 32 != 0) {
            this.digits = new int[]{(int) value, (int) (value >>> 32)};
        } else {
            this.digits = new int[]{(int) value};
        }
    }

    public BigUInt copy() {
        int[] newDigits = new int[this.digits.length];
        System.arraycopy(this.digits, 0, newDigits, 0, newDigits.length);
        return new BigUInt(newDigits);
    }

//    private BigUInt() {
//        this(new ArrayList<Integer>(150));
//    }

//    private BigUInt(int value, List<Integer> list) {
//        this(list);
//        digits.add(value);
////        if (value == 0) {
////            digits.add(0);
////            return;
////        }
////        while (value > 0) {
////            digits.add(value % BASE_INT);
////            value /= BASE_INT;
////        }
//    }

    private BigUInt(int[] digits) {
        this.digits = digits;
    }

    public BigUInt(byte[] bytes) {
        this.digits = new int[bytes.length / 4 + (bytes.length % 4 == 0 ? 0 : 1)];
        for (int i = bytes.length - 1; i >= 3; i = i - 4) {
            this.digits[(bytes.length - i) / 4] = (bytes[i] & 0xFF) | ((bytes[i - 1] & 0xFF) << 8) | ((bytes[i - 2] & 0xFF) << 16) | ((bytes[i - 3] & 0xFF) << 24);
        }
        switch (bytes.length % 4) {
            case 3:
                this.digits[this.digits.length - 1] = (bytes[2] & 0xFF) | ((bytes[1] & 0xFF) << 8) | ((bytes[0] & 0xFF) << 16);
                break;
            case 2:
                this.digits[this.digits.length - 1] = (bytes[1] & 0xFF) | ((bytes[0] & 0xFF) << 8);
                break;
            case 1:
                this.digits[this.digits.length - 1] = (bytes[0] & 0xFF);
                break;
            default:
                break;
        }
        this.digits = trimZeroes(this.digits);
    }

    public byte[] toByteArray() {
        byte[] temp = new byte[this.digits.length * 4];
        for (int i = 0; i < this.digits.length; i++) {
            System.arraycopy(ByteBuffer.allocate(4).putInt(this.digits[this.digits.length - i - 1]).array(), 0, temp, 4 * i, 4);
        }

        int size = temp.length;
        while (temp[temp.length - size] == 0 && size > 1) {
            size--;
        }
        if (size == temp.length) {
            return temp;
        } else {
            return Arrays.copyOfRange(temp, temp.length - size, temp.length);
        }
    }
    public byte[] toByteArrayStartsWithZero() {
        byte[] temp = new byte[this.digits.length * 4];
        for (int i = 0; i < this.digits.length; i++) {
            System.arraycopy(ByteBuffer.allocate(4).putInt(this.digits[this.digits.length - i - 1]).array(), 0, temp, 4 * i, 4);
        }

        int size = temp.length;
        while (temp[temp.length - size] == 0 && size > 1) {
            size--;
        }
        if (temp[temp.length - size] >>> 7 == 0) {
            return Arrays.copyOfRange(temp, temp.length - size, temp.length);
        } else {
            byte[] result = new byte[size + 1];
            result[0] = 0;
            System.arraycopy(temp, temp.length - size, result, result.length - size, size);
            return result;
        }
    }

    public boolean equals(BigUInt other) {
        if (this.digits.length != other.digits.length) {
            return false;
        }

        for (int i = this.digits.length - 1; i >= 0; i--) {
            if (this.digits[i] != other.digits[i]) {
                return false;
            }
        }
        return true;
    }


    public boolean greaterThan(BigUInt other) {
        if (this.digits.length != other.digits.length) {
            return this.digits.length > other.digits.length;
        }

        for (int i = this.digits.length - 1; i >= 0; i--) {
            if (this.digits[i] != other.digits[i]) {
                return ((long) this.digits[i] & 0xFFFFFFFFL) > ((long) other.digits[i] & 0xFFFFFFFFL);
            }
        }
        return false;
    }

    public boolean greaterThanOrEquals(BigUInt other) {
        if (this.digits.length != other.digits.length) {
            return this.digits.length > other.digits.length;
        }
        for (int i = this.digits.length - 1; i >= 0; i--) {
            if (this.digits[i] != other.digits[i]) {
                return ((long) this.digits[i] & 0xFFFFFFFFL) > ((long) other.digits[i] & 0xFFFFFFFFL);
            }
        }
        return true;
    }

    public BigUInt add(BigUInt other) {
        return new BigUInt(add(this.digits, other.digits));
    }

    private int[] add(int[] first, int[] second) {

        int[] shorter, longer;
        if (first.length < second.length) {
            shorter = first;
            longer = second;
        } else {
            shorter = second;
            longer = first;
        }
        int[] result = new int[longer.length];
        long sum = 0;
        int i = 0;
        for (; i < shorter.length; i++) {
            result[i] = (int) (sum = ((long) shorter[i] & 0xFFFFFFFFL) + ((long) longer[i] & 0xFFFFFFFFL) + (sum >>> 32));
        }
        boolean bool;
        for (bool = sum >>> 32 != 0; bool && i < longer.length; i++) {
            bool = (result[i] = longer[i] + 1) == 0;
        }
        for (; i < longer.length; i++) {
            result[i] = longer[i];
        }
        if (bool) {
            int[] temp = new int[result.length + 1];
            System.arraycopy(result, 0, temp, 0, result.length);
            temp[result.length] = 1;
            return temp;
        }
        return result;
    }

    public BigUInt subtract(BigUInt other) {
        if (other.greaterThan(this)) {
            throw new IllegalArgumentException();
        }
        return subtractTrusted(other);
    }

    private BigUInt subtractTrusted(BigUInt other) {
        return new BigUInt(subtract(this.digits, other.digits));
    }

    private static int[] subtract(int[] from, int[] other) {
        long diff = 0;
        int[] result = new int[from.length];
        int i = 0;
        for (; i < other.length; i++) {
            diff = ((long) from[i] & 0xFFFFFFFFL) - ((long) other[i] & 0xFFFFFFFFL) + (diff >> 32);
            result[i] = (int) diff;
        }
        boolean bool;
        for (bool = diff >> 32 != 0; i < from.length && bool; i++) {
            bool = (result[i] = (int) ((long) from[i] & 0xFFFFFFFFL) - 1) == -1;
        }
        for (; i < from.length; i++) {
            result[i] = from[i];
        }
        return trimZeroes(result);
    }

//    private BigUInt subtractFromThis(BigUInt other) {
//        this.digits = subtractTrusted(this.digits, other.digits);
//        return this;
//    }

//    private BigUInt addToThis(BigUInt other) {
//        int thisSize = this.digits.size();
//        int otherSize = other.digits.size();
//        long carry = 0;
//        if (thisSize < otherSize) {
//            for (int i = 0; i < thisSize; i++) {
//                long sum = ((long)this.digits.get(i) & 0xFFFFFFFFL) + ((long)other.digits.get(i) & 0xFFFFFFFFL) + carry;
//                this.digits.set(i, (int)(sum & 0xFFFFFFFFL));
//                carry = sum >>> 32;
//            }
//            for (int i = thisSize; i < otherSize; i++) {
//                long sum = other.digits.get(i) + carry;
//                this.digits.add((int)(sum & 0xFFFFFFFFL));
//                carry = sum >>> 32;
//            }
//            if (carry > 0) {
//                this.digits.add((int)(carry & 0xFFFFFFFFL));
//            }
//        } else if (thisSize == otherSize) {
//            for (int i = 0; i < thisSize; i++) {
//                long sum = ((long)this.digits.get(i) & 0xFFFFFFFFL) + ((long)other.digits.get(i) & 0xFFFFFFFFL) + carry;
//                this.digits.set(i, (int)(sum & 0xFFFFFFFFL));
//                carry = sum >>> 32;
//            }
//            if (carry > 0) {
//                this.digits.add((int)(carry & 0xFFFFFFFFL));
//            }
//        } else {
//            for (int i = 0; i < otherSize; i++) {
//                long sum = ((long)this.digits.get(i) & 0xFFFFFFFFL) + ((long)other.digits.get(i) & 0xFFFFFFFFL) + carry;
//                this.digits.set(i, (int)(sum & 0xFFFFFFFFL));
//                carry = sum >>> 32;
//            }
//            for (int i = otherSize; i < thisSize; i++) {
//                long sum = this.digits.get(i) + carry;
//                this.digits.set(i, (int)(sum & 0xFFFFFFFFL));
//                carry = sum >>> 32;
//            }
//            if (carry > 0) {
//                this.digits.add((int)(carry & 0xFFFFFFFFL));
//            }
//        }
//        return this;
//    }

    public BigUInt multiply(BigUInt other) {
        if (this.digits.length >= 80 && other.digits.length >= 80) {
            return multiplyKaratsuba(this, other);
        } else {
            return new BigUInt(multiplyArrays(this.digits, other.digits));
        }
    }

    private static int[] multiplyArrays(int[] first, int[] second) {
        int[] result = new int[first.length + second.length];
        long prod = 0;
        int i, j;
        for (i = 0; i < first.length; i++) {
            result[i] = (int) (prod = ((long) first[i] & 0xFFFFFFFFL) * ((long) second[0] & 0xFFFFFFFFL) + (prod >>> 32));
        }

        result[first.length] = (int) (prod >>> 32);

        int resultIndex;
        for (i = 1; i < second.length; i++) {
            prod = 0;
            resultIndex = i;
            for (j = 0; j < first.length; j++) {
                result[resultIndex] = (int) (prod = ((long) result[resultIndex] & 0xFFFFFFFFL) + ((long) first[j] & 0xFFFFFFFFL) * ((long) second[i] & 0xFFFFFFFFL) + (prod >>> 32));
                resultIndex++;
            }

            result[resultIndex] = (int) (prod >>> 32);
        }
        return trimZeroes(result);
    }

    private static BigUInt multiplyKaratsuba(BigUInt first, BigUInt second) {
        int m = (Math.max(first.digits.length, second.digits.length) + 1) / 2;
        BigUInt first0 = first.getLower(m);
        BigUInt first1 = first.getUpper(m);
        BigUInt second0 = second.getLower(m);
        BigUInt second1 = second.getUpper(m);
        BigUInt z2 = first1.multiply(second1);
        BigUInt z0 = first0.multiply(second0);
        BigUInt z1 = (first1.add(first0)).multiply(second1.add(second0)).subtractTrusted(z2).subtractTrusted(z0);
        return (z2.shiftLeftDigits(2 * m)).add(z1.shiftLeftDigits(m)).add(z0);

    }

    private BigUInt getUpper(int offset) {
        if (this.digits.length < offset) {
            return ZERO.copy();
        }
        int[] resultArray = new int[this.digits.length - offset];
        System.arraycopy(this.digits, offset, resultArray, 0, resultArray.length);
        return new BigUInt(trimZeroes(resultArray));
    }

    private BigUInt getLower(int offset) {
        int size = Math.min(offset, this.digits.length);

        int[] resultArray = new int[size];
        System.arraycopy(this.digits, 0, resultArray, 0, size);
        return new BigUInt(trimZeroes(resultArray));
    }

    private BigUInt shiftLeftDigits(int offset) {
        int[] resultArray = new int[digits.length + offset];
        for (int i = 0; i < offset; i++) {
            resultArray[i] = 0;
        }
        System.arraycopy(this.digits, 0, resultArray, offset, this.digits.length);
        return new BigUInt(resultArray);
    }

    public BigUInt mod(BigUInt other) {
        return this.divide(other)[1];
//        BigUInt result = this;
//        while (result.greaterThanOrEquals(other)) {
//            BigUInt temp = other;
//            while (result.greaterThan(temp.multiply(TWO).add(other))) {
//                temp = temp.multiply(TWO);
//            }
//            result = result.subtractTrusted(temp);
//        }
//        return result;
    }

    /**
     * @param other
     * @return array of result of division and remainder
     */
    public BigUInt[] divide(BigUInt other) {
        if (other.greaterThan(this)) {
            return new BigUInt[]{ZERO.copy(), this.copy()};
        }
//        BigUInt carry = ZERO.copy();
//        int currentIndex = this.digits.size() - 1;
        int[] temp = this.copy().digits;
        int[] resultArray = new int[temp.length - other.digits.length + 1];
        for (int start = temp.length - other.digits.length; start >= 0; start--) {
            int q = 0;
            while (arrayGreaterOrEqualWithOffset(temp, start, other.digits)) {
                long coef = 1;
                BigUInt tempInt1 = other;
                BigUInt tempInt2 = other.multiply(TWO);
                while (arrayGreaterOrEqualWithOffset(temp, start, tempInt2.digits)) {
                    coef *= 2;
                    tempInt1 = tempInt2;
                    tempInt2 = tempInt2.multiply(TWO);
                }
                subtractFromArrayWithOffset(temp, start, tempInt1.digits);
                q += coef;
            }
            resultArray[start] = q;
        }
        return new BigUInt[]{new BigUInt(trimZeroes(resultArray)), new BigUInt(trimZeroes(temp))};
    }
    private BigUInt shiftLeft1Bit() {
        boolean newDigit = (this.digits[this.digits.length - 1] >>> 31) != 0;
        int[] result = new int[this.digits.length + (newDigit ? 1 : 0)];
        for (int i = this.digits.length - 1; i >= 1; i--) {
            result[i] = (this.digits[i] << 1) ^ (this.digits[i-1] >>> 31);
        }
        result[0] = this.digits[0] << 1;
        if (newDigit) {
            result[result.length - 1] = 1;
        }
        return new BigUInt(result);
    }

    private static boolean arrayGreaterOrEqualWithOffset(int[] first, int start, int[] other) {
        int actualLen = first.length - start;
        while (first[start + actualLen - 1] == 0 && actualLen > 1) {
            actualLen--;
        }
        if (actualLen != other.length) {
            return actualLen > other.length;
        }
        for (int i = actualLen - 1; i >= 0; i--) {
            if (first[i + start] != other[i]) {
                return ((long) first[i + start] & 0xFFFFFFFFL) > ((long) other[i] & 0xFFFFFFFFL);
            }
        }
        return true;
    }

    private static void subtractFromArrayWithOffset(int[] from, int start, int[] other) {
        if (!arrayGreaterOrEqualWithOffset(from, start, other)) {
            throw new IllegalArgumentException();
        }
        int actualLen = from.length - start;
        while (from[start + actualLen - 1] == 0 && actualLen > 1) {
            actualLen--;
        }
        long carry = 0;
        for (int i = start; i < start + other.length; i++) {
            long diff = ((long) from[i] & 0xFFFFFFFFL) - ((long) other[i - start] & 0xFFFFFFFFL) + carry;
            if (diff < 0) {
                from[i] = (int) ((diff + 0x0100000000L) & 0xFFFFFFFFL);
                carry = -1;
            } else {
                from[i] = (int) (diff & 0xFFFFFFFFL);
                carry = 0;
            }
        }
        for (int i = other.length + start; i < start + actualLen; i++) {
            if (carry == 0) {
                break;
            }
            long diff = ((long) from[i] & 0xFFFFFFFFL) + carry;
            if (diff < 0) {
                from[i] = (int) ((diff + 0x0100000000L) & 0xFFFFFFFFL);
                carry = -1;
            } else {
                from[i] = (int) (diff & 0xFFFFFFFFL);
                carry = 0;
            }
        }
    }

//    private BigUInt multiplyThisByBase() {
//        if (this.equals(ZERO)) {
//            return this;
//        }
//        ArrayList<Integer> newDigits = new ArrayList<>(this.digits.size() + 1);
//        newDigits.add(0);
//        newDigits.addAll(this.digits);
//        this.digits = newDigits;
//        return this;
//    }

    private BigUInt divideThisByTwo() {
        for (int i = 0; i < this.digits.length - 1; i++) {
            this.digits[i] = (this.digits[i] >>> 1) ^ (this.digits[i + 1] << 31);
        }
        this.digits[this.digits.length - 1] >>>= 1;
        this.digits = trimZeroes(this.digits);
        return this;
    }

    public BigUInt powMod(BigUInt exp, BigUInt modulus) {
        if (modulus.equals(ONE)) {
            return new BigUInt(0);
        }
        BigUInt base = this.copy().mod(modulus);
        BigUInt exponent = exp.copy();
        BigUInt result = ONE.copy();
        while (exponent.greaterThan(ZERO)) {
            if ((exponent.digits[0] << 31) != 0) {
                result = result.multiply(base).mod(modulus);
            }
            exponent.divideThisByTwo();
            base = base.multiply(base).mod(modulus);
        }
        return result;
    }

    public BigUInt modInv(BigUInt modulus) {
        BigUInt rii, ri;
//        BigUInt sii = ONE.copy(), si = ZERO.copy();
        BigUInt tii = ZERO.copy(), ti = ONE.copy();

        rii = modulus;
        ri = this.mod(modulus);

        BigUInt temp;
        int i = 1;
        for (; ; i++) {
            BigUInt[] res = rii.divide(ri); // {q, new ri}
            if (res[1].equals(ZERO)) {
                break;
            }
            rii = ri;
            ri = res[1];
//            temp = si.copy();
//            si = sii.addToThis(si.multiply(res[0]));
//            sii = temp;
            temp = ti.copy();
            ti = tii.add(ti.multiply(res[0]));
            tii = temp;
        }
        if (i % 2 == 0) {
            return modulus.subtractTrusted(ti);
        } else {
            return ti;
        }
    }

    public static BigUInt probablePrime(int bitLength, Random rnd) {
        BigInteger bigInteger = BigInteger.probablePrime(bitLength, rnd);
//        byte[] bigIntegerBytes = BigInteger.probablePrime(bitLength, rnd).toByteArray();

        return new BigUInt(bigInteger.toByteArray());
    }
}
