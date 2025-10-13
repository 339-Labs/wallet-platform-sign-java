package com.labs339.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.dao.entity.AddressNonceModel;
import com.labs339.platform.dao.mapper.AddressNonceMapper;
import com.labs339.platform.service.AddressNonceService;
import org.springframework.stereotype.Service;

@Service
public class AddressNonceServiceImpl extends ServiceImpl<AddressNonceMapper, AddressNonceModel> implements AddressNonceService {


    @Override
    public CommonResponse GetAddressUpdateNonce(String address,String chainName,Integer chainId) {

        // todo 需要加分布式锁
        AddressNonceModel addressNonceModel = this.getOne(new LambdaQueryWrapper<AddressNonceModel>()
                .eq(AddressNonceModel::getAddress, address)
                .eq(AddressNonceModel::getChainId, chainId)
                .eq(AddressNonceModel::getChainName, chainName));
        if (addressNonceModel == null) {
            addressNonceModel = new AddressNonceModel();
            addressNonceModel.setAddress(address);
            addressNonceModel.setChainId(chainId);
            addressNonceModel.setChainName(chainName);
            addressNonceModel.setNonce(0);
        }else {
            addressNonceModel.setNonce(addressNonceModel.getNonce() + 1);
        }
        this.updateById(addressNonceModel);

        return CommonResponse.success(addressNonceModel.getNonce());
    }

}
