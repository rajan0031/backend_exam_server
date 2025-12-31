package com.example.java_crud.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.java_crud.model.User;
import com.example.java_crud.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.java_crud.dtos.UserDto.LoginResponse;
import com.example.java_crud.dtos.UserDto.ProfileResponse;
import com.example.java_crud.dtos.UserDto.RefreshTokenRequest;
import com.example.java_crud.dtos.UserDto.ValidTokenDto;
import com.example.java_crud.dtos.UserDto.LoginRequestDto;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return service.createUser(user);
    }

    // start ---- this is just for the simple check
    @GetMapping("/ping")
    public String pingServer() {
        return "Server is running!";
    }

    // end ----- this is the just for the simple check

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    // update the user details here
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return service.updateUser(id, user);
    }

    // login the user
    // @PostMapping("/login")
    // @postMapping("/login")
    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequestDto loginRequestDto,
            HttpServletResponse httpServletResponse) {
        return service.loginUser(loginRequestDto.getEmail(), loginRequestDto.getPassword(), httpServletResponse);
    }

    // get the profile of the users
    @GetMapping("/profile")
    public ProfileResponse GetUserProfile(HttpServletRequest httpServletRequest) {
        return service.getUserProfile(httpServletRequest);
    }

    // controller for the getting the newtoken and the refreshtoken from the refresh
    // token
    @PostMapping("/refresh-token")
    public ResponseEntity<ValidTokenDto> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        ValidTokenDto validTokens = service.getValidTokens(refreshTokenRequest.getRefreshToken());
        // System.out.println("the refresh token" + ref);

        System.out.println(validTokens.getToken());
        return ResponseEntity.ok(validTokens);
    }

}
