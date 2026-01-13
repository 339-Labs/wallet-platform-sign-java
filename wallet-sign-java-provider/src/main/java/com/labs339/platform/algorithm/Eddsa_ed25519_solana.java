package com.labs339.platform.algorithm;

import com.labs339.platform.dao.KeyPair;
import com.labs339.platform.enums.AlgorithmType;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * Solana 签名策略
 * - 使用 Ed25519 曲线
 * - 派生路径：m/44'/501'/account'/change' (BIP44)
 * - 签名格式：64 字节纯签名
 * - 地址格式：Base58 编码的公钥（32字节）
 */
@Component
public class Eddsa_ed25519_solana extends Eddsa_ed25519 {

    private static final Logger log = LoggerFactory.getLogger(Eddsa_ed25519_solana.class);

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.EDDSA_25519;
    }

    @Override
    public KeyPair getKeyPair(String coin, int index) throws Exception {
        // 复用父类方法，Solana 派生路径已在父类中通过 DepthEnum.Four 处理
        return super.getKeyPair(coin, index);
    }

    @Override
    public String sign(String coin, int index, String msg) throws Exception {
        // 复用父类方法，Solana 使用标准 Ed25519 签名
        return super.sign(coin, index, msg);
    }

    /**
     * 从公钥生成 Solana 地址
     * Solana 地址就是 Base58 编码的公钥
     *
     * @param publicKey Ed25519 公钥（32字节）
     * @return Solana 地址（Base58 编码）
     */
    public static String publicKeyToSolanaAddress(byte[] publicKey) {
        return base58Encode(publicKey);
    }

    /**
     * 从私钥生成 Solana Keypair 格式
     * Solana CLI 使用的格式：[privateKey(32) + publicKey(32)] = 64字节的JSON数组
     *
     * @param privateKey 私钥（32字节）
     * @return Solana keypair JSON 格式字符串
     */
    public static String toSolanaKeypairJson(byte[] privateKey) {
        byte[] publicKey = getPublicKeyEd25519(privateKey);
        
        // 组合成 64 字节
        byte[] keypair = new byte[64];
        System.arraycopy(privateKey, 0, keypair, 0, 32);
        System.arraycopy(publicKey, 0, keypair, 32, 32);

        // 转换为 JSON 数组格式
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < keypair.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(keypair[i] & 0xFF);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 验证 Solana 签名
     *
     * @param publicKey 公钥（32字节）
     * @param message   原始消息
     * @param signature 签名（64字节）
     * @return 是否验证通过
     */
    public static boolean verifySolanaSignature(byte[] publicKey, byte[] message, byte[] signature) {
        return verify(publicKey, message, signature);
    }

    /**
     * Base58 编码（Solana 使用标准 Base58，不带校验和）
     */
    private static String base58Encode(byte[] input) {
        final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
        
        if (input.length == 0) {
            return "";
        }

        BigInteger num = new BigInteger(1, input);
        StringBuilder sb = new StringBuilder();

        while (num.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divmod = num.divideAndRemainder(BigInteger.valueOf(58));
            sb.insert(0, ALPHABET.charAt(divmod[1].intValue()));
            num = divmod[0];
        }

        // 处理前导零
        for (byte b : input) {
            if (b == 0) {
                sb.insert(0, '1');
            } else {
                break;
            }
        }

        return sb.toString();
    }

    /**
     * Base58 解码
     */
    public static byte[] base58Decode(String input) {
        final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
        
        if (input.isEmpty()) {
            return new byte[0];
        }

        BigInteger num = BigInteger.ZERO;
        for (char c : input.toCharArray()) {
            int digit = ALPHABET.indexOf(c);
            if (digit == -1) {
                throw new IllegalArgumentException("Invalid Base58 character: " + c);
            }
            num = num.multiply(BigInteger.valueOf(58)).add(BigInteger.valueOf(digit));
        }

        byte[] bytes = num.toByteArray();
        
        // 处理前导零
        int leadingZeros = 0;
        for (char c : input.toCharArray()) {
            if (c == '1') {
                leadingZeros++;
            } else {
                break;
            }
        }

        // 移除 BigInteger 可能添加的前导零字节
        int stripZeros = 0;
        if (bytes.length > 0 && bytes[0] == 0) {
            stripZeros = 1;
        }

        byte[] result = new byte[leadingZeros + bytes.length - stripZeros];
        System.arraycopy(bytes, stripZeros, result, leadingZeros, bytes.length - stripZeros);
        
        return result;
    }
}
