package lesha3003.sigma;

import java.util.HashMap;
import java.util.Map;

public final class PublicKeyProvider {
    private static Map<Long, byte[][]> map = new HashMap<>();

    public static void registerPk(long id, byte[] pk, byte[] modulus) {
        map.put(id, new byte[][] {pk, modulus});
    }
    public static byte[] getPk(long id) {
        return map.get(id)[0];
    }
    public static byte[] getModulus(long id) {
        return map.get(id)[1];
    }
}
