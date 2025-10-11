package com.labs339.platform.utils;

import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

@Component
public class Utils {

    /**
     * HMAC-SHA512
     */
    public static byte[] hmacSha512(byte[] key, byte[] data) throws Exception {
        HMac hmac = new HMac(new SHA512Digest());
        hmac.init(new KeyParameter(key));
        hmac.update(data, 0, data.length);
        byte[] result = new byte[64];
        hmac.doFinal(result, 0);
        return result;
    }

    /**
     * 给byte 添加前缀
     * @param prefix
     * @param input
     * @return
     */
    public static byte[] addBytePrefix(byte[] prefix, byte[] input) {
        byte[] result = new byte[prefix.length + input.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(input, 0, result, prefix.length, input.length);
        return result;
    }

    /**
     * Keccak256哈希（Ethereum使用的哈希算法）
     * 注意：Keccak256 ≠ SHA3-256
     *
     * @param input 输入数据
     * @return 哈希结果（32字节）
     */
    public static byte[] keccak256(byte[] input) {
        org.bouncycastle.crypto.digests.KeccakDigest digest =
                new org.bouncycastle.crypto.digests.KeccakDigest(256);
        digest.update(input, 0, input.length);
        byte[] hash = new byte[32];
        digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * 获取指纹（公钥哈希的前4字节）
     */
    public static byte[] getFingerprint(byte[] publicKey) throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash = sha256.digest(publicKey);
        MessageDigest ripemd160 = MessageDigest.getInstance("RIPEMD160", "BC");
        byte[] hash160 = ripemd160.digest(hash);
        return Arrays.copyOfRange(hash160, 0, 4);
    }

    /**
     * 字节数组转十六进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 十六进制字符串转字节数组
     */
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 将BigInteger转换为固定长度字节数组
     */
    public static byte[] toBytesPadded(BigInteger value, int length) {
        byte[] result = new byte[length];
        byte[] bytes = value.toByteArray();

        int bytesLength;
        int srcOffset;
        if (bytes[0] == 0) {
            bytesLength = bytes.length - 1;
            srcOffset = 1;
        } else {
            bytesLength = bytes.length;
            srcOffset = 0;
        }

        if (bytesLength > length) {
            throw new RuntimeException("Input is too large");
        }

        int destOffset = length - bytesLength;
        System.arraycopy(bytes, srcOffset, result, destOffset, bytesLength);
        return result;
    }

}
