package com.labs339.platform.utils;

import cn.hutool.core.util.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("redis.is.cluster")
    private boolean isCluster = false;

    @Value("")
    private String node = "redis://127.0.0.1:6379";

    @Value("")
    private String password = "Shy00000";

    @Bean
    public RedissonClient redissonClient() {

        Config config = new Config();
        if (!isCluster) {
            config.useSingleServer()
                    .setAddress(node)
                    .setPassword(password);
        }else {

            String [] nodes = node.split(",");
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            for (String node : nodes) {
                clusterServersConfig.addNodeAddress(prefixAddress(node));
            }
            if (StrUtil.isNotBlank(password)) {
                clusterServersConfig.setPassword(password);
            }
        }
        return Redisson.create(config);
    }

    private String prefixAddress(String address) {
        if (StrUtil.isNotBlank(address) &&  !address.startsWith("redis:")) {
            return "redis://"+address;
        }
        return address;
    }

}
