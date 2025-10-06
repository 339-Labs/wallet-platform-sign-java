package com.labs339.platform.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DepthEnum {

    Three(3,"3层"),
    Four(4,"4层"),
    Five(5,"标准五层"),;

    private final int type;
    private final String msg;


}
