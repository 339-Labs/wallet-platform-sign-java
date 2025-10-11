package com.labs339.platform.algorithm;

import com.labs339.platform.dao.KeyPair;
import com.labs339.platform.enums.AlgorithmType;
import com.labs339.platform.enums.CoinType;
import com.labs339.platform.enums.TxTypeEnum;
import com.labs339.platform.utils.Utils;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Arrays;

@Component
public class Ecdsa_secp256k1_evm extends Ecdsa_secp256k1 {


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
        CoinType coinType = CoinType.getCoinType(coin);
        byte[] signatureByte = signEthereum(keyPair.getPrivateKey(), Base64.decode(msg), TxTypeEnum.getByType(coinType));
        return Base64.toBase64String(signatureByte);
    }

    /**
     * Ethereum 签名（用于交易签名）
     * 格式：r(32) + s(32) + v(1)
     *
     * @param privateKey 私钥
     * @param txHash 交易哈希（已由外部服务构建好）
     * @param txTypeEnum 链ID（1=以太坊主网, 56=BSC, null=legacy格式,tron）
     * @return 65字节签名
     */
    public static byte[] signEthereum(byte[] privateKey, byte[] txHash, TxTypeEnum txTypeEnum) throws Exception {
        // 1. 获取原始签名
        byte[] rawSig = sign(privateKey, txHash);
        BigInteger r = new BigInteger(1, Arrays.copyOfRange(rawSig, 0, 32));
        BigInteger s = new BigInteger(1, Arrays.copyOfRange(rawSig, 32, 64));

        // 2. 计算恢复ID
        byte[] publicKey = getPublicKeySecp256k1(privateKey, false);
        int recId = calculateRecoveryId(r, s, txHash, publicKey);
        if (recId == -1) {
            throw new RuntimeException("无法计算恢复ID");
        }

        // 3. 计算v值
        int v;
        if (TxTypeEnum.isLegacy(txTypeEnum)) {
            // Legacy 格式（EIP-155之前）波场链
            v = 27 + recId;
        } else if (TxTypeEnum.isEvmEip1559(txTypeEnum)){
            // EIP-1559 格式 chainId 独立字段
            v = recId;
        }else if (TxTypeEnum.isEvmEip155(txTypeEnum)) {
            // EIP-155 格式，chainId 在v内部
            v = txTypeEnum.getChainId() * 2 + 35 + recId;
        }else {
            v = recId;
        }

        // 4. 组装签名：r + s + v
        byte[] result = new byte[65];
        System.arraycopy(rawSig, 0, result, 0, 64);
        result[64] = (byte) v;
        return result;
    }

    /**
     * 计算恢复ID（用于 Ethereum/Tron 的 v 值）
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

    private static byte[] recoverFromRecoveryId(int recId, BigInteger r, BigInteger s, byte[] messageHash) {
        BigInteger n = CURVE.getN();
        BigInteger i = BigInteger.valueOf((long) recId / 2);
        BigInteger x = r.add(i.multiply(n));

        if (x.compareTo(CURVE.getCurve().getField().getCharacteristic()) >= 0) {
            return null;
        }

        ECPoint R = decompressKey(x, (recId & 1) == 1);
        if (!R.multiply(n).isInfinity()) {
            return null;
        }

        BigInteger e = new BigInteger(1, messageHash);
        BigInteger rInv = r.modInverse(n);
        BigInteger srInv = rInv.multiply(s).mod(n);
        BigInteger eInv = rInv.multiply(e).mod(n);

        ECPoint q = CURVE.getG().multiply(eInv).negate().add(R.multiply(srInv));
        return q.getEncoded(false);
    }

    private static ECPoint decompressKey(BigInteger x, boolean yBit) {
        byte[] compEnc = new byte[33];
        compEnc[0] = (byte) (yBit ? 0x03 : 0x02);
        byte[] xBytes = Utils.toBytesPadded(x, 32);
        System.arraycopy(xBytes, 0, compEnc, 1, 32);
        return CURVE.getCurve().decodePoint(compEnc);
    }


}
