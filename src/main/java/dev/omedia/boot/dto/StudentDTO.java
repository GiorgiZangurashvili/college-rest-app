package dev.omedia.boot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Schema(title = "Student", description = "Data stored about student in college database")
public class StudentDTO {
    @Schema(description = "Student's id")
    private long id;
    @Schema(description = "Student's name")
    private String name;
    @Schema(description = "Student's mail")
    private String mail;
    @Schema(description = "Student's birthdate")
    private LocalDateTime birthDate;
    @Schema(description = "Student's registered courses and grades")
    private Set<CourseRegistrationAndGradeDTO> courseRegistrationAndGrades;
}
