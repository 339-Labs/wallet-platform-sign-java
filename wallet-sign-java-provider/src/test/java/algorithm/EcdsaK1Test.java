package algorithm;

import com.labs339.platform.WalletSignApplication;
import com.labs339.platform.algorithm.Ecdsa_secp256k1;
import com.labs339.platform.dao.ExtendedKey;
import com.labs339.platform.utils.Utils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utils.SeedManagementUtilsTest;

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
            byte[] publickey = Ecdsa_secp256k1.getPublicKeySecp256k1(key.getKey(),false);

            String path1 = "m/44'/60'/1'/0/0";
            ExtendedKey key1 = Ecdsa_secp256k1.derivePathSecp256k1(seed,path1);
            byte[] publickey1 = Ecdsa_secp256k1.getPublicKeySecp256k1(key1.getKey(),false);


        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

}
