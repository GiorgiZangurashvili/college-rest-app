package dev.omedia.boot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
@Schema(title = "Course", description = "Course data in college database")
public class CourseDTO {
    @Schema(description = "Course id")
    private long id;
    @Schema(description = "Course name")
    private String name;
    @Schema(description = "Course code")
    private String code;
    @Schema(description = "Course effectiveFrom date")
    private LocalDateTime effectiveFrom;
    @Schema(description = "Course effectiveTo date")
    private LocalDateTime effectiveTo;
    @Schema(description = "Semester start date")
    private LocalDate semesterStartDate;
    @Schema(description = "Semester end date")
    private LocalDate semesterEndDate;
    @Schema(description = "Course registrations and grades")
    private Set<CourseRegistrationAndGradeDTO> courseRegistrationAndGrades;
}
