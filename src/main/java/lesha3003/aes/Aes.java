package lesha3003.aes;

public final class Aes {
    public static final int COLUMN_COUNT = 4;
    public static final int ROW_COUNT = 4;
    public static final int BLOCK_SIZE = 16;

    public enum KeySize {
        AES_128,
        AES_192,
        AES_256;

        public int getKeyLengthBits() {
            switch (this) {
                case AES_128:
                    return 128;
                case AES_192:
                    return 192;
                case AES_256:
                    return 256;
                default:
                    //unreachable
                    return 0;
            }
        }
    }

}
