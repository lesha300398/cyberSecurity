package lesha3003.ellipticcurves;

import java.nio.ByteBuffer;
import java.util.Arrays;

public final class Polynomial {
    private int[] digits;

    public static final Polynomial ZERO = new Polynomial(new int[] {0});
    public static final Polynomial ONE = new Polynomial(new int[] {1});

    private static final Polynomial TO_REPLACE = ONE.shiftLeft(191); // x^191

    private static final Polynomial REPLACE_WITH = ONE.add(ONE.shiftLeft(9)); // x^9 + 1

    public static final Polynomial MODULUS = ONE.shiftLeft(191).add(ONE.add(ONE.shiftLeft(9))); // x^191 + x^9 + 1

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

    public Polynomial copy() {
        return new Polynomial(Arrays.copyOf(this.digits, this.digits.length));
    }

    private Polynomial(int[] digits) {
        this.digits = digits;
    }

    public Polynomial(byte[] bytes) {
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

    public boolean equals(Polynomial other) {
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
    public boolean greaterThan(Polynomial other) {
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

    public boolean greaterThanOrEquals(Polynomial other) {
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

    public Polynomial add(Polynomial other) {
        return new Polynomial(xor(this.digits, other.digits));
    }

    private static int[] xor(int[] first, int[] second) {
        int[] shorter, longer;
        if (first.length < second.length) {
            shorter = first;
            longer = second;
        } else {
            shorter = second;
            longer = first;
        }
        int[] result = new int[longer.length];
        int i = 0;
        for(;i<shorter.length;i++) {
            result[i] = longer[i] ^ shorter[i];
        }
        for(; i< longer.length; i++) {
            result[i] = longer[i];
        }
        return trimZeroes(result);
    }


    public Polynomial multiply(Polynomial other) {
        Polynomial result = this.multiplyNoMod(other);
        while (result.greaterThanOrEquals(TO_REPLACE)) {
            result = result.getLower(191).add(result.getUpper(191).multiplyNoMod(REPLACE_WITH));
        }
        return result;
    }
    public Polynomial square() {
        Polynomial result = ZERO;
        for (int i = 0; i < this.digits.length; i++) {
            for (int j = 0; j < 32; j++) {
                if ((this.digits[i] & (1 << j)) != 0) {
                    result = result.add(ONE.shiftLeft((32*i + j)*2));
                }
            }
        }
        while (result.greaterThanOrEquals(TO_REPLACE)) {
            result = result.getLower(191).add(result.getUpper(191).multiplyNoMod(REPLACE_WITH));
        }
        return result;
    }

    private Polynomial multiplyNoMod(Polynomial other) {
        Polynomial result = ZERO;
        for (int i = 0; i < other.digits.length; i++) {
            for (int j = 0; j < 32; j++) {
                if ((other.digits[i] & (1 << j)) != 0) {
                    result = result.add(this.shiftLeft(32*i + j));
                }
            }
        }
        return result;
    }
    private Polynomial getLower(int offset) {
        int offsetDigits = offset / 32;
        int offsetBits = offset % 32;
        if (offsetDigits >= this.digits.length) {
            return this;
        }

        if (offsetBits == 0) {
            return new Polynomial(trimZeroes(Arrays.copyOfRange(this.digits, 0, offsetDigits)));
        } else {
            int[] result = new int[offsetDigits + 1];
            System.arraycopy(this.digits, 0, result, 0,result.length - 1);
            int shift = (32 - offsetBits);
            result[result.length - 1] = this.digits[offsetDigits] << shift >>> shift;
            return new Polynomial(trimZeroes(result));
        }
    }
    private Polynomial getUpper(int offset) {
        return this.shiftRight(offset);
    }

    public Polynomial shiftLeft(int offset) {
        int offsetDigits = offset / 32;
        int offsetBits = offset % 32;
        int nextOffsetBits = 32 - offsetBits;

        if (offsetBits == 0) {
            int[] result = new int[this.digits.length + offsetDigits];
            System.arraycopy(this.digits, 0, result, offsetDigits, this.digits.length);
            return new Polynomial(trimZeroes(result));
        } else {
            int[] result = new int[this.digits.length + offsetDigits + 1];
            result[offsetDigits] = this.digits[0] << offsetBits;

            for (int i = 1; i < this.digits.length; i++) {
                result[offsetDigits + i] = (this.digits[i] << offsetBits) | (this.digits[i-1] >>> nextOffsetBits);
            }

            result[result.length - 1] = this.digits[this.digits.length - 1] >>> nextOffsetBits;
            return new Polynomial(trimZeroes(result));
        }
    }

    public Polynomial shiftRight(int offset) {
        int offsetDigits = offset / 32;
        int offsetBits = offset % 32;
        int nextOffsetBits = 32 - offsetBits;

        if (offsetDigits >= this.digits.length) {
            return ZERO.copy();
        }

        if (offsetBits == 0) {
            int[] result = new int[this.digits.length - offsetDigits];
            System.arraycopy(this.digits, offsetDigits, result, 0, result.length);
            return new Polynomial(trimZeroes(result));
        } else {
            int[] result = new int[this.digits.length - offsetDigits];

            for (int i = 0; i < result.length - 1; i++) {
                result[i] = (this.digits[i + offsetDigits] >>> offsetBits) | (this.digits[i+offsetDigits+1] << nextOffsetBits);
            }

            result[result.length - 1] = this.digits[this.digits.length - 1] >>> offsetBits;
            return new Polynomial(trimZeroes(result));
        }
    }

    private Polynomial[] dividePol(Polynomial other) {
        if (other.greaterThan(this)) {
            return new Polynomial[]{ZERO, this};
        }
//        BigUInt carry = ZERO.copy();
//        int currentIndex = this.digits.size() - 1;
        Polynomial temp = this.copy();
        Polynomial result = new Polynomial(new int[]{ 0 });
        int otherIdx = other.firstNonZeroBit();
        while (temp.greaterThanOrEquals(other)) {
            int tempIdx = temp.firstNonZeroBit();
            temp = temp.add(other.shiftLeft(tempIdx - otherIdx));
            result = result.add(ONE.shiftLeft(tempIdx - otherIdx));
        }
        return new Polynomial[]{result, temp};
    }
    private int firstNonZeroBit() {
        for (int i = this.digits.length - 1; i>=0;i--) {
            for (int j = 31; j >= 0 ; j--) {
                if ((this.digits[i] & (1 << j)) != 0) {
                    return 32 * i + j;
                }
            }
        }
        return 0;
    }
    public Polynomial inv() {
            Polynomial rii, ri;
//        BigUInt sii = ONE.copy(), si = ZERO.copy();
            Polynomial tii = ZERO, ti = ONE;

            rii = MODULUS;
            ri = this;

            Polynomial temp;
            int i = 1;
            for (; ; i++) {
                Polynomial[] res = rii.dividePol(ri); // {q, new ri}
                if (res[1].equals(ZERO)) {
                    break;
                }
                rii = ri;
                ri = res[1];
//            temp = si.copy();
//            si = sii.addToThis(si.multiply(res[0]));
//            sii = temp;
                temp = ti;
                ti = tii.add(ti.multiply(res[0]));
                tii = temp;
            }
            return ti;
//            if (i % 2 == 0) {
//                return MODULUS.add(ti);
//            } else {
//                return ti;
//            }
    }
    public Polynomial divide(Polynomial other) {
        return this.multiply(other.inv());
    }

//    public Polynomial multiplyInt(int val) {
//        Polynomial temp = this;
//        Polynomial result = ZERO;
//        for (int i = 0; i < 32; i++) {
//            if ((val & (1 << i)) != 0) {
//                result = result.add(temp);
//            }
//            temp = temp.add(temp);
//        }
//        return result;
//    }
}
