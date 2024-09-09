package app.task.services.interfaces;

import app.task.payload.CreateTaskDto;
import app.task.payload.UpdateTaskDto;
import org.springframework.http.ResponseEntity;

public interface ITaskService {

    ResponseEntity<?> createTask(CreateTaskDto createTaskDto);

    ResponseEntity<?> getAllTasks();

    ResponseEntity<?> getTaskById(Long id);

    ResponseEntity<?> updateTaskById(Long id, UpdateTaskDto updateTaskDto);

    ResponseEntity<?> deleteTaskById(Long id);

}