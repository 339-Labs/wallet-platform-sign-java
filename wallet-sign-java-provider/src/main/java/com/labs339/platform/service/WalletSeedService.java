package com.labs339.platform.service;

public interface WalletSeedService {

    Long createWallet(Long userId, String mnemonic, String passphrase) throws Exception;

    byte[] getSeed(Long userId, String mnemonic) throws Exception;

    boolean verifyMnemonic(Long userId, String mnemonic);
}
