package com.labs339.platform.common;

public class SnowflakeUtil {

    /**
     * 基准时间戳 (2025-01-01 00:00:00 UTC)
     * 可以使用自己的基准时间，建议使用项目启动时间
     */
    private static final long EPOCH = 1609459200000L;

    /**
     * 机器ID位数
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 数据中心ID位数
     */
    private static final long DATACENTER_ID_BITS = 5L;

    /**
     * 序列号位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 机器ID最大值 (2^5 - 1 = 31)
     */
    private static final long MAX_WORKER_ID = (1L << WORKER_ID_BITS) - 1;

    /**
     * 数据中心ID最大值 (2^5 - 1 = 31)
     */
    private static final long MAX_DATACENTER_ID = (1L << DATACENTER_ID_BITS) - 1;

    /**
     * 序列号最大值 (2^12 - 1 = 4095)
     */
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    /**
     * 机器ID左移位数 = 序列号位数 = 12
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 数据中心ID左移位数 = 序列号位数 + 机器ID位数 = 12 + 5 = 17
     */
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间戳左移位数 = 序列号位数 + 机器ID位数 + 数据中心ID位数 = 12 + 5 + 5 = 22
     */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    // ============================== 实例变量 ==============================

    /** 单例实例 */
    private static volatile SnowflakeUtil instance;

    /**
     * 机器ID
     */
    private final long workerId;

    /**
     * 数据中心ID
     */
    private final long datacenterId;

    /**
     * 序列号
     */
    private long sequence = 0L;

    /**
     * 上一次生成ID的时间戳
     */
    private long lastTimestamp = -1L;

    // ============================== 构造方法 ==============================

    /**
     * 构造函数
     *
     * @param workerId 机器ID (0-31)
     * @param datacenterId 数据中心ID (0-31)
     */
    public SnowflakeUtil(long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format(
                    "Worker ID must be between 0 and %d", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format(
                    "Datacenter ID must be between 0 and %d", MAX_DATACENTER_ID));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获取单例实例
     */
    private static SnowflakeUtil getInstance() {
        if (instance == null) {
            synchronized (SnowflakeUtil.class) {
                if (instance == null) {
                    long workerId = getWorkerId();
                    long datacenterId = getDatacenterId();
                    instance = new SnowflakeUtil(workerId, datacenterId);
                }
            }
        }
        return instance;
    }

    // ============================== 静态方法 - 业务直接调用 ==============================

    /**
     * 生成雪花ID - 最常用的方法
     *
     * @return 雪花ID
     */
    public static long nextId() {
        return getInstance().generateId();
    }

    /**
     * 生成雪花ID字符串
     *
     * @return 雪花ID字符串
     */
    public static String nextIdStr() {
        return String.valueOf(nextId());
    }

    /**
     * 批量生成ID
     *
     * @param count 数量
     * @return ID数组
     */
    public static long[] nextIds(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }

        long[] ids = new long[count];
        SnowflakeUtil util = getInstance();
        for (int i = 0; i < count; i++) {
            ids[i] = util.generateId();
        }
        return ids;
    }

    /**
     * 获取当前生成器信息
     *
     * @return 生成器信息
     */
    public static String getInfo() {
        SnowflakeUtil util = getInstance();
        return String.format("SnowflakeUtil{workerId=%d, datacenterId=%d}",
                util.workerId, util.datacenterId);
    }

    // ============================== 核心生成方法 ==============================

    /**
     * 生成ID的核心方法（线程安全）
     */
    private synchronized long generateId() {
        long timestamp = System.currentTimeMillis();

        // 检查时钟回拨
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format(
                    "Clock moved backwards. Last timestamp: %d, current: %d",
                    lastTimestamp, timestamp));
        }

        // 处理序列号
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // 序列号溢出，等待下一毫秒
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // 组装ID
        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 等待下一毫秒
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    // ============================== 自动获取机器标识 ==============================

    /**
     * 自动获取机器ID
     */
    private static long getWorkerId() {
        try {
            java.net.InetAddress addr = java.net.InetAddress.getLocalHost();
            byte[] ipBytes = addr.getAddress();
            return ((long) (ipBytes[ipBytes.length - 1] & 0xFF)) % (MAX_WORKER_ID + 1);
        } catch (Exception e) {
            // 获取失败时使用随机数
            return (long) (Math.random() * MAX_WORKER_ID);
        }
    }

    /**
     * 自动获取数据中心ID
     */
    private static long getDatacenterId() {
        try {
            java.net.InetAddress addr = java.net.InetAddress.getLocalHost();
            byte[] ipBytes = addr.getAddress();
            return ((long) (ipBytes[ipBytes.length - 2] & 0xFF)) % (MAX_DATACENTER_ID + 1);
        } catch (Exception e) {
            // 获取失败时使用随机数
            return (long) (Math.random() * MAX_DATACENTER_ID);
        }
    }


}
