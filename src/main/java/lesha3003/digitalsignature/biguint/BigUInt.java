package lesha3003.digitalsignature.biguint;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public final class BigUInt {
    private final static int BASE_BITS = 8;
    private final static int BASE_INT = (int) Math.pow(2, BASE_BITS);

    //    private boolean negative = false;
    private List<Integer> digits;

    private static BigUInt ZERO = new BigUInt(0, new ArrayList<Integer>(1));
    private static BigUInt ONE = new BigUInt(1, new ArrayList<Integer>(1));
    private static BigUInt TWO = new BigUInt(2, new ArrayList<Integer>(1));
    private static BigUInt BASE = new BigUInt(BASE_INT, new ArrayList<Integer>(1));

    public static BigUInt zero() {
        return ZERO;
    }

    public static BigUInt one() {
        return ONE;
    }

    public static BigUInt two() {
        return TWO;
    }

    public BigUInt(int value) {
        this(value, new ArrayList<Integer>(500));
    }

    public BigUInt copy() {
        BigUInt result = new BigUInt();
        for (Integer integer : this.digits) {
            result.digits.add(integer.intValue());
        }
        return result;
    }

    private BigUInt() {
        this(new ArrayList<Integer>(500));
    }

    private BigUInt(int value, List<Integer> list) {
        this(list);
        if (value == 0) {
            digits.add(0);
            return;
        }
        while (value > 0) {
            digits.add(value % BASE_INT);
            value /= BASE_INT;
        }
    }

    private BigUInt(List<Integer> list) {
        this.digits = list;
    }

    public BigUInt(byte[] bytes) {
        this(new ArrayList<Integer>(bytes.length));
        for (byte elem : bytes) {
            digits.add(elem & 0x000000FF);
        }
        if (digits.size() > 0) {
            while (digits.get(digits.size() - 1) == 0){
                digits.remove(digits.size() - 1);
            }
        } else {
            digits.add(0);
        }
    }

    public byte[] toByteArray() {
        byte[] result = new byte[this.digits.size()];
        for (int i = 0; i < this.digits.size(); i++) {
            result[i] = (byte) this.digits.get(i).intValue();
        }
        return result;
    }

    public boolean equals(BigUInt other) {
        if (this.digits.size() != other.digits.size()) {
            return false;
        }

        for (int i = this.digits.size() - 1; i >= 0; i--) {
            if (this.digits.get(i).intValue() != other.digits.get(i).intValue()) {
                return false;
            }
        }
        return true;
    }


    public boolean greaterThan(BigUInt other) {
        if (this.digits.size() != other.digits.size()) {
            return this.digits.size() > other.digits.size();
        }

        for (int i = this.digits.size() - 1; i >= 0; i--) {
            if (this.digits.get(i).intValue() != other.digits.get(i).intValue()) {
                return this.digits.get(i) > other.digits.get(i);
            }
        }
        return false;
    }

    public boolean greaterThanOrEquals(BigUInt other) {
        if (this.digits.size() != other.digits.size()) {
            return this.digits.size() > other.digits.size();
        }
        for (int i = this.digits.size() - 1; i >= 0; i--) {
            if (this.digits.get(i).intValue() != other.digits.get(i).intValue()) {
                return this.digits.get(i) > other.digits.get(i);
            }
        }
        return true;
    }

    public BigUInt add(BigUInt other) {
        int thisSize = this.digits.size();
        int otherSize = other.digits.size();
        BigUInt shorter, longer;
        if (thisSize < otherSize) {
            shorter = this;
            longer = other;
        } else {
            shorter = other;
            longer = this;
        }
        BigUInt result = new BigUInt();
        int carry = 0;
        for (int i = 0; i < shorter.digits.size(); i++) {
            int sum = shorter.digits.get(i) + longer.digits.get(i) + carry;
            result.digits.add(sum % BASE_INT);
            carry = sum >> BASE_BITS;
        }
        for (int i = shorter.digits.size(); i < longer.digits.size(); i++) {
            if (carry == 0) {
                break;
            }
            int sum = longer.digits.get(i) + carry;
            result.digits.add(sum % BASE_INT);
            carry = sum >> BASE_BITS;
        }
        if (carry > 0) {
            result.digits.add(carry);
        }
        return result;
    }

    public BigUInt subtract(BigUInt other) {
        return this.copy().subtractFromThis(other);
    }

    private BigUInt subtractFromThis(BigUInt other) {
        if (other.greaterThan(this)) {
            throw new IllegalArgumentException();
        }
        int carry = 0;
        for (int i = 0; i < other.digits.size(); i++) {
            int diff = this.digits.get(i) - other.digits.get(i) + carry;
            if (diff < 0) {
                this.digits.set(i, diff + BASE_INT);
                carry = -1;
            } else {
                this.digits.set(i, diff);
                carry = 0;
            }
        }
        for (int i = other.digits.size(); i < this.digits.size(); i++) {
            if (carry == 0) {
                break;
            }
            int diff = this.digits.get(i) + carry;
            if (diff < 0) {
                this.digits.set(i, diff + BASE_INT);
                carry = -1;
            } else {
                this.digits.set(i, diff);
                carry = 0;
            }
        }
        for (int i = this.digits.size() - 1; i > 0; i--) {
            if (this.digits.get(i) == 0) {
                this.digits.remove(i);
            } else {
                break;
            }
        }
        return this;
    }

    private BigUInt addToThis(BigUInt other) {
        int thisSize = this.digits.size();
        int otherSize = other.digits.size();
        int carry = 0;
        if (thisSize < otherSize) {
            for (int i = 0; i < thisSize; i++) {
                int sum = this.digits.get(i) + other.digits.get(i) + carry;
                this.digits.set(i, sum % BASE_INT);
                carry = sum / BASE_INT;
            }
            for (int i = thisSize; i < otherSize; i++) {
                int sum = other.digits.get(i) + carry;
                this.digits.add(sum % BASE_INT);
                carry = sum / BASE_INT;
            }
            if (carry > 0) {
                this.digits.add(carry);
            }
        } else if (thisSize == otherSize) {
            for (int i = 0; i < thisSize; i++) {
                int sum = this.digits.get(i) + other.digits.get(i) + carry;
                this.digits.set(i, sum % BASE_INT);
                carry = sum / BASE_INT;
            }
            if (carry > 0) {
                this.digits.add(carry);
            }
        } else {
            for (int i = 0; i < otherSize; i++) {
                int sum = this.digits.get(i) + other.digits.get(i) + carry;
                this.digits.set(i, sum % BASE_INT);
                carry = sum / BASE_INT;
            }
            for (int i = otherSize; i < thisSize; i++) {
                if (carry == 0) {
                    break;
                }
                int sum = this.digits.get(i) + carry;
                this.digits.set(i, sum % BASE_INT);
                carry = sum / BASE_INT;
            }
            if (carry > 0) {
                this.digits.add(carry);
            }
        }
        return this;
    }

    public BigUInt multiply(BigUInt other) {
        BigUInt result = new BigUInt(0);
        BigUInt temp = new BigUInt();
        for (int i = 0; i < this.digits.size(); i++) {
            temp.digits.clear();
            for (int k = 0; k < i; k++) {
                temp.digits.add(0);
            }
            int carry = 0;
            for (int j = 0; j < other.digits.size(); j++) {
                int product = this.digits.get(i) * other.digits.get(j) + carry;
                temp.digits.add(product % BASE_INT);
                carry = product / BASE_INT;
            }
            if (carry > 0) {
                temp.digits.add(carry);
            }
            result.addToThis(temp);
        }
        return result;
    }

    public BigUInt mod(BigUInt other) {
        BigUInt result = this.copy();
        while (result.greaterThanOrEquals(other)) {
            BigUInt temp = other.copy();
            while (result.greaterThan(temp.multiply(TWO).addToThis(other))) {
                temp = temp.multiply(TWO);
            }
            result.subtractFromThis(temp);
        }
        return result;
    }

    /**
     * @param other
     * @return array of result of division and remainder
     */
    public BigUInt[] divide(BigUInt other) {
        if (other.greaterThan(this)) {
            return new BigUInt[]{ZERO.copy(), this.copy()};
        }
        BigUInt carry = ZERO.copy();
        int currentIndex = this.digits.size() - 1;
        LinkedList<Integer> resultList = new LinkedList<>();
        BigUInt temp = new BigUInt();
        for (int i = this.digits.size() - other.digits.size() + 1; i < this.digits.size(); i++) {
            temp.digits.add(this.digits.get(i).intValue());
        }
        if (temp.digits.isEmpty()) {
            temp.digits.add(0);
        }
        for (int i = 0; i <= this.digits.size() - other.digits.size(); i++) {
            temp.multiplyThisByBase().addToThis(new BigUInt(this.digits.get(this.digits.size() - other.digits.size() - i), new ArrayList<Integer>(1)));
            int q = 0;
            while (temp.greaterThanOrEquals(other)) {
                temp.subtractFromThis(other);
                q++;
            }
            resultList.addFirst(q);
        }
        while (resultList.peekLast() == 0) {
            resultList.removeLast();
        }
        return new BigUInt[]{new BigUInt(new ArrayList<>(resultList)), temp};
    }

    private BigUInt multiplyThisByBase() {
        if (this.equals(ZERO)) {
            return this;
        }
        ArrayList<Integer> newDigits = new ArrayList<>(this.digits.size() + 1);
        newDigits.add(0);
        newDigits.addAll(this.digits);
        this.digits = newDigits;
        return this;
    }

    private BigUInt divideThisByTwo() {
        int valToAdd = BASE_INT >> 1;
        boolean addValue = false;
        for (int i = this.digits.size() - 1; i >= 0; i--) {
            int current = this.digits.get(i);
            this.digits.set(i, (current >> 1) + (addValue ? valToAdd : 0));
            addValue = current % 2 > 0;
        }
        if (this.digits.get(this.digits.size() - 1) == 0) {
            this.digits.remove(this.digits.size() - 1);
        }
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
            if (exponent.digits.get(0) % 2 == 1) {
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
            ti = tii.addToThis(ti.multiply(res[0]));
            tii = temp;
        }
        if (i % 2 == 0) {
            return modulus.subtract(ti);
        } else {
            return ti;
        }
    }

    public static BigUInt probablePrime(int bitLength, Random rnd) {
        BigUInt result = new BigUInt();
        byte[] bigIntegerBytes = BigInteger.probablePrime(bitLength, rnd).toByteArray();
        for (int i = bigIntegerBytes.length - 1; i >= 0; i--) {
            result.digits.add(bigIntegerBytes[i] & 0xFF);
        }
        if (result.digits.get(result.digits.size() - 1) == 0) {
            result.digits.remove(result.digits.size() - 1);
        }
        return result;
    }
}
