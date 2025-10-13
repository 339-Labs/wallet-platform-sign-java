package com.labs339.platform.addressresolver;

import com.labs339.platform.enums.ChainType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public interface AddressResolverStrategy {

    /**
     * 同一个链有多种地址可配置，为空使用默认的
     * @param address
     * @param addressType
     * @return
     */
    String resolve(String address,String addressType);

    ChainType getChainType();

}
