package com.labs339.platform.utils;

import org.springframework.stereotype.Component;
import org.web3j.crypto.MnemonicUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

@Component
public class SeedManagementUtils {

    /**
     * 1 - 先 生成一个128-256位随机熵 128，160，192，224，256
     * 2 - 以128为例，对随机熵做的SHA-256计算，取前4位当做校验和，加到熵的末尾，得到132位的一个数，然后按每11位做切分，这样就有了12个二进制数，
     * 3 - 这个数字作为助记词在列表中索引，使用索引在BIP39定义的单词表里提取助记词，
     * 4 - 得到12个助记词。12，15，18，21 ，24
     *
     * 生成BIP39助记词
     * @param strength 熵的位数 (128=12词, 256=24词)
     * @return 助记词字符串
     */
    public static String generateMnemonic(int strength) throws Exception {
        if (strength % 32 != 0 || strength < 128 || strength > 256) {
            throw new IllegalArgumentException("Strength must be 128, 160, 192, 224, or 256");
        }

        // 1. 生成随机熵
        byte[] entropy = new byte[strength / 8];
        SecureRandom random = new SecureRandom();
        random.nextBytes(entropy);

        // 2. 计算校验和
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(entropy);
        int checksumLength = strength / 32;

        // 3. 组合熵和校验和
        StringBuilder bits = new StringBuilder();
        for (byte b : entropy) {
            bits.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        for (int i = 0; i < checksumLength; i++) {
            bits.append((hash[0] >> (7 - i)) & 1);
        }

        // 4. 将11位转换为单词索引
        List<String> words = new ArrayList<>();
        for (int i = 0; i < bits.length(); i += 11) {
            int index = Integer.parseInt(bits.substring(i, i + 11), 2);
            words.add(MnemonicUtils.getWords().get(index));
        }

        return String.join(" ", words);
    }

    /**
     * 1 - 使用PBKDF2基于密码的密钥派生函数，以助记词为入参，对助记词进行2028次哈希计算（HMAC-SHA512），得到512位（64字节）的Seed。
     * 2 - seed = PBKDF2(mnemonic, "mnemonic" + passphrase, 2048, HMAC-SHA512)  - passphrase 是可选密码
     *
     * 从助记词生成种子
     * @param mnemonic 助记词
     * @param passphrase 可选的密码短语
     * @return 64字节种子
     */
    public static byte[] mnemonicToSeed(String mnemonic, String passphrase) throws Exception {
        if (passphrase == null) {
            passphrase = "";
        }
        // BIP39标准: PBKDF2-HMAC-SHA512
        String salt = "mnemonic" + passphrase;

        javax.crypto.SecretKeyFactory factory =
                javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

        javax.crypto.spec.PBEKeySpec spec =
                new javax.crypto.spec.PBEKeySpec(
                        mnemonic.toCharArray(),
                        salt.getBytes(StandardCharsets.UTF_8),
                        2048,
                        512
                );

        byte[] seed = factory.generateSecret(spec).getEncoded();
        return seed;
    }

    /**
     * 验证助记词
     */
    public static boolean validateMnemonic(String mnemonic) {
        String[] words = mnemonic.trim().split("\\s+");
        if (words.length != 12 && words.length != 24) {
            return false;
        }

        // 验证每个单词都在词表中
        Set<String> wordSet = new HashSet<>(MnemonicUtils.getWords());
        for (String word : words) {
            if (!wordSet.contains(word.toLowerCase())) {
                return false;
            }
        }

        return true;
    }


}
