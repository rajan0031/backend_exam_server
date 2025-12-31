package com.example.java_crud.utils;

public class OpenEndpointsConfig {

    // here i am defining the endpints which are free from Authorizations
    public static final String[] PUBLIC_ENDPOINTS = {
            "/users/**",
            "/users/register",
            "/users",
            "/users/ping",
            "/db/**",
            "/users/refresh-token"
    };
}
