package app.task.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateTaskDto {

    @NotNull
    @NotBlank
    private String title;

    private String description;

    private LocalDateTime dueDate;

    @Pattern(regexp = "OPEN|IN_PROGRESS|COMPLETED", message = "Invalid status. Allowed values are OPEN, IN_PROGRESS and COMPLETED")
    private String status;

}
