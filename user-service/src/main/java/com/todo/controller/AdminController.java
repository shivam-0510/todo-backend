package com.todo.controller;

import com.todo.dto.StatusUpdateRequest;
import com.todo.dto.UserResponse;
import com.todo.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Get all users - admin only
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    // Delete a user - admin only
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // Activate or deactivate user
    @PatchMapping("/users/{id}/status")
    public UserResponse updateUserStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest statusUpdate) {
        return userService.updateUserStatus(id, statusUpdate.isActive());
    }
}

