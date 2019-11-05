package lesha3003.digitalsignature.rsa;

import java.util.Arrays;

public final class Rsa {
    public int modulusLen;

    private RsaCore rsaCore;

    public Rsa(int modulusLenBytes) {
        this.modulusLen = modulusLenBytes;
        this.rsaCore = new RsaCore(modulusLenBytes * 8);
    }
    public byte[] encrypt(byte[] message) {
        return rsaCore.encrypt(message);
    }
    public byte[] decrypt(byte[] cipher) {
        return rsaCore.decrypt(cipher);
    }
    public byte[] decryptAndUnpad(byte[] cipher) throws UnpaddingException {
        return unpad(rsaCore.decrypt(cipher));
    }

    public byte[] pad(byte[] message) {
        if (message.length > modulusLen - 3) {
            throw new IllegalArgumentException();
        }
        byte[] paddedMessage = new byte[modulusLen];
        System.arraycopy(message, 0, paddedMessage, paddedMessage.length - message.length, message.length);
        paddedMessage[0] = 0x00;
        paddedMessage[1] = 0x01;
        for (int i = 2; i < paddedMessage.length - message.length - 1; i++) {
            paddedMessage[i] = (byte)0xFF;
        }
        paddedMessage[paddedMessage.length - message.length - 1] = 0x00;
        return paddedMessage;
    }
    public byte[] unpad(byte[] paddedMessage) throws UnpaddingException {
        if (paddedMessage.length > modulusLen) {
            throw new UnpaddingException();
        }
        int i;
        if (paddedMessage[0] == 0x00 && paddedMessage[1] == 0x01) {
            i = 2;
        } else if (paddedMessage[0] == 0x01) {
            i = 1;
        } else {
            throw new UnpaddingException();
        }
        while (paddedMessage[i] == (byte)0xFF) {
            i++;
        }
        if (paddedMessage[i] == 0x00) {
            i++;
        } else {
            throw new UnpaddingException();
        }

        return Arrays.copyOfRange(paddedMessage, i, paddedMessage.length);
    }

    public class UnpaddingException extends Exception {}
}
