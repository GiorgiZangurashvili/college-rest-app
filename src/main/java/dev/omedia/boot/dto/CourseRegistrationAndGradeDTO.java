package dev.omedia.boot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Schema(title = "Course registrations and grades", description = "Data of registrations and grades in college database")
public class CourseRegistrationAndGradeDTO {
    @Schema(description = "Registrations and grades id")
    private long id;
    @Schema(description = "Registered student's id")
    private long studentId;
    @Schema(description = "Registered course's id")
    private long courseId;
    @Schema(description = "Student's registration date")
    private LocalDateTime registeredAt;
    @Schema(description = "Student's grade")
    private int grade;
}
