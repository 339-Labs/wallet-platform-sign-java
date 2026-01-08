package com.labs339.platform.algorithm;

import com.labs339.platform.dao.ExtendedKey;
import com.labs339.platform.dao.KeyPair;
import com.labs339.platform.enums.AlgorithmType;
import com.labs339.platform.enums.CoinType;
import com.labs339.platform.utils.Utils;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.labs339.platform.utils.Utils.getFingerprint;
import static com.labs339.platform.utils.Utils.hmacSha512;

@Component
public class Ecdsa_secp256k1 extends Seed implements AlgorithmStrategy {

    protected static final X9ECParameters CURVE = SECNamedCurves.getByName("secp256k1");
    protected static final ECDomainParameters DOMAIN = new ECDomainParameters(
            CURVE.getCurve(), CURVE.getG(), CURVE.getN(), CURVE.getH()
    );

    // 继承 Seed 中的方法
    @Override
    public void initSeed() {
        super.initSeed();  // 如果需要保留父类的初始化逻辑，可以调用 super.initSeed();
        // 你可以根据需要重写父类的 initSeed() 方法
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.ECDSA_K1;
    }

    @Override
    public KeyPair getKeyPair(String coin, int index) throws Exception {

        CoinType coinType = CoinType.getCoinType(coin);

        // path =  m/purpose'/coin_type'/account'/change/address_index   "m/44'/0'/0'/0/0"
        StringBuffer path = new StringBuffer("m/44'/");
        if (coinType.getIsEvm()){
            path.append(CoinType.Ethereum.getCoinType()+"'/");
        }else {
            path.append(coinType.getCoinType()+"'/");
        }
        path.append(index);
        path.append("'/0/0");

        ExtendedKey extendedKey = derivePathSecp256k1(SEED,path.toString());
        byte[] publicKey = getPublicKeySecp256k1(extendedKey.getKey(),false);

        KeyPair keyPair = new KeyPair();
        keyPair.setCoin(coin);
        keyPair.setIndex(index);
        keyPair.setPublicKeyHex(Hex.toHexString(publicKey));
        keyPair.setPrivateKey(extendedKey.getKey());

        return keyPair;
    }

    @Override
    public String sign(String coin, int index, String msg) throws Exception {
        KeyPair keyPair = getKeyPair(coin,index);
        byte[] signatureByte = sign(keyPair.getPrivateKey(), Base64.decode(msg));
        return Base64.toBase64String(signatureByte);
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


    /**
     * 签名数据
     * @param privateKey 私钥（32字节）
     * @param messageHash 消息哈希（32字节，通常是SHA256或Keccak256的结果）
     * @return 签名（DER编码或R+S格式）
     */
    public static byte[] sign(byte[] privateKey, byte[] messageHash) throws Exception {
        // 1. 创建签名器
        ECDSASigner signer = new ECDSASigner();

        // 2. 设置secp256k1曲线参数
//        X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
//        ECDomainParameters domain = new ECDomainParameters(
//                curve.getCurve(),
//                curve.getG(),
//                curve.getN(),
//                curve.getH()
//        );

        // 3. 初始化私钥
        BigInteger d = new BigInteger(1, privateKey);
        ECPrivateKeyParameters privKey = new ECPrivateKeyParameters(d, DOMAIN);

        // 4. 初始化签名器
        signer.init(true, privKey);

        // 5. 签名
        BigInteger[] signature = signer.generateSignature(messageHash);
        BigInteger r = signature[0];
        BigInteger s = signature[1];

        // 6 规范化s值（防止交易可塑性）
        BigInteger halfCurveOrder = CURVE.getN().shiftRight(1);
        if (s.compareTo(halfCurveOrder) > 0) {
            s = CURVE.getN().subtract(s);
        }

        // 7. 返回R+S格式（64字节）
        byte[] result = new byte[64];
        byte[] rBytes = Utils.toBytesPadded(r, 32);
        byte[] sBytes = Utils.toBytesPadded(s, 32);
        System.arraycopy(rBytes, 0, result, 0, 32);
        System.arraycopy(sBytes, 0, result, 32, 32);
        return result;

    }

    /**
     * 验证签名
     * @param publicKey 公钥（未压缩格式，65字节）
     * @param messageHash 消息哈希
     * @param signature 签名（64字节：R+S）
     * @return 是否验证通过
     */
    public static boolean verify(byte[] publicKey, byte[] messageHash, byte[] signature) {
        try {
            // 1. 解码签名
            BigInteger r = new BigInteger(1, Arrays.copyOfRange(signature, 0, 32));
            BigInteger s = new BigInteger(1, Arrays.copyOfRange(signature, 32, 64));

            // 2. 解析公钥
            X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
            ECPoint point = curve.getCurve().decodePoint(publicKey);

            ECDomainParameters domain = new ECDomainParameters(
                    curve.getCurve(), curve.getG(), curve.getN(), curve.getH()
            );
            ECPublicKeyParameters pubKey = new ECPublicKeyParameters(point, domain);

            // 3. 验证
            ECDSASigner verifier = new ECDSASigner();
            verifier.init(false, pubKey);

            return verifier.verifySignature(messageHash, r, s);

        } catch (Exception e) {
            return false;
        }
    }

}
