package com.labs339.platform.dao;

import lombok.Data;

@Data
public class ExtendedKey {

    private final byte[] key;           // 32字节私钥或公钥
    private final byte[] chainCode;     // 32字节链码
    /**
     * 在 BIP32 中 depth 表示当前密钥（无论是父密钥还是子密钥）在密钥树中的层级深度
     * 根密钥（主密钥）深度为 0。
     * 第一级子密钥（例如 m/0'）的深度为 1。
     * 第二级子密钥（例如 m/0'/0'）的深度为 2。
     * 依此类推，深度每增加一级，路径中的每个索引就代表该层级的子密钥。
     *
     * m/purpose'/coin_type'/account'/change/address_index 例子：m/44'/0'/0'/0/0        ' 符号表示硬化派生
     * depth = 5：根节点 m 为深度 0，第一个 44' 为深度 1，0' 为深度 2，第二个 0' 为深度 3，0 为深度 4，最后一个 0 为深度 5。
     */
    private final int depth;
    /**
     * childNumber 是在某一层级下，表示当前密钥在该层级中的位置的索引
     * childNumber 对应路径中的最后一部分（通常是 address_index 或 change，即最后的子密钥索引）。
     * 在路径 m/44'/0'/0'/0/0 和 m/44'/0'/0'/0/1 中，childNumber 为 0 和 1 分别表示同一层级的两个不同的子密钥
     */
    private final int childNumber;
    private final byte[] parentFingerprint;

    public ExtendedKey(byte[] key, byte[] chainCode, int depth, int childNumber, byte[] parentFingerprint) {
        this.key = key;
        this.chainCode = chainCode;
        this.depth = depth;
        this.childNumber = childNumber;
        this.parentFingerprint = parentFingerprint;
    }

}
