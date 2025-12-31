package com.example.java_crud.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class UserDto {

    public static class getAllUserDto {
        public int id;
        public String name;
        public String email;

        // getters and setters goes here

        // all etters goes here
        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        // all setters goes here

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

    // this is login response here

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class LoginResponse {
        private String name;
        private String email;
        private String message;
        private String token;
        private String refreshToken;
    }

    // this is the loginrequestobj

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class LoginRequestDto {
        private String email;
        private String password;
    }

    // user profile response
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ProfileResponse {
        private Long id;
        private String name;
        private String email;
        private String role;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ValidTokenDto {
        private String token;
        private String refreshToken;
        private String message;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class RefreshTokenRequest {
        private String refreshToken;

    }

}
