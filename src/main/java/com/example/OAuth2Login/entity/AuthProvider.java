package com.example.OAuth2Login.entity;

public enum AuthProvider {
    LOCAL("local"),
    GITHUB("github"),
    GOOGLE("google");

    final String name;

    AuthProvider(String name) {
        this.name = name;
    }

   static public AuthProvider value(String name) {
        for (AuthProvider provider : AuthProvider.values()) {
            if (provider.name.equals(name)) {
                return provider;
            }
        }
        return null;
    }
}
