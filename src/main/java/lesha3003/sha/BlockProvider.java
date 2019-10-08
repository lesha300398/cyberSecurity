package lesha3003.sha;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import lesha3003.sha.padding.Padding;


public final class BlockProvider {
    public BlockProvider(int blockSize, Padding padding) {
        this.blockSize = blockSize;
        this.padding = padding;
    }

    public int blockSize;
    public Padding padding;

    public interface BlockSequence {
        byte[] nextBlock() throws IOException;
    }

    public BlockSequence blocksFromInputStream(final InputStream is) {
        return new BlockSequence() {
            Boolean streamDone = false;
            Padding p = padding;
            InputStream inputStream = is;

            @Override
            public byte[] nextBlock() throws IOException {
                if (streamDone) {
                    return null;
                }
                byte[] block = new byte[blockSize];

                int readBytes = inputStream.read(block);
                if (readBytes == blockSize) {
//                        if (inputStream.available() == 0 && padding == null) {
//                            streamDone = true;
//                        }
                    return block;
                } else if (readBytes == -1) {
                    if (p == null) {
                        streamDone = true;
                        return null;
                    } else {
                        inputStream = new ByteArrayInputStream(p.getPaddedBlock(new byte[0]));
                        p = null;
                        return nextBlock();
                    }
                } else {
                    byte[] unfinishedBlock = new byte[readBytes];
                    for (int i = 0; i < readBytes; i++) {
                        unfinishedBlock[i] = block[i];
                    }
                    if (p == null) {
                        streamDone = true;
                        return unfinishedBlock;
                    } else {
                        inputStream = new ByteArrayInputStream(p.getPaddedBlock(unfinishedBlock));
                        p = null;
                        return nextBlock();
                    }
                }
            }

        };
    }

//    public static Enumeration<byte[]> blocksFromFile(final File file, final Padding padding) {
//        try {
//            return blocksFromInputStream(new FileInputStream(file), padding);
//        } catch (FileNotFoundException ex) {
//            System.out.print(ex);
//            return new Enumeration<byte[]>() {
//                @Override
//                public boolean hasMoreElements() {
//                    return false;
//                }
//
//                @Override
//                public byte[] nextElement() {
//                    return new byte[0];
//                }
//            };
//        }
//    }
//
//    public static Enumeration<byte[]> blocksFromBytes(final byte[] bytes, final Padding padding) {
//        return blocksFromInputStream(new ByteArrayInputStream(bytes), padding);
//    }
//
//    public static Enumeration<byte[]> blocksFromHexString(final String hexString, final Padding padding) {
//        return blocksFromBytes(Utils.hexStringToByteArray(hexString), padding);
//    }
}

