package com.andrew.pharmapay.payloads;

public class AuthResponse {
    private String authToken;
    private String userRole;

    public AuthResponse() {
    }

    public AuthResponse(String authToken, String userRole) {
        this.authToken = authToken;
        this.userRole = userRole;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
