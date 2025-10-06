package com.labs339.platform.service.impl;

import com.labs339.platform.service.WalletKmsService;
import com.labs339.platform.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WalletKmsServiceImpl implements WalletKmsService {
    @Override
    public byte[] loadSeedByKey(String kmsKey) {
        // 模拟通过 KMS Key 获取加密的 seed
        log.info("[KMS] Loading seed from KMS using key: " + kmsKey);
        // 实际环境下在这里调用 KMS API（AWS KMS, HSM, Vault...）
        if ("real-key".equals(kmsKey)) {
            return Utils.hexToBytes("0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef");
        }
        return null;
    }
}
