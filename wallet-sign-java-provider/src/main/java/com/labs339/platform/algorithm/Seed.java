package com.labs339.platform.algorithm;

import com.labs339.platform.service.WalletKmsService;
import com.labs339.platform.utils.Utils;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.security.Security;
import java.util.Scanner;

@Component
public class Seed {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    protected static final int HARDENED_BIT = 0x80000000;

    protected static byte[] SEED;

    @Autowired(required = false)
    private WalletKmsService kmsService;

    //todo  kms 初始化 种子
    @PostConstruct
    public void initSeed() {
        SEED = Utils.hexToBytes("b1d8378e098e95b7629e31986cf9f0fff5a45d1bd9e8c73969664715a1630398851b9f35368ec2d272df5e53a4ddfdefc533a8a3cdf1c1b402e9246f6497dc67");
        System.out.println("Default SEED initialized finish ");
    }

    /**
     *  控制台输入后，从KMS动态更新SEED
     */
    public void updateSeedFromConsole() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("[KMS] Enter your KMS key: ");
            String kmsKey = scanner.nextLine();

            if (kmsService == null) {
                System.err.println("[KMS] KmsService not available. Can't update SEED.");
                return;
            }

            byte[] newSeed = kmsService.loadSeedByKey(kmsKey);
            if (newSeed != null && newSeed.length > 0) {
                SEED = newSeed;
                System.out.println("[KMS] SEED successfully updated from KMS!");
            } else {
                System.err.println("[KMS] Failed to load seed from KMS. SEED not updated.");
            }
        }
    }

}
