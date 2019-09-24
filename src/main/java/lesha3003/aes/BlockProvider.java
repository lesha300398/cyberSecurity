package lesha3003.aes;



import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import lesha3003.aes.padding.Padding;

public final class BlockProvider {
    private BlockProvider(){}

    public interface BlockSequence {
        byte[] nextBlock() throws IOException;
    }
    public static BlockSequence blocksFromInputStream(final InputStream is, final Padding padding) {
        return new BlockSequence() {
            Boolean streamDone = false;

            @Override
            public byte[] nextBlock() throws IOException {
                if (streamDone) {
                    return null;
                }
                byte[] block = new byte[16];

                    int readBytes = is.read(block);
                    if (readBytes == 16) {
//                        if (is.available() == 0 && padding == null) {
//                            streamDone = true;
//                        }
                        return block;
                    } else if (readBytes == -1) {
                        streamDone = true;
                        if (padding == null) {
                            return null;
                        } else {
                            return padding.getPaddedBlock(new byte[0]);
                        }
                    } else {
                        streamDone = true;
                        byte[] unfinishedBlock = new byte[readBytes];
                        for (int i = 0; i < readBytes; i++) {
                            unfinishedBlock[i] = block[i];
                        }
                        if (padding == null) {
                            return unfinishedBlock;
                        } else {
                            return padding.getPaddedBlock(unfinishedBlock);
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

