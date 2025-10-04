package com.labs339.platform.algorithm;

import lombok.Getter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import java.security.Security;

@Component
public class Seed {


    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    protected static final int HARDENED_BIT = 0x80000000;


    protected static byte[] SEED;

    // kms 初始化 种子
    public Seed() {


    }

}
