package com.todo.controller;

import com.todo.dto.CreateTodoRequest;
import com.todo.dto.TodoResponse;
import com.todo.dto.UpdateTodoRequest;
import com.todo.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(
            @Valid @RequestBody CreateTodoRequest request,
            HttpServletRequest servletRequest) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        TodoResponse response = todoService.createTodo(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getTodosByLoggedInUser(HttpServletRequest servletRequest) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TodoResponse> todos = todoService.getTodosByUserId(userId);
        if (todos.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable Long todoId,
                                                    HttpServletRequest servletRequest) {
        TodoResponse todo = todoService.getTodoById(todoId);
        return ResponseEntity.ok(todo);
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable Long todoId,
            @Valid @RequestBody UpdateTodoRequest request,
            HttpServletRequest servletRequest) {

        TodoResponse updated = todoService.updateTodo(todoId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Map<String, String>> deleteTodo(
            @PathVariable Long todoId,
            HttpServletRequest servletRequest) {

        todoService.deleteTodo(todoId);
        return ResponseEntity.ok(Map.of("message", "Todo deleted successfully"));
    }
}
