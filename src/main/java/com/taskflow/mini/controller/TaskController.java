package com.taskflow.mini.controller;

import com.taskflow.mini.model.Task;
import com.taskflow.mini.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication){
        String userEmail = authentication.getName();
        Task createdTask = taskService.createTask(task, userEmail);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(Authentication authentication){
        String userEmail = authentication.getName();
        List<Task> tasks = taskService.getTasksByUser(userEmail);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable String id, Authentication authentication){
        String userEmail = authentication.getName();
        Optional<Task> task = taskService.getTaskByIdAndUser(id, userEmail);

        if (task.isPresent()){
            return ResponseEntity.ok(task.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea no encontrada");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable String id,
                                        @RequestBody Task task,
                                        Authentication authentication){
        String userEmail = authentication.getName();
        Optional<Task> updatedTask = taskService.updateTask(id, task, userEmail);

        if (updatedTask.isPresent()) {
            return ResponseEntity.ok(updatedTask.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea no encontrada");
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggleTaskCompletion(@PathVariable String id,
                                                  Authentication authentication){
        String userEmail = authentication.getName();
        Optional<Task> task = taskService.toggleTaskCompletion(id, userEmail);

        if (task.isPresent()){
            return ResponseEntity.ok(task.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea no encontrada");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id, Authentication authentication){
        String userEmail = authentication.getName();
        boolean deleted = taskService.deleteTask(id, userEmail);

        if (deleted){
            return ResponseEntity.ok("Tarea eliminada exitosamente");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarea no encontrada");
    }
}
