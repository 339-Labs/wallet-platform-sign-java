package com.labs339.platform.algorithm;

import com.labs339.platform.dao.ExtendedKey;
import com.labs339.platform.dao.KeyPair;
import com.labs339.platform.enums.AlgorithmType;
import com.labs339.platform.enums.CoinType;
import com.labs339.platform.enums.DepthEnum;
import com.labs339.platform.utils.Utils;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static com.labs339.platform.utils.Utils.getFingerprint;
import static com.labs339.platform.utils.Utils.hmacSha512;

@Component
public class Eddsa_ed25519 extends Seed implements AlgorithmStrategy{
    private static final Logger log = LoggerFactory.getLogger(Eddsa_ed25519.class);

    // 继承 Seed 中的方法
    @Override
    public void initSeed() {
        super.initSeed();  // 如果需要保留父类的初始化逻辑，可以调用 super.initSeed();
        // 你可以根据需要重写父类的 initSeed() 方法
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.EDDSA_25519;
    }

    @Override
    public KeyPair getKeyPair(String coin, int index) throws Exception {
        CoinType coinType = CoinType.getCoinType(coin);

        // path =  m/purpose'/coin_type'/account'/change'   "m/44'/501'/0'/0'"
        StringBuffer path = new StringBuffer("m/44'/");
        path.append(coinType.getCoinType()+"'/");
        path.append(index+"'");
        if (DepthEnum.Four == coinType.getDepth()){
            path.append("/0'");
        }
        if (DepthEnum.Five == coinType.getDepth()){
            path.append("/0'/0'");
        }

        ExtendedKey extendedKey = derivePathEd25519(SEED,path.toString());
        byte[] publicKey = getPublicKeyEd25519(extendedKey.getKey());

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
     * 从种子生成 Ed25519 主密钥（SLIP-0010标准）
     * @param seed 种子（建议64字节）
     * @return 扩展密钥
     */
    public static ExtendedKey fromSeedEd25519(byte[] seed) throws Exception {
        byte[] hmac = hmacSha512("ed25519 seed".getBytes(), seed);
        byte[] masterKey = Arrays.copyOfRange(hmac, 0, 32);
        byte[] chainCode = Arrays.copyOfRange(hmac, 32, 64);

        return new ExtendedKey(masterKey, chainCode, 0, 0, new byte[4]);
    }


    /**
     * bip32 secp256k1 派生子密钥，seed 生成无限多个子公私钥对
     * Ed25519 派生子密钥（仅支持强化派生）
     * Ed25519 不支持非强化派生，所以所有派生都是强化的
     * @param parent 父扩展密钥
     * @param index 子密钥索引
     * @return 子扩展密钥
     */
    public static ExtendedKey deriveChildEd25519(ExtendedKey parent, int index) throws Exception {
        // Ed25519 只支持强化派生
        int hardenedIndex = index | HARDENED_BIT;

        ByteBuffer data = ByteBuffer.allocate(37);
        data.put((byte) 0x00);
        data.put(parent.getKey());
        data.putInt(hardenedIndex);

        byte[] hmac = hmacSha512(parent.getChainCode(), data.array());
        byte[] childKey = Arrays.copyOfRange(hmac, 0, 32);
        byte[] childChainCode = Arrays.copyOfRange(hmac, 32, 64);

        byte[] fingerprint = getFingerprint(getPublicKeyEd25519(parent.getKey()));

        return new ExtendedKey(childKey, childChainCode, parent.getDepth() + 1, hardenedIndex, fingerprint);
    }


    /**
     * Ed25519 路径派生
     * @param seed 种子
     * @param path 派生路径，例如 "m/44'/501'/0'/0'"  m/purpose'/coin_type'/account'/change'  ' 符号表示硬化派生
     * @return 派生的扩展密钥
     */
    public static ExtendedKey derivePathEd25519(byte[] seed, String path) throws Exception {
        ExtendedKey key = fromSeedEd25519(seed);

        if (!path.startsWith("m/") && !path.startsWith("M/")) {
            throw new IllegalArgumentException("Path must start with m/ or M/");
        }

        String[] parts = path.substring(2).split("/");
        for (String part : parts) {
            if (part.isEmpty()) continue;

            // Ed25519 所有派生都应该是强化的
            boolean hardened = part.endsWith("'") || part.endsWith("h");
            int index = Integer.parseInt(part.replaceAll("['^h]", ""));

            if (!hardened) {
                System.out.println("Warning: Ed25519 should use hardened derivation. Auto-converting to hardened.");
            }

            key = deriveChildEd25519(key, index);
        }

        return key;
    }

    /**
     * 从 Ed25519 私钥获取公钥
     */
    public static byte[] getPublicKeyEd25519(byte[] privateKey) {
        try {
            // 修正：getByName 返回的是 EdDSANamedCurveSpec
            net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec curveSpec =
                    net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable.getByName("Ed25519");

            net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec privKeySpec =
                    new net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec(privateKey, curveSpec);

            net.i2p.crypto.eddsa.EdDSAPrivateKey privKey =
                    new net.i2p.crypto.eddsa.EdDSAPrivateKey(privKeySpec);

            // 获取公钥
            net.i2p.crypto.eddsa.EdDSAPublicKey pubKey =
                    new net.i2p.crypto.eddsa.EdDSAPublicKey(
                            new net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec(privKey.getA(), curveSpec));

            return pubKey.getAbyte();
        } catch (Exception e) {
            // 备选方案：使用 BouncyCastle 的 Ed25519
            try {
                return getPublicKeyEd25519BouncyCastle(privateKey);
            } catch (Exception ex) {
                // 如果都不可用，使用简化的占位符
                // 实际生产环境必须正确实现
                log.error("Ed25519 public key derivation failed",ex);
                throw new RuntimeException("Ed25519 public key derivation failed", ex);
            }
        }
    }


    /**
     * 使用 BouncyCastle 实现 Ed25519 公钥派生（备选方案）
     */
    private static byte[] getPublicKeyEd25519BouncyCastle(byte[] privateKey) throws Exception {
        // 使用 BouncyCastle 的 Ed25519
        org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters privateKeyParams =
                new org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters(privateKey, 0);

        org.bouncycastle.crypto.params.Ed25519PublicKeyParameters publicKeyParams =
                privateKeyParams.generatePublicKey();

        byte[] publicKey = new byte[32];
        publicKeyParams.encode(publicKey, 0);

        return publicKey;
    }


    /**
     * 签名数据
     * @param privateKey 私钥（32字节）
     * @param message 原始消息（任意长度）
     * @return 签名（64字节）
     */
    public static byte[] sign(byte[] privateKey, byte[] message) {
        // 1. 创建签名器
        Ed25519Signer signer = new Ed25519Signer();

        // 2. 初始化私钥
        Ed25519PrivateKeyParameters privKey = new Ed25519PrivateKeyParameters(privateKey, 0);

        // 3. 初始化签名器
        signer.init(true, privKey);

        // 4. 更新消息
        signer.update(message, 0, message.length);

        // 5. 生成签名
        return signer.generateSignature();
    }

    /**
     * 验证签名
     * @param publicKey 公钥（32字节）
     * @param message 原始消息
     * @param signature 签名（64字节）
     * @return 是否验证通过
     */
    public static boolean verify(byte[] publicKey, byte[] message, byte[] signature) {
        try {
            // 1. 创建验证器
            Ed25519Signer verifier = new Ed25519Signer();

            // 2. 初始化公钥
            Ed25519PublicKeyParameters pubKey = new Ed25519PublicKeyParameters(publicKey, 0);

            // 3. 初始化验证器
            verifier.init(false, pubKey);

            // 4. 更新消息
            verifier.update(message, 0, message.length);

            // 5. 验证签名
            return verifier.verifySignature(signature);

        } catch (Exception e) {
            return false;
        }
    }



}
