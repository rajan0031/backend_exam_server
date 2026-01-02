package com.example.java_crud.service;

import com.example.java_crud.model.User;
import com.example.java_crud.repository.UserRepository;
import com.example.java_crud.utils.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.example.java_crud.Exception.CustomException;
import com.example.java_crud.dtos.UserDto.LoginResponse;

import java.util.List;
import com.example.java_crud.dtos.UserDto.ProfileResponse;
import com.example.java_crud.dtos.UserDto.ValidTokenDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {

        var isEmailExists = userRepository.findByEmail(user.getEmail());

        var name = user.getName();
        var email = user.getEmail();
        var password = user.getPassword();
        var role = user.getRole();

        // Validate name is valid name 
        if (name == null || name.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Name is required");
        }

        // Validate email
        if (email == null || email.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (!email.contains("@")) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Invalid email format");
        }

        // Validate password
        if (password == null || password.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Password is required");
        }
        if (password.length() < 6) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters long");
        }

        // Validate role
        if (role == null || role.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Role is required");
        }

        if (isEmailExists != null) {
            throw new CustomException(HttpStatus.CONFLICT, "User already exists in the database");
        }

        // Encrypt password before saving
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        user.setPassword(hashedPassword);

        // Save user if all validations pass
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());

        return userRepository.save(existingUser);
    }

    // login user

    public LoginResponse loginUser(String email, String password, HttpServletResponse response) {

        // Find user by email
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        // Verify password using BCrypt
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // get the token from the jwt

        String token = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        System.out.println("the token and refresh token is " + token + " " + refreshToken);

        // se the refresh token in the httpOnly cookies
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        // only over HTTPS
        cookie.setPath("/");
        // available to all endpoints
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        response.addCookie(cookie);

        System.out.println("Access token: " + token);
        System.out.println("Refresh token set in cookie: " + cookie.getValue());
        // Return response with token
        return new LoginResponse(user.getName(), user.getEmail(), "Login successful", token, refreshToken);
    }

    // api function to get all the details by teh user id

    public ProfileResponse getUserProfile(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        User user = jwtUtils.extractUser(token);
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setId(user.getId());
        profileResponse.setName(user.getName());
        profileResponse.setEmail(user.getEmail());
        profileResponse.setRole(user.getRole());
        return profileResponse;
    }

    // getting the refresh tokne api

    public ValidTokenDto getValidTokens(String refreshToken) {
        try {
            if (refreshToken == null || refreshToken.isBlank()) {
                return new ValidTokenDto(null, null, "Refresh token is empty");
            }

            Claims claims = jwtUtils.validateAndGetClaims(refreshToken);
            String email = claims.getSubject();

            User user = userRepository.findByEmail(email);
            if (user == null) {
                return new ValidTokenDto(null, null, "User not found for this token");
            }

            String newAccessToken = jwtUtils.generateToken(user);
            String newRefreshToken = jwtUtils.generateRefreshToken(user);

            Cookie cookie = new Cookie("refreshToken", newRefreshToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60);

            return new ValidTokenDto(newAccessToken, newRefreshToken, "Token refreshed successfully");

        } catch (JwtException ex) {
            return new ValidTokenDto(null, null, "Invalid or expired refresh token: " + ex.getMessage());
        }
    }
}
