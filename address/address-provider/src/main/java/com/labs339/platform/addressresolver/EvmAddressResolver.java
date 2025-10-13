package com.labs339.platform.addressresolver;

import com.labs339.platform.enums.ChainType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EvmAddressResolver implements AddressResolverStrategy {
    @Override
    public String resolve(String address,String addressType) {
        return "";
    }

    @Override
    public ChainType getChainType() {
        return ChainType.Ether;
    }
}
