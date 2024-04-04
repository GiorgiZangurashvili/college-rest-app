package dev.omedia.boot.mapper;

import dev.omedia.boot.domain.CourseRegistrationAndGrade;
import dev.omedia.boot.dto.CourseRegistrationAndGradeDTO;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CourseRegistrationAndGradeMapper {
    public static CourseRegistrationAndGradeDTO entityToDTO(CourseRegistrationAndGrade courseRegistrationAndGrade) {
        return new CourseRegistrationAndGradeDTO(courseRegistrationAndGrade.getId(), courseRegistrationAndGrade.getStudentId(),
                courseRegistrationAndGrade.getCourseId(), courseRegistrationAndGrade.getRegisteredAt(), courseRegistrationAndGrade.getGrade());
    }

    public static Set<CourseRegistrationAndGradeDTO> entitiesToCourseRegistrationAndGradeDTOs(Set<CourseRegistrationAndGrade> courseRegistrationAndGrades) {
        return Optional.ofNullable(courseRegistrationAndGrades).orElse(Collections.emptySet())
                .stream()
                .map(CourseRegistrationAndGradeMapper::entityToDTO)
                .collect(Collectors.toSet());
    }

    public static CourseRegistrationAndGrade dtoToEntity(CourseRegistrationAndGradeDTO courseRegistrationAndGradeDTO) {
        var courseRegistrationAndGrade = new CourseRegistrationAndGrade();

        courseRegistrationAndGrade.setId(courseRegistrationAndGradeDTO.getId());
        courseRegistrationAndGrade.setCourseId(courseRegistrationAndGradeDTO.getCourseId());
        courseRegistrationAndGrade.setStudentId(courseRegistrationAndGradeDTO.getStudentId());
        courseRegistrationAndGrade.setRegisteredAt(courseRegistrationAndGradeDTO.getRegisteredAt());

        return courseRegistrationAndGrade;
    }
}
