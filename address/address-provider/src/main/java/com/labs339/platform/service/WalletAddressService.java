package com.labs339.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.dao.entity.WalletAddressModel;

public interface WalletAddressService extends IService<WalletAddressModel> {

    CommonResponse GetUserAddress(Long userId, String chainName, String chainType);

    CommonResponse GetAddressInfo(String address,String chainName,String chainType);



}
