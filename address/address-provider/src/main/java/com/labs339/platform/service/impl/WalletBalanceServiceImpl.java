package com.labs339.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.dao.entity.WalletBalanceModel;
import com.labs339.platform.dao.mapper.WalletBalanceMapper;
import com.labs339.platform.req.UserTokenReq;
import com.labs339.platform.rsp.UserTokenRsp;
import com.labs339.platform.service.WalletBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WalletBalanceServiceImpl extends ServiceImpl<WalletBalanceMapper, WalletBalanceModel> implements WalletBalanceService {
    @Override
    public CommonResponse GetUserToken(UserTokenReq req) {

        List<WalletBalanceModel> walletBalanceModelList = this.list(new LambdaQueryWrapper<WalletBalanceModel>()
                .eq(WalletBalanceModel::getUserId, req.getUserId())
                .eq(!StringUtils.isEmpty(req.getTokenName()),WalletBalanceModel::getTokenName, req.getTokenName()));

        List<UserTokenRsp> tokenRsps = walletBalanceModelList.stream().map(s->{
            UserTokenRsp userTokenRsp = new UserTokenRsp();
            userTokenRsp.setTokenName(s.getTokenName());
            userTokenRsp.setTotalAmount(s.getTotalAmount());
            userTokenRsp.setAvailableAmount(s.getAvailableAmount());
            userTokenRsp.setLockAmount(s.getLockAmount());
            return userTokenRsp;
        }).collect(Collectors.toList());

        return CommonResponse.success(tokenRsps);
    }

    @Override
    public CommonResponse UpdateWalletAmount(WalletBalanceModel model) {
        // todo 需要根据用户 id 和 tokenName 加锁




        return null;
    }
}
