package app.task.services;

import app.task.entity.Task;
import app.task.entity.status.Status;
import app.task.payload.CreateTaskDto;
import app.task.payload.UpdateTaskDto;
import app.task.repositories.TaskRepository;
import app.task.services.interfaces.ITaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService implements ITaskService {
    private final TaskRepository taskRepository;

    public ResponseEntity<?> createTask(CreateTaskDto createTaskDto) {
        try {
            Task task = new Task();
            task.setTitle(createTaskDto.getTitle());
            task.setDescription(createTaskDto.getDescription());
            task.setDueDate(createTaskDto.getDueDate());
            log.info("saving task {}", task);

            Task savedTask = taskRepository.save(task);

            return successMessage(savedTask);
        } catch (Exception e) {
            return internalServerError(e);
        }
    }

    public ResponseEntity<?> getAllTasks() {
        try {
            List<Task> tasks = taskRepository.findAll();
            log.info("Tasks returned");
            return ResponseEntity.status(HttpStatus.OK).body(tasks);
        } catch (Exception e) {
            return internalServerError(e);
        }
    }

    public ResponseEntity<?> getTaskById(Long id) {
        try {
            Optional<Task> optionalTask = taskRepository.findById(id);

            if (optionalTask.isEmpty()) {
                return notFoundError("Task not found");
            }

            log.info("Task found");
            return ResponseEntity.status(HttpStatus.OK).body(optionalTask.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity<?> updateTaskById(Long id, UpdateTaskDto updateTaskDto) {
        try {
            Optional<Task> optionalTask = taskRepository.findById(id);
            if (optionalTask.isEmpty()) {
                return notFoundError("Task not found");
            }

            Task task = optionalTask.get();
            task.setTitle(updateTaskDto.getTitle());
            task.setDescription(updateTaskDto.getDescription());
            task.setDueDate(updateTaskDto.getDueDate());
            task.setStatus(Status.valueOf(updateTaskDto.getStatus()));
            log.info("Updating task {}", task);

            Task savedTask = taskRepository.save(task);

            return successMessage(savedTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity<?> deleteTaskById(Long id) {
        try {
            taskRepository.deleteById(id);
            log.info("Task deleted {}", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Task deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private ResponseEntity<?> internalServerError(Exception e) {
        log.error("Error occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    private ResponseEntity<?> notFoundError(String error) {
        log.error(error);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    private ResponseEntity<?> successMessage(Task task) {
        log.info("Task saved successfully");
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }
}