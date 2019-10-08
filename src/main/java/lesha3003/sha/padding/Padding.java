package lesha3003.sha.padding;

public interface Padding {
    byte[] getPaddedBlock(byte[] block);
}
