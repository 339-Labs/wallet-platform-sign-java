package com.labs339.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.labs339.platform.dao.entity.AddressPoolModel;
import com.labs339.platform.dao.mapper.AddressPoolMapper;
import com.labs339.platform.service.AddressPoolService;
import org.springframework.stereotype.Service;

@Service
public class AddressPoolServiceImpl extends ServiceImpl<AddressPoolMapper, AddressPoolModel> implements AddressPoolService {
}
