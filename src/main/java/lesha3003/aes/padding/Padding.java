package lesha3003.aes.padding;

public interface Padding {
    byte[] getPaddedBlock(byte[] block);
}
