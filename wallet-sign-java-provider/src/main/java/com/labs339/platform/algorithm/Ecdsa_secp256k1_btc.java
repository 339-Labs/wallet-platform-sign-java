package com.labs339.platform.algorithm;

import com.labs339.platform.dao.KeyPair;
import com.labs339.platform.enums.AlgorithmType;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;

import java.math.BigInteger;
import java.util.Arrays;

@Component
public class Ecdsa_secp256k1_btc extends Ecdsa_secp256k1 {


    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.ECDSA_K1;
    }

    @Override
    public KeyPair getKeyPair(String coin, int index) throws Exception {
        return super.getKeyPair(coin, index);
    }

    @Override
    public String sign(String coin, int index, String msg) throws Exception {
        KeyPair keyPair = getKeyPair(coin,index);

        byte[] bytes = signBitcoin(keyPair.getPrivateKey(),Base64.decode(msg));

        return Base64.toBase64String(bytes);
    }

    /**
     * Bitcoin 签名（DER 编码格式）
     * 用于 P2PKH/P2WPKH 交易
     *
     * Bitcoin SegWit 签名（与传统签名相同，但用于不同的 sighash 算法）
     * SegWit 的签名格式与传统相同，区别在于交易构建时的 sighash 计算
     *
     * @param privateKey 私钥
     * @param txHash 交易哈希（已由外部服务构建好的 sighash）
     * @return DER 编码的签名（可变长度，通常 70-72 字节）
     */
    public static byte[] signBitcoin(byte[] privateKey, byte[] txHash) throws Exception {
        // 1. 获取原始签名
        byte[] rawSig = sign(privateKey, txHash);
        BigInteger r = new BigInteger(1, Arrays.copyOfRange(rawSig, 0, 32));
        BigInteger s = new BigInteger(1, Arrays.copyOfRange(rawSig, 32, 64));

        // 2. DER 编码
        return encodeDER(r, s);
    }
    /**
     * Bitcoin 签名（带 SIGHASH 类型）
     * 实际使用时需要在签名后附加 SIGHASH 类型字节 ，可用于多签
     *
     * @param sigHashType SIGHASH类型（0x01=SIGHASH_ALL, 0x02=SIGHASH_NONE, 0x03=SIGHASH_SINGLE）
     */
    public static byte[] signBitcoinWithSigHashType(byte[] privateKey, byte[] txHash, byte sigHashType) throws Exception {
        byte[] derSig = signBitcoin(privateKey, txHash);

        // 附加 SIGHASH 类型
        byte[] result = new byte[derSig.length + 1];
        System.arraycopy(derSig, 0, result, 0, derSig.length);
        result[derSig.length] = sigHashType;
        return result;
    }

    /**
     * DER 编码（Bitcoin 专用）
     * 格式：0x30 [总长度] 0x02 [r长度] [r] 0x02 [s长度] [s]
     */
    private static byte[] encodeDER(BigInteger r, BigInteger s) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // 编码 r
            byte[] rBytes = r.toByteArray();
            bos.write(0x02); // INTEGER 标签
            bos.write(rBytes.length);
            bos.write(rBytes);

            // 编码 s
            byte[] sBytes = s.toByteArray();
            bos.write(0x02); // INTEGER 标签
            bos.write(sBytes.length);
            bos.write(sBytes);

            // 外层 SEQUENCE
            byte[] sequence = bos.toByteArray();
            bos.reset();
            bos.write(0x30); // SEQUENCE 标签
            bos.write(sequence.length);
            bos.write(sequence);

            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("DER 编码失败", e);
        }
    }


}
