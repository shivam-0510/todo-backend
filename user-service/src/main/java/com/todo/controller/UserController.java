package com.todo.controller;

import com.todo.dto.UserResponse;
import com.todo.dto.UserRequest;
import com.todo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get current user's profile
    @GetMapping("/me")
    public UserResponse getMe(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUserByUsername(username);
    }

    // Update current user's profile
    @PutMapping("/me")
    public UserResponse updateMe(Authentication authentication, @RequestBody UserRequest updateRequest) {
        String username = authentication.getName();
        UserResponse user = userService.getUserByUsername(username);
        return userService.updateUser(user.getId(), updateRequest);
    }
}


