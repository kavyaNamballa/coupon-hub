package com.demo.couponHub.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RefreshTokenResponse {

    private String accessToken;

    public RefreshTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
