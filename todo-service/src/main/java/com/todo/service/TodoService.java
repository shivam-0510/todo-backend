package com.todo.service;

import com.todo.dto.CreateTodoRequest;
import com.todo.dto.TodoResponse;
import com.todo.dto.UpdateTodoRequest;
import com.todo.exception.ResourceNotFoundException;
import com.todo.model.Todo;
import com.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public TodoResponse createTodo(CreateTodoRequest request,String userId) {
        // TODO: Add user validation via user-service API call if needed

        Todo todo = new Todo();
        todo.setUserId(userId);
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setPriority(request.getPriority());
        todo.setDueDate(request.getDueDate());
        todo.setStatus("PENDING");
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());

        Todo saved = todoRepository.save(todo);
        return mapToResponse(saved);
    }

    public List<TodoResponse> getTodosByUserId(String userId) {
        List<Todo> todos = todoRepository.findByUserId(userId);
        return todos.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public TodoResponse getTodoById(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo with id " + todoId + " not found"));
        return mapToResponse(todo);
    }

    public TodoResponse updateTodo(Long todoId, UpdateTodoRequest request) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo with id " + todoId + " not found"));

        if (request.getTitle() != null) todo.setTitle(request.getTitle());
        if (request.getDescription() != null) todo.setDescription(request.getDescription());
        if (request.getStatus() != null) todo.setStatus(request.getStatus());
        if (request.getPriority() != null) todo.setPriority(request.getPriority());
        if (request.getDueDate() != null) todo.setDueDate(request.getDueDate());

        todo.setUpdatedAt(LocalDateTime.now());
        Todo updated = todoRepository.save(todo);
        return mapToResponse(updated);
    }

    public void deleteTodo(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo with id " + todoId + " not found"));
        todoRepository.delete(todo);
    }

    private TodoResponse mapToResponse(Todo todo) {
        TodoResponse todoResponse=new TodoResponse();
        todoResponse.setId(todo.getId());
        todoResponse.setDescription(todo.getDescription());
        todoResponse.setTitle(todo.getTitle());
        todoResponse.setPriority(todo.getPriority());
        todoResponse.setDueDate(todo.getDueDate());
        todoResponse.setCreatedAt(todo.getCreatedAt());
        todoResponse.setUpdatedAt(todo.getUpdatedAt());
        todoResponse.setStatus(todo.getStatus());

        return todoResponse;
    }
}

