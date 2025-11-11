package com.labs339.platform.addressresolver;

import com.labs339.platform.common.ByteUtils;
import com.labs339.platform.enums.ChainType;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.Base58;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SolanaAddressResolver implements AddressResolverStrategy {
    @Override
    public String resolve(String addressHex,String addressType) {
        byte[] bytes = ByteUtils.hexToBytes(addressHex);
        String address = null;
        try {
            address = Base58.encode(bytes);
        }catch (Exception e){
            log.error("evm public to address error , hex {}，msg {}",addressHex,e.getMessage(),e);
        }
        return address;
    }

    @Override
    public ChainType getChainType() {
        return ChainType.Solana;
    }

}
