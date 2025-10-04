package utils;

import com.labs339.platform.WalletSignApplication;
import com.labs339.platform.utils.SeedManagementUtils;
import com.labs339.platform.utils.Utils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = WalletSignApplication.class)
public class SeedManagementUtilsTest {

    private static final Logger log = LoggerFactory.getLogger(SeedManagementUtilsTest.class);

    @Test
    public void generateMnemonic(){
        try {
            String mnemonic = SeedManagementUtils.generateMnemonic(128);
            log.info("mnemonic:{}",mnemonic);
            System.out.println(mnemonic);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void mnemonicToSeed(){

        try {
            String mnemonic = SeedManagementUtils.generateMnemonic(128);
            System.out.println("mnemonic:{} "+ mnemonic);

            byte[] bytes =  SeedManagementUtils.mnemonicToSeed(mnemonic,null);
            String seedHex = Utils.bytesToHex(bytes);
            System.out.println("seedHex:{} "+seedHex);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
