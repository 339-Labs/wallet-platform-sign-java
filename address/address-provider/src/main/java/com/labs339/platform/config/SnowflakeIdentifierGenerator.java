package com.labs339.platform.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.labs339.platform.common.SnowflakeUtil;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeIdentifierGenerator implements IdentifierGenerator {
    @Override
    public Long nextId(Object entity) {
        return SnowflakeUtil.nextId();
    }
}
