//package com.labs339.platform.utils;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.core.util.StrUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.redis.connection.RedisStringCommands;
//import org.springframework.data.redis.connection.ReturnType;
//import org.springframework.data.redis.core.*;
//import org.springframework.data.redis.core.types.Expiration;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
///**
// * spring redis 工具类
// *
// * @author kit.shy
// * @date 2025/11/12
// */
//@Slf4j
//@Component
//public class RedisUtils {
//
//    @Autowired
//    public RedisTemplate redisTemplate;
//
//    /**
//     * 缓存基本的对象，Integer、String、实体类等
//     *
//     * @param key   缓存的键值
//     * @param value 缓存的值
//     */
//    public <T> void set(final String key, final T value) {
//        redisTemplate.opsForValue().set(key, value);
//    }
//
//    /**
//     * 缓存基本的对象，Integer、String、实体类等
//     *
//     * @param key      缓存的键值
//     * @param value    缓存的值
//     * @param timeout  时间
//     * @param timeUnit 时间颗粒度
//     */
//    public <T> void set(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
//        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
//    }
//
//    /**
//     * 设置有效时间
//     *
//     * @param key     Redis键
//     * @param timeout 超时时间
//     * @return true=设置成功；false=设置失败
//     */
//    public boolean expire(final String key, final long timeout) {
//        return expire(key, timeout, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 设置有效时间
//     *
//     * @param key     Redis键
//     * @param timeout 超时时间
//     * @param unit    时间单位
//     * @return true=设置成功；false=设置失败
//     */
//    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
//        return redisTemplate.expire(key, timeout, unit);
//    }
//
//    /**
//     * 获得缓存的基本对象。
//     *
//     * @param key 缓存键值
//     * @return 缓存键值对应的数据
//     */
//    public <T> T get(final String key) {
//        ValueOperations<String, T> operation = redisTemplate.opsForValue();
//        return operation.get(key);
//    }
//
//    /**
//     * 获得缓存的基本对象。出入多少个key，就返回多少个值,包含空的
//     *
//     * @param keys 取值列表，不能为空
//     * @return 缓存键值对应的数据
//     */
//    public <T> List<T> multiGet(final List<String> keys) {
//        return redisTemplate.opsForValue().multiGet(keys);
//    }
//
//    /**
//     * 删除单个对象
//     *
//     * @param key
//     */
//    public boolean delete(final String key) {
//        return redisTemplate.delete(key);
//    }
//
//    /**
//     * 删除集合对象
//     *
//     * @param collection 多个对象
//     * @return
//     */
//    public long delete(final Collection collection) {
//        return redisTemplate.delete(collection);
//    }
//
//    /**
//     * 缓存List数据
//     *
//     * @param key      缓存的键值
//     * @param dataList 待缓存的List数据
//     * @return 缓存的对象
//     */
//    public <T> long setList(final String key, final List<T> dataList) {
//        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
//        return count == null ? 0 : count;
//    }
//
//    /**
//     * 获得缓存的list对象
//     *
//     * @param key 缓存的键值
//     * @return 缓存键值对应的数据
//     */
//    public <T> List<T> multiGetOne(final String key) {
//        return redisTemplate.opsForList().range(key, 0, -1);
//    }
//
//    /**
//     * 缓存Set
//     *
//     * @param key     缓存键值
//     * @param dataSet 缓存的数据
//     * @return 缓存数据的对象
//     */
//    public <T> BoundSetOperations<String, T> setSet(final String key, final Set<T> dataSet) {
//        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
//        Iterator<T> it = dataSet.iterator();
//        while (it.hasNext()) {
//            setOperation.add(it.next());
//        }
//        return setOperation;
//    }
//
//    /**
//     * 获得缓存的set
//     *
//     * @param key
//     * @return
//     */
//    public <T> Set<T> getSet(final String key) {
//        return redisTemplate.opsForSet().members(key);
//    }
//
//    /**
//     * 缓存Map
//     *
//     * @param key
//     * @param dataMap
//     */
//    public <T> void setMap(final String key, final Map<String, T> dataMap) {
//        if (dataMap != null) {
//            redisTemplate.opsForHash().putAll(key, dataMap);
//        }
//    }
//
//    /**
//     * 获得缓存的Map
//     *
//     * @param key
//     * @return
//     */
//    public <T> Map<String, T> getMap(final String key) {
//        return redisTemplate.opsForHash().entries(key);
//    }
//
//    /**
//     * 往Hash中存入数据
//     *
//     * @param key   Redis键
//     * @param hKey  Hash键
//     * @param value 值
//     */
//    public <T> void setMapValue(final String key, final String hKey, final T value) {
//        redisTemplate.opsForHash().put(key, hKey, value);
//    }
//
//    /**
//     * 获取Hash中的数据
//     *
//     * @param key  Redis键
//     * @param hKey Hash键
//     * @return Hash中的对象
//     */
//    public <T> T getMapValue(final String key, final String hKey) {
//        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
//        return opsForHash.get(key, hKey);
//    }
//
//    /**
//     * 删除Hash中的数据
//     *
//     * @param key
//     * @param hkey
//     */
//    public void delMapValue(final String key, final String hkey) {
//        HashOperations hashOperations = redisTemplate.opsForHash();
//        hashOperations.delete(key, hkey);
//    }
//
//    /**
//     * 获取多个Hash中的数据
//     *
//     * @param key   Redis键
//     * @param hKeys Hash键集合
//     * @return Hash对象集合
//     */
//    public <T> List<T> getMultiMapValue(final String key, final Collection<Object> hKeys) {
//        return redisTemplate.opsForHash().multiGet(key, hKeys);
//    }
//
//    /**
//     * 获得缓存的基本对象列表
//     *
//     * @param pattern 字符串前缀
//     * @return 对象列表
//     */
//    public Collection<String> keys(final String pattern) {
//        return redisTemplate.keys(pattern);
//    }
//
//
//    /**
//     *  获取key 对应list数量
//     * @param key
//     * @return
//     */
//    public Long listSize(String key){
//        if(StrUtil.isNotBlank(key)){
//            return redisTemplate.opsForList().size(key);
//        }
//        return null;
//    }
//
//
//    /**
//     * push多个值进去，值都是 1
//     *
//     * @param key
//     * @return
//     */
//    public Long lpush(String key, String value,Long expireTime,TimeUnit timeUnit) {
//        redisTemplate.expire(key,expireTime, timeUnit);
//        return redisTemplate.opsForList().leftPush(key, value);
//    }
//
//
//    /**
//     * 一次性pop出指定数量的数据，rightPop(key,2,TimeUnit.SECONDS)设置了请求超时时间
//     *
//     * @param key  键
//     * @param size 需要取出的元素个数
//     * @return 返回取出的元素集合
//     */
//    public List<String> multiRPopPipeline(String key, int size) throws Exception {
//
//        // 获取当前队列里的值
//        int curSize = Math.toIntExact(redisTemplate.opsForList().size(key));
//        if (curSize == 0) {
//            return Collections.emptyList();
//        }
//        // 判断操作次数
//        List<String> resutls = redisTemplate.executePipelined(new SessionCallback<String>() {
//            @Override
//            public String execute(RedisOperations redisOperations) throws DataAccessException {
//                final int finalSize = Math.toIntExact(Math.min(curSize, size));
//                for (int i = 0; i < finalSize; i++) {
//                    //设置2秒超时
//                    redisOperations.opsForList().rightPop(key);
//                }
//                return null;
//            }
//        });
//
//        return resutls;
//
//    }
//
//    public String rPopOne(String key) {
//        List<String> resultList = Collections.emptyList();
//        try {
//            resultList = multiRPopPipeline(key, 1);
//            if(CollectionUtil.isEmpty(resultList)){
//                return "";
//            }
//            return resultList.get(0);
//        }catch (Exception e) {
//            log.error("rPopOne error key:{} ...", key,e);
//        }
//        return "";
//    }
//
//    //redis加锁
//    public boolean lock(String key, String value, long timeout, TimeUnit timeUnit) {
//        Boolean locked;
//        try {
//            //SET_IF_ABSENT --> NX: Only set the key if it does not already exist.
//            //SET_IF_PRESENT --> XX: Only set the key if it already exist.
//            locked = (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection ->
//                    connection.set(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8),
//                            Expiration.from(timeout, timeUnit), RedisStringCommands.SetOption.SET_IF_ABSENT));
//        } catch (Exception e) {
//            log.error("Lock failed for redis key: {}, value: {}", key, value);
//            locked = false;
//        }
//        return locked != null && locked;
//    }
//
//    public boolean lock(String key, String value,long milliseconds) {
//        Boolean lock = redisTemplate.opsForValue().setIfAbsent(key, value, milliseconds, TimeUnit.MILLISECONDS);
//        return lock;
//    }
//
//    public boolean lock(String key, long milliseconds) {
//        Boolean lock = redisTemplate.opsForValue().setIfAbsent(key, "lock", milliseconds, TimeUnit.MILLISECONDS);
//        return lock;
//    }
//
//    //redis解锁
//    public boolean unlock(String key, String value) {
//        try {
//            //使用lua脚本保证删除的原子性，确保解锁
//            String script = "if redis.call('get', KEYS[1]) == ARGV[1] " +
//                    "then return redis.call('del', KEYS[1]) " +
//                    "else return 0 end";
//            Boolean unlockState = (Boolean) redisTemplate.execute((RedisCallback<Boolean>) connection ->
//                    connection.eval(script.getBytes(), ReturnType.BOOLEAN, 1,
//                            key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8)));
//            return unlockState == null || !unlockState;
//        } catch (Exception e) {
//            log.error("unLock failed for redis key: {}, value: {}", key, value);
//            return false;
//        }
//    }
//
//    public boolean tryLockInSecond(String key, String value, long milliseconds) {
//        try {
//            int retryTime = 5;
//            for (int i = 0; i < retryTime; i++) {
//                Boolean lock = redisTemplate.opsForValue().setIfAbsent(key, value, milliseconds, TimeUnit.MILLISECONDS);
//                if (lock) {
//                    return true;
//                } else {
//                    Thread.sleep(150);
//                }
//            }
//        } catch (Exception e) {
//            log.error("tryLock failed {}", e);
//        }
//        return false;
//    }
//
//}
//
