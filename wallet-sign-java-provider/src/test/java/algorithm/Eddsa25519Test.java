package algorithm;

import com.labs339.platform.WalletSignApplication;
import com.labs339.platform.algorithm.Ecdsa_secp256k1;
import com.labs339.platform.algorithm.Eddsa_ed25519;
import com.labs339.platform.dao.ExtendedKey;
import com.labs339.platform.utils.Utils;
import org.bitcoinj.core.Base58;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = WalletSignApplication.class)
public class Eddsa25519Test {

    private static final Logger log = LoggerFactory.getLogger(Eddsa_ed25519.class);

    private static final String mnemonic = "cinnamon pluck two medal pattern detect seven media flame endless music sudden";
    private static final String seedHex = "b1d8378e098e95b7629e31986cf9f0fff5a45d1bd9e8c73969664715a1630398851b9f35368ec2d272df5e53a4ddfdefc533a8a3cdf1c1b402e9246f6497dc67";


    @Test
    public void fromSeedSecp256k1(){

        byte[] seed = Utils.hexToBytes(seedHex);

        try {
            ExtendedKey key = Eddsa_ed25519.fromSeedEd25519(seed);
            System.out.println(key);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void deriveChildEd25519(){
        byte[] seed = Utils.hexToBytes(seedHex);
        try {
            ExtendedKey key = Eddsa_ed25519.fromSeedEd25519(seed);
            key = Eddsa_ed25519.deriveChildEd25519(key,0);
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
            String path = "m/44'/501'/0'/0'";
            ExtendedKey key = Eddsa_ed25519.derivePathEd25519(seed,path);
            String privateKey = Utils.bytesToHex(key.getKey());
            System.out.println("privateKey:"+privateKey);
            byte[] publickey = Eddsa_ed25519.getPublicKeyEd25519(key.getKey());
            String address = Base58.encode(publickey);
            System.out.println("address:"+address);
            String addressHex = Utils.bytesToHex(publickey);
            log.info("addressHex:"+addressHex);

            String path1 = "m/44'/501'/1'/0'";
            ExtendedKey key1 = Eddsa_ed25519.derivePathEd25519(seed,path1);
            String privateKey1 = Utils.bytesToHex(key1.getKey());
            System.out.println("privateKey1:"+privateKey1);
            byte[] publickey1 = Eddsa_ed25519.getPublicKeyEd25519(key1.getKey());
            String address1 = Base58.encode(publickey1);
            System.out.println("address1:"+address1);
            String addressHex1 = Utils.bytesToHex(publickey1);
            log.info("addressHex1:"+addressHex1);


        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }


}
