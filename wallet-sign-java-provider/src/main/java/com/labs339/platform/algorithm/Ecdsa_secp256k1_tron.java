package com.labs339.platform.algorithm;

import com.labs339.platform.dao.KeyPair;
import com.labs339.platform.enums.AlgorithmType;
import com.labs339.platform.utils.Utils;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Tron 签名策略
 * - 使用 secp256k1 曲线
 * - 签名格式：r(32) + s(32) + v(1) = 65 字节
 * - v 值使用 Legacy 格式：v = 27 + recId
 * - 地址格式：Base58Check 编码，以 41 (hex) 开头，显示为 T 开头
 */
@Component
public class Ecdsa_secp256k1_tron extends Ecdsa_secp256k1 {

    // Tron 地址前缀（主网）
    private static final byte TRON_ADDRESS_PREFIX = 0x41;

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.ECDSA_K1;
    }

    @Override
    public KeyPair getKeyPair(String coin, int index) throws Exception {
        KeyPair keyPair = super.getKeyPair(coin, index);
        // 可选：生成 Tron 格式地址
        // String tronAddress = publicKeyToTronAddress(Hex.decode(keyPair.getPublicKeyHex()));
        return keyPair;
    }

    @Override
    public String sign(String coin, int index, String msg) throws Exception {
        KeyPair keyPair = getKeyPair(coin, index);
        byte[] signatureByte = signTron(keyPair.getPrivateKey(), Base64.decode(msg));
        return Base64.toBase64String(signatureByte);
    }

    /**
     * Tron 签名
     * 格式：r(32) + s(32) + v(1) = 65 字节
     * v 使用 Legacy 格式：v = 27 + recId
     *
     * @param privateKey 私钥（32字节）
     * @param txHash     交易哈希（已由外部服务构建好）
     * @return 65字节签名
     */
    public static byte[] signTron(byte[] privateKey, byte[] txHash) throws Exception {
        // 1. 获取原始签名 (r + s)
        byte[] rawSig = sign(privateKey, txHash);
        BigInteger r = new BigInteger(1, Arrays.copyOfRange(rawSig, 0, 32));
        BigInteger s = new BigInteger(1, Arrays.copyOfRange(rawSig, 32, 64));

        // 2. 计算恢复 ID
        byte[] publicKey = getPublicKeySecp256k1(privateKey, false);
        int recId = calculateRecoveryId(r, s, txHash, publicKey);
        if (recId == -1) {
            throw new RuntimeException("无法计算恢复ID");
        }

        // 3. 计算 v 值（Tron 使用 Legacy 格式）
        int v = 27 + recId;

        // 4. 组装签名：r + s + v
        byte[] result = new byte[65];
        System.arraycopy(rawSig, 0, result, 0, 64);
        result[64] = (byte) v;
        return result;
    }

    /**
     * 计算恢复 ID
     */
    private static int calculateRecoveryId(BigInteger r, BigInteger s, byte[] messageHash, byte[] publicKey) {
        for (int i = 0; i < 4; i++) {
            try {
                byte[] recovered = recoverFromRecoveryId(i, r, s, messageHash);
                if (recovered != null && Arrays.equals(recovered, publicKey)) {
                    return i;
                }
            } catch (Exception e) {
                // 继续尝试下一个
            }
        }
        return -1;
    }

    /**
     * 从恢复 ID 恢复公钥
     */
    private static byte[] recoverFromRecoveryId(int recId, BigInteger r, BigInteger s, byte[] messageHash) {
        BigInteger n = CURVE.getN();
        BigInteger i = BigInteger.valueOf((long) recId / 2);
        BigInteger x = r.add(i.multiply(n));

        if (x.compareTo(CURVE.getCurve().getField().getCharacteristic()) >= 0) {
            return null;
        }

        org.bouncycastle.math.ec.ECPoint R = decompressKey(x, (recId & 1) == 1);
        if (!R.multiply(n).isInfinity()) {
            return null;
        }

        BigInteger e = new BigInteger(1, messageHash);
        BigInteger rInv = r.modInverse(n);
        BigInteger srInv = rInv.multiply(s).mod(n);
        BigInteger eInv = rInv.multiply(e).mod(n);

        org.bouncycastle.math.ec.ECPoint q = CURVE.getG().multiply(eInv).negate().add(R.multiply(srInv));
        return q.getEncoded(false);
    }

    /**
     * 解压公钥点
     */
    private static org.bouncycastle.math.ec.ECPoint decompressKey(BigInteger x, boolean yBit) {
        byte[] compEnc = new byte[33];
        compEnc[0] = (byte) (yBit ? 0x03 : 0x02);
        byte[] xBytes = Utils.toBytesPadded(x, 32);
        System.arraycopy(xBytes, 0, compEnc, 1, 32);
        return CURVE.getCurve().decodePoint(compEnc);
    }

    /**
     * 从公钥生成 Tron 地址
     * 格式：Base58Check(0x41 + Keccak256(pubKey)[12:32])
     *
     * @param publicKey 未压缩公钥（65字节，包含 0x04 前缀）
     * @return Tron 地址（以 T 开头）
     */
    public static String publicKeyToTronAddress(byte[] publicKey) throws Exception {
        // 1. 移除 0x04 前缀，取后 64 字节
        byte[] pubKeyWithoutPrefix = publicKey;
        if (publicKey.length == 65 && publicKey[0] == 0x04) {
            pubKeyWithoutPrefix = Arrays.copyOfRange(publicKey, 1, 65);
        }

        // 2. Keccak256 哈希
        byte[] hash = Utils.keccak256(pubKeyWithoutPrefix);

        // 3. 取后 20 字节
        byte[] addressBytes = Arrays.copyOfRange(hash, 12, 32);

        // 4. 添加 Tron 前缀 0x41
        byte[] addressWithPrefix = new byte[21];
        addressWithPrefix[0] = TRON_ADDRESS_PREFIX;
        System.arraycopy(addressBytes, 0, addressWithPrefix, 1, 20);

        // 5. Base58Check 编码
        return base58CheckEncode(addressWithPrefix);
    }

    /**
     * Base58Check 编码
     */
    private static String base58CheckEncode(byte[] data) throws Exception {
        // 计算校验和（双 SHA256 的前 4 字节）
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] hash1 = sha256.digest(data);
        byte[] hash2 = sha256.digest(hash1);
        byte[] checksum = Arrays.copyOfRange(hash2, 0, 4);

        // 拼接数据和校验和
        byte[] dataWithChecksum = new byte[data.length + 4];
        System.arraycopy(data, 0, dataWithChecksum, 0, data.length);
        System.arraycopy(checksum, 0, dataWithChecksum, data.length, 4);

        // Base58 编码
        return base58Encode(dataWithChecksum);
    }

    /**
     * Base58 编码
     */
    private static String base58Encode(byte[] input) {
        final String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
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
}
