package algorithm;

import com.labs339.platform.WalletSignApplication;
import com.labs339.platform.algorithm.Ecdsa_secp256k1;
import com.labs339.platform.dao.ExtendedKey;
import com.labs339.platform.utils.Utils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.crypto.Keys;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;
import utils.SeedManagementUtilsTest;

import java.security.Key;
import java.security.MessageDigest;

@SpringBootTest(classes = WalletSignApplication.class)
public class EcdsaK1Test {

    private static final Logger log = LoggerFactory.getLogger(Ecdsa_secp256k1.class);

    private static final String mnemonic = "cinnamon pluck two medal pattern detect seven media flame endless music sudden";
    private static final String seedHex = "b1d8378e098e95b7629e31986cf9f0fff5a45d1bd9e8c73969664715a1630398851b9f35368ec2d272df5e53a4ddfdefc533a8a3cdf1c1b402e9246f6497dc67";

    @Test
    public void fromSeedSecp256k1(){

        byte[] seed = Utils.hexToBytes(seedHex);

        try {
            ExtendedKey key = Ecdsa_secp256k1.fromSeedSecp256k1(seed);
            System.out.println(key);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void deriveChildSecp256k1(){
        byte[] seed = Utils.hexToBytes(seedHex);
        try {
            ExtendedKey key = Ecdsa_secp256k1.fromSeedSecp256k1(seed);

            key = Ecdsa_secp256k1.deriveChildSecp256k1(key,0);

            System.out.println(key);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void derivePathSecp256k1(){
        byte[] seed = Utils.hexToBytes(seedHex);
        try {
            //  m/purpose'/coin_type'/account'/change/address_index
            String path = "m/44'/60'/0'/0/0";
            ExtendedKey key = Ecdsa_secp256k1.derivePathSecp256k1(seed,path);
            String privateKey = Hex.toHexString(key.getKey());
            System.out.println("privateKey:"+privateKey);
            byte[] publickey = Ecdsa_secp256k1.getPublicKeySecp256k1(key.getKey(),false);
            String address = publicKeyToAddress(publickey);
            System.out.println("address:"+address); // 0x19c8d1ce3a9f5042b3c2b10ba86132051b40ec1e
            log.info("address:"+address);

            String path1 = "m/44'/60'/1'/0/0";
            ExtendedKey key1 = Ecdsa_secp256k1.derivePathSecp256k1(seed,path1);
            String privateKey1 = Hex.toHexString(key1.getKey());
            System.out.println("privateKey1:"+privateKey1);
            byte[] publickey1 = Ecdsa_secp256k1.getPublicKeySecp256k1(key1.getKey(),false);
            String address2 = publicKeyToAddress(publickey1);  // 0x1cbde8383e723b60ee36cd00a9fc749c0755bc59
            System.out.println("address2:"+address2);
            log.info("address2:"+address2);

            String path2 = "m/44'/60'/0'/0/1";
            ExtendedKey key2 = Ecdsa_secp256k1.derivePathSecp256k1(seed,path2);
            String privateKey2 = Hex.toHexString(key2.getKey());
            System.out.println("privateKey2:"+privateKey2);
            byte[] publickey2 = Ecdsa_secp256k1.getPublicKeySecp256k1(key2.getKey(),false);
            String address3 = publicKeyToAddress(publickey2);  // 0x6bcbf2f66374c4c56330a544683f9c4ef10386fa
            System.out.println("address3:"+address3);
            log.info("address3:"+address3);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * 从公钥生成EVM地址
     *
     * 流程：
     * 1. 取公钥的后64字节（去掉第一个0x04前缀字节）
     * 2. Keccak256哈希
     * 3. 取哈希结果的后20字节
     * 4. 添加0x前缀
     *
     * @param publicKey 未压缩公钥（65字节）
     * @return EVM地址
     */
    public static String publicKeyToAddress(byte[] publicKey) throws Exception {
        if (publicKey.length != 65) {
            throw new IllegalArgumentException("Public key must be 65 bytes (uncompressed)");
        }

        // 1. 去掉第一个字节（0x04前缀）
        byte[] publicKeyWithoutPrefix = new byte[64];
        System.arraycopy(publicKey, 1, publicKeyWithoutPrefix, 0, 64);

        // 2. Keccak256哈希
        byte[] hash = Utils.keccak256(publicKeyWithoutPrefix);

        // 3. 取后20字节
        byte[] address = new byte[20];
        System.arraycopy(hash, 12, address, 0, 20);

        // 4. 转换为十六进制并添加0x前缀
        return "0x" + Hex.toHexString(address);
    }


    @Test
    public void sign(){



    }


}
