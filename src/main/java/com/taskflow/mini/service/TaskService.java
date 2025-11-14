package com.taskflow.mini.service;

import com.taskflow.mini.model.Task;
import com.taskflow.mini.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task, String userId){
        task.setUserId(userId);
        return taskRepository.save(task);
    }

    public List<Task> getTasksByUser(String userId){
        return taskRepository.findByUserId(userId);
    }

    public Optional<Task> getTaskByIdAndUser(String taskId, String userId){
        return taskRepository.findByIdAndUserId(taskId, userId);
    }

    public Optional<Task> updateTask(String taskId, Task updateTask, String userId){
        Optional<Task> existingTask = taskRepository.findByIdAndUserId(taskId, userId);

        if (existingTask.isPresent()){
            Task task = existingTask.get();
            task.setTitle(updateTask.getTitle());
            task.setDescription(updateTask.getDescription());
            task.setCompleted(updateTask.isCompleted());
            return Optional.of(taskRepository.save(task));
        }

        return Optional.empty();
    }

    public Optional<Task> toggleTaskCompletion(String taskId, String userId){
        Optional<Task> existingTask = taskRepository.findByIdAndUserId(taskId, userId);

        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            task.setCompleted(!task.isCompleted());
            return Optional.of(taskRepository.save(task));
        }

        return Optional.empty();
    }

    public boolean deleteTask(String taskId, String userId){
        Optional<Task> existingTask = taskRepository.findByIdAndUserId(taskId, userId);

        if (existingTask.isPresent()) {
            taskRepository.deleteById(taskId);
            return true;
        }
        return false;
    }
}
