package com.andrew.pharmapay.payloads;

public class AuthResponse {
    private String authToken;

    public AuthResponse() {
    }

    public AuthResponse(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
