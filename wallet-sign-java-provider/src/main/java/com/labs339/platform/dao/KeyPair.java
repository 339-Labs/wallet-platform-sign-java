package com.labs339.platform.dao;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class KeyPair implements Serializable {

    private int index;

    private String publicKeyHex;

    private String coin;

    private byte[] privateKey;

}
