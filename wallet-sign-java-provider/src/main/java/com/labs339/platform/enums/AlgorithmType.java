package com.labs339.platform.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlgorithmType {

    ECDSA_K1("ECDSA","secp256k1"),
    ECDSA_R1("ECDSA","secp256r1"),
    EDDSA_25519("EDDSA","ed25519"),
    EDDSA_448("EDDSA","ed448"),;

    private final String type;
    private final String curve;


}
