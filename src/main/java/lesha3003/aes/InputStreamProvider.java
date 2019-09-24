package lesha3003.aes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class InputStreamProvider {
    private InputStreamProvider(){}

    public static InputStream fromHexString(String hexString) {
        return fromBytes(Utils.hexStringToByteArray(hexString));
    }
    public static InputStream fromBytes(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }
    public static InputStream fromFile(File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }
}
