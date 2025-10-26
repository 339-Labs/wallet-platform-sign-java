package com.labs339.platform.addressresolver;

import com.labs339.platform.common.ByteUtils;
import com.labs339.platform.enums.ChainType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EvmAddressResolver implements AddressResolverStrategy {
    @Override
    public String resolve(String addressHex,String addressType) {
        byte[] bytes = ByteUtils.hexToBytes(addressHex);
        String address = null;
        try {
            address = publicKeyToAddress(bytes);
        }catch (Exception e){
            log.error("evm public to address error , hex {}",addressHex,e.getMessage());
        }
        return address;
    }

    @Override
    public ChainType getChainType() {
        return ChainType.Ethereum;
    }


    public static String publicKeyToAddress(byte[] publicKey) throws Exception {
        if (publicKey.length != 65) {
            throw new IllegalArgumentException("Public key must be 65 bytes (uncompressed)");
        }

        // 1. 去掉第一个字节（0x04前缀）
        byte[] publicKeyWithoutPrefix = new byte[64];
        System.arraycopy(publicKey, 1, publicKeyWithoutPrefix, 0, 64);

        // 2. Keccak256哈希
        byte[] hash = ByteUtils.keccak256(publicKeyWithoutPrefix);

        // 3. 取后20字节
        byte[] address = new byte[20];
        System.arraycopy(hash, 12, address, 0, 20);

        // 4. 转换为十六进制并添加0x前缀
        return "0x" + ByteUtils.bytesToHex(address);
    }


}
