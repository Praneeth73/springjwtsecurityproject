package com.springjwtsecurity.springjwtscurity.Util;

public class AuthenticationResponse {

    public final String jwtToken;

    public AuthenticationResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
