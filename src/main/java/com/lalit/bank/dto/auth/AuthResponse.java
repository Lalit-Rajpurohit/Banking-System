package com.lalit.bank.dto.auth;

public class AuthResponse {
    private String token;
    private String tokenType = "Bearer";
    private UserInfo user;

    public AuthResponse() {}
    public AuthResponse(String token, String tokenType, UserInfo user) {
        this.token = token;
        this.tokenType = tokenType != null ? tokenType : "Bearer";
        this.user = user;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public UserInfo getUser() { return user; }
    public void setUser(UserInfo user) { this.user = user; }

    public static AuthResponseBuilder builder() { return new AuthResponseBuilder(); }
    public static class AuthResponseBuilder {
        private String token;
        private String tokenType = "Bearer";
        private UserInfo user;
        public AuthResponseBuilder token(String token) { this.token = token; return this; }
        public AuthResponseBuilder tokenType(String tokenType) { this.tokenType = tokenType; return this; }
        public AuthResponseBuilder user(UserInfo user) { this.user = user; return this; }
        public AuthResponse build() { return new AuthResponse(token, tokenType, user); }
    }

    public static class UserInfo {
        private Long id;
        private String name;
        private String email;
        private String role;

        public UserInfo() {}
        public UserInfo(Long id, String name, String email, String role) {
            this.id = id; this.name = name; this.email = email; this.role = role;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public static UserInfoBuilder builder() { return new UserInfoBuilder(); }
        public static class UserInfoBuilder {
            private Long id;
            private String name, email, role;
            public UserInfoBuilder id(Long id) { this.id = id; return this; }
            public UserInfoBuilder name(String name) { this.name = name; return this; }
            public UserInfoBuilder email(String email) { this.email = email; return this; }
            public UserInfoBuilder role(String role) { this.role = role; return this; }
            public UserInfo build() { return new UserInfo(id, name, email, role); }
        }
    }
}
