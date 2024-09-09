package app.task.services;

import app.task.entity.Task;
import app.task.entity.status.Status;
import app.task.payload.CreateTaskDto;
import app.task.payload.UpdateTaskDto;
import app.task.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        // Given
        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setTitle("New Task");
        createTaskDto.setDescription("Task Description");
        Task savedTask = new Task();
        savedTask.setTitle("New Task");

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // When
        ResponseEntity<?> response = taskService.createTask(createTaskDto);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testGetAllTasks() {
        // Given
        Task task1 = new Task();
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setTitle("Task 2");

        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(tasks);

        // When
        ResponseEntity<?> response = taskService.getAllTasks();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Task> returnedTasks = (List<Task>) response.getBody();
        assertEquals(2, Objects.requireNonNull(returnedTasks).size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById() {
        // Given
        Long taskId = 1L;
        Task task = new Task();
        task.setTitle("Task 1");
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // When
        ResponseEntity<?> response = taskService.getTaskById(taskId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Task returnedTask = (Task) response.getBody();
        assertEquals("Task 1", Objects.requireNonNull(returnedTask).getTitle());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void testGetTaskByIdNotFound() {
        // Given
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When
        ResponseEntity<?> response = taskService.getTaskById(taskId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void testUpdateTaskById() {
        // Given
        Long taskId = 1L;
        Task task = new Task();
        task.setTitle("Old Task");
        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setTitle("Updated Task");
        updateTaskDto.setStatus(Status.IN_PROGRESS.name());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        ResponseEntity<?> response = taskService.updateTaskById(taskId, updateTaskDto);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testDeleteTaskById() {
        // Given
        Long taskId = 1L;
        doNothing().when(taskRepository).deleteById(taskId);

        // When
        ResponseEntity<?> response = taskService.deleteTaskById(taskId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskRepository, times(1)).deleteById(taskId);
    }
}
