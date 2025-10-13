package com.labs339.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.dao.entity.AddressNonceModel;

public interface AddressNonceService extends IService<AddressNonceModel> {

    CommonResponse GetAddressUpdateNonce(String address,String chainName,Integer chainId);


}
