//package com.labs339.platform.utils;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class RedisBloomFilter {
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    private static final String BLOOM_KEY = "bloom:filter";
//    private static final int BIT_SIZE = 1 << 24; // ~16M bits
//
//    private int[] hash(String value) {
//        int h1 = value.hashCode();
//        int h2 = h1 ^ (h1 >>> 16);
//        return new int[]{
//                Math.abs(h1 % BIT_SIZE),
//                Math.abs(h2 % BIT_SIZE)
//        };
//    }
//
//    /**
//     * 添加数据
//     * @param value
//     */
//    public void add(String value) {
//        for (int pos : hash(value)) {
//            redisTemplate.opsForValue().setBit(BLOOM_KEY, pos, true);
//        }
//    }
//
//    /**
//     * 是否存在过滤器中
//     * @param value
//     * @return
//     */
//    public boolean isExists(String value) {
//        for (int pos : hash(value)) {
//            if (!Boolean.TRUE.equals(redisTemplate.opsForValue().getBit(BLOOM_KEY, pos))) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//
//}
