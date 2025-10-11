package algorithm;

import com.labs339.platform.WalletSignApplication;
import com.labs339.platform.algorithm.AlgorithmStrategy;
import com.labs339.platform.algorithm.Ecdsa_secp256k1_evm;
import com.labs339.platform.dao.KeyPair;
import com.labs339.platform.enums.TxTypeEnum;
import com.labs339.platform.enums.CoinType;
import com.labs339.platform.utils.Utils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = WalletSignApplication.class)
public class EcdsaK1EvmSignTest {

    @Autowired
    private ApplicationContext applicationContext; // 获取 Spring 上下文


    private static final Logger log = LoggerFactory.getLogger(Ecdsa_secp256k1_evm.class);

    private static final String mnemonic = "cinnamon pluck two medal pattern detect seven media flame endless music sudden";
    private static final String seedHex = "b1d8378e098e95b7629e31986cf9f0fff5a45d1bd9e8c73969664715a1630398851b9f35368ec2d272df5e53a4ddfdefc533a8a3cdf1c1b402e9246f6497dc67";

    @Test
    public void sign(){

        // 使用 applicationContext
        assertNotNull(applicationContext);
        // 可以测试其他bean是否加载
        assertNotNull(applicationContext.getBean(Ecdsa_secp256k1_evm.class));

        try {

            CoinType coinType = CoinType.Arbitrum_Sepolia;

            BigInteger nonce = BigInteger.valueOf(0);
            BigInteger maxFeePerGas = BigInteger.valueOf(26000000000L);  // 4 Gwei
            BigInteger maxPriorityFeePerGas = BigInteger.valueOf(20520000000L);  // 2 Gwei
            long gasLimit = 100000L;  // 默认的 gas limit
            String toAddress = "0x6bcbf2f66374c4c56330a544683f9c4ef10386fa";
            BigInteger value = BigInteger.valueOf(10000000000000000L);  // 0.01 ETH
            String data = "0x";  // 空数据
            int chainId = TxTypeEnum.Arbitrum_Sepolia.getChainId();  // arb test

            // 1 EIP-1559 签名前的格式: 0x02 || RLP(chainId, nonce, maxPriorityFeePerGas, maxFeePerGas, gasLimit, to, value, data, accessList)

            // 1. 构建待签名的 RLP 编码
            List<RlpType> values = new ArrayList<>();
            values.add(RlpString.create(chainId));
            values.add(RlpString.create(nonce));
            values.add(RlpString.create(maxPriorityFeePerGas));
            values.add(RlpString.create(maxFeePerGas));
            values.add(RlpString.create(gasLimit));
            values.add(RlpString.create(Numeric.hexStringToByteArray(toAddress)));
            values.add(RlpString.create(value));
            values.add(RlpString.create(data));
            values.add(new RlpList());  // 空的 accessList

            byte[] rlpEncoded = RlpEncoder.encode(new RlpList(values));

            /**
             * 0x01：表示传统的以太坊交易类型（EIP-155）。
             * 0x02：表示 EIP-1559 交易类型，包含 maxPriorityFeePerGas 和 maxFeePerGas 字段。
             * 这类交易允许用户设置更细粒度的费用参数，尤其适应于网络拥堵时的交易费用调节。
             */
            // 2 计算hash
            byte[] prefix = new byte[] { (byte) 0x02 };
            byte[] dataToHash = Utils.addBytePrefix(prefix,rlpEncoded);
            byte[] txHash = Utils.keccak256(dataToHash);

            // 3 签名
            AlgorithmStrategy algorithmStrategy = applicationContext.getBean(coinType.getAlgorithmStrategyClass());
            KeyPair key = algorithmStrategy.getKeyPair(coinType.getCoin(), 0);

            byte[] signatureByte = Ecdsa_secp256k1_evm.signEthereum(key.getPrivateKey(),txHash, TxTypeEnum.Arbitrum_Sepolia);

            // 4. 解析签名
            byte[] r = new byte[32];
            byte[] s = new byte[32];
            System.arraycopy(signatureByte, 0, r, 0, 32);
            System.arraycopy(signatureByte, 32, s, 0, 32);
            int v = signatureByte[64] & 0xFF;  // yParity: 0 or 1


            // 5. 构建完整交易 , 创建签名后的交易
            List<RlpType> signedValues = new ArrayList<>();
            signedValues.add(RlpString.create(chainId));
            signedValues.add(RlpString.create(nonce));
            signedValues.add(RlpString.create(maxPriorityFeePerGas));
            signedValues.add(RlpString.create(maxFeePerGas));
            signedValues.add(RlpString.create(gasLimit));
            signedValues.add(RlpString.create(Numeric.hexStringToByteArray(toAddress)));
            signedValues.add(RlpString.create(value));
            signedValues.add(RlpString.create(data));
            signedValues.add(new RlpList());  // 空的 accessList
            signedValues.add(RlpString.create(v));  // yParity
            signedValues.add(RlpString.create(r));
            signedValues.add(RlpString.create(s));

            byte[] signedRlp = RlpEncoder.encode(new RlpList(signedValues));


            // 8. 添加类型前缀 0x02
            byte[] finalTx = new byte[1 + signedRlp.length];
            finalTx[0] = 0x02;
            System.arraycopy(signedRlp, 0, finalTx, 1, signedRlp.length);

            // 6. 转换为十六进制
            System.out.println("0x" + Hex.toHexString(finalTx));

        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }


}
