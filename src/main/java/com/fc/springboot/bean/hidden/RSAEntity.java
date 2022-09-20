package com.fc.springboot.bean.hidden;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;


@Data
public class RSAEntity implements Serializable {

    private BigInteger N;

    //私钥
    private BigInteger E;

    //公钥
    private BigInteger D;
}
