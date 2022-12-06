package com.wangkisa.commerce.security;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class JwtModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String accessTokenExpirationDate;
    private String refreshToken;
    private String refreshTokenExpirationDate;

}