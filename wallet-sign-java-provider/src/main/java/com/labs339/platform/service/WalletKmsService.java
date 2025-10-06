package com.labs339.platform.service;

public interface WalletKmsService {

    byte[] loadSeedByKey(String kmsKey);

}
