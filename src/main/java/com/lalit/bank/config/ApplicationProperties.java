package com.lalit.bank.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private final Jwt jwt = new Jwt();

    public Jwt getJwt() { return jwt; }

    public static class Jwt {
        private String secret;
        private long expirationMs = 86400000;

        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        public long getExpirationMs() { return expirationMs; }
        public void setExpirationMs(long expirationMs) { this.expirationMs = expirationMs; }
    }
}
