package com.labs339.platform.req;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserTokenReq implements Serializable {

    private String userId;

    private String tokenName;

}
