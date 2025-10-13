package com.labs339.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.dao.entity.AddressPoolModel;
import com.labs339.platform.dao.entity.WalletAddressModel;
import com.labs339.platform.dao.mapper.AddressPoolMapper;
import com.labs339.platform.dao.mapper.WalletAddressMapper;
import com.labs339.platform.service.WalletAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WalletAddressServiceImpl extends ServiceImpl<WalletAddressMapper, WalletAddressModel> implements WalletAddressService {

    @Autowired
    private AddressPoolMapper addressPoolMapper;

    @Override
    public CommonResponse GetUserAddress(Long userId, String chainName, String chainType) {

        WalletAddressModel walletAddressModel = this.getOne(new LambdaQueryWrapper<WalletAddressModel>()
                .eq(WalletAddressModel::getUserId,userId)
                .eq(WalletAddressModel::getChainType,chainType));

        if (walletAddressModel == null) {

            AddressPoolModel addressPoolModel = addressPoolMapper.selectOne(new LambdaQueryWrapper<AddressPoolModel>()
                    .eq(AddressPoolModel::getUsed,false)
                    .eq(AddressPoolModel::getChainType,chainType)
                    .last("limit 1"));
            if (addressPoolModel == null) {
                new Exception().printStackTrace();
            }

            WalletAddressModel walletAddress = new WalletAddressModel();
            walletAddress.setUserId(userId);
            walletAddress.setChainType(chainType);
            walletAddress.setAddress(addressPoolModel.getAddress());

            this.save(walletAddress);
            addressPoolModel.setUsed(true);
            addressPoolMapper.updateById(addressPoolModel);
            return CommonResponse.success(addressPoolModel.getAddress());
        }

        return CommonResponse.success(walletAddressModel.getAddress());
    }

    @Override
    public CommonResponse GetAddressInfo(String address, String chainName, String chainType) {



        return null;
    }

}
