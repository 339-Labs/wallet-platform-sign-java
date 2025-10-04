package com.labs339.platform.algorithm;

import com.labs339.platform.dao.ExtendedKey;
import com.labs339.platform.enums.AlgorithmType;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.labs339.platform.utils.Utils.getFingerprint;
import static com.labs339.platform.utils.Utils.hmacSha512;

@Component
public class Ecdsa_secp256k1 extends Seed implements AlgorithmStrategy {

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.ECDSA_R1;
    }

    @Override
    public ExtendedKey getKeyPair(int coin, int index) throws Exception {
        return null;
    }

    @Override
    public String sign(String coin, int index, String msg) throws Exception {
        return "";
    }


    /**
     * master_key, chain_code = HMAC-SHA512(key="Bitcoin seed", data=seed)
     * - 前 32 字节是 master private key（根私钥
     * - 后 32 字节是 chain code（链码，用于派生）
     *
     * 从种子生成 secp256k1 主密钥
     * @param seed 种子（建议64字节）
     * @return 扩展密钥
     */
    public static ExtendedKey fromSeedSecp256k1(byte[] seed) throws Exception {
        byte[] hmac = hmacSha512("Bitcoin seed".getBytes(), seed);
        byte[] masterKey = Arrays.copyOfRange(hmac, 0, 32);
        byte[] chainCode = Arrays.copyOfRange(hmac, 32, 64);

        // 验证私钥有效性
        BigInteger privateKey = new BigInteger(1, masterKey);
        X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
        if (privateKey.compareTo(curve.getN()) >= 0) {
            throw new IllegalArgumentException("Invalid private key");
        }
        return new ExtendedKey(masterKey, chainCode, 0, 0, new byte[4]);
    }

    /**
     * bip32 secp256k1 派生子密钥，seed 生成无限多个子公私钥对
     * @param parent 父扩展密钥
     * @param index 子密钥索引（>= 0x80000000 表示强化派生）
     * @return 子扩展密钥
     */
    public static ExtendedKey deriveChildSecp256k1(ExtendedKey parent, int index) throws Exception {
        boolean hardened = (index & HARDENED_BIT) != 0;

        ByteBuffer data = ByteBuffer.allocate(37);
        if (hardened) {
            // 强化派生：使用私钥
            data.put((byte) 0x00);
            data.put(parent.getKey());
        } else {
            // 普通派生：使用公钥
            byte[] publicKey = getPublicKeySecp256k1(parent.getKey(), true);
            data.put(publicKey);
        }
        data.putInt(index);

        byte[] hmac = hmacSha512(parent.getChainCode(), data.array());
        byte[] il = Arrays.copyOfRange(hmac, 0, 32);  //前 32 字节（用于计算子私钥）
        byte[] ir = Arrays.copyOfRange(hmac, 32, 64); // 后 32 字节（用于计算链码）

        // 计算子私钥 = (parse256(IL) + kpar) mod n
        X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
        BigInteger ilNum = new BigInteger(1, il);
        BigInteger parentKeyNum = new BigInteger(1, parent.getKey());
        BigInteger childKeyNum = ilNum.add(parentKeyNum).mod(curve.getN());

        if (ilNum.compareTo(curve.getN()) >= 0 || childKeyNum.equals(BigInteger.ZERO)) {
            throw new IllegalArgumentException("Invalid child key");
        }

        byte[] childKey = childKeyNum.toByteArray();
        if (childKey.length > 32) {
            childKey = Arrays.copyOfRange(childKey, 1, 33);
        } else if (childKey.length < 32) {
            byte[] padded = new byte[32];
            System.arraycopy(childKey, 0, padded, 32 - childKey.length, childKey.length);
            childKey = padded;
        }

        byte[] fingerprint = getFingerprint(getPublicKeySecp256k1(parent.getKey(), true));

        // m/0'
        return new ExtendedKey(childKey, ir, parent.getDepth() + 1, index, fingerprint);
    }


    /**
     * secp256k1 路径派生
     * @param seed 种子
     * @param path 派生路径，例如 "m/44'/0'/0'/0/0"   m/purpose'/coin_type'/account'/change/address_index ' 符号表示硬化派生
     * @return 派生的扩展密钥
     */
    public static ExtendedKey derivePathSecp256k1(byte[] seed, String path) throws Exception {
        ExtendedKey key = fromSeedSecp256k1(seed);

        if (!path.startsWith("m/") && !path.startsWith("M/")) {
            throw new IllegalArgumentException("Path must start with m/ or M/");
        }

        String[] parts = path.substring(2).split("/");
        for (String part : parts) {
            if (part.isEmpty()) continue;

            boolean hardened = part.endsWith("'") || part.endsWith("h");
            int index = Integer.parseInt(part.replaceAll("['^h]", ""));

            if (hardened) {
                index |= HARDENED_BIT;
            }

            key = deriveChildSecp256k1(key, index);
        }

        return key;
    }


    /**
     * 从私钥获取 secp256k1 公钥
     * compressed 参数是一个布尔值，决定返回的公钥是否为压缩格式：EVM 中的公钥通常是 64 字节 长度，包含了 X 和 Y 坐标
     * 压缩格式（compressed）：公钥的长度是 33 字节。其第一个字节表示公钥的 Y 坐标的奇偶性，后 32 字节表示公钥的 X 坐标。
     * 非压缩格式（uncompressed）：公钥的长度是 65 字节，第一个字节为 0x04，后跟 32 字节的 X 坐标和 32 字节的 Y 坐标。
     */
    public static byte[] getPublicKeySecp256k1(byte[] privateKey, boolean compressed) {
        X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
        BigInteger d = new BigInteger(1, privateKey);
        ECPoint Q = curve.getG().multiply(d);

        return Q.getEncoded(compressed);
    }



}
