package dev.omedia.boot.mapper;

import dev.omedia.boot.domain.Course;
import dev.omedia.boot.dto.CourseDTO;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseMapper {
    public static CourseDTO entityToDTO(Course course) {
        return new CourseDTO(course.getId(), course.getName(), course.getCode(), course.getEffectiveFrom(), course.getEffectiveTo(),
                course.getSemesterStartDate(), course.getSemesterEndDate(),
                CourseRegistrationAndGradeMapper.entitiesToCourseRegistrationAndGradeDTOs(course.getCourseRegistrationAndGrades()));
    }

    public static Collection<CourseDTO> entitiesToCourseDTOs(Collection<Course> courses) {
        return Optional.ofNullable(courses).orElse(Collections.emptySet())
                .stream()
                .map(CourseMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    public static Course dtoToEntity(CourseDTO courseDTO) {
        Course course = new Course();

        course.setId(courseDTO.getId());
        course.setName(courseDTO.getName());
        course.setCode(courseDTO.getCode());
        course.setEffectiveFrom(courseDTO.getEffectiveFrom());
        course.setEffectiveTo(courseDTO.getEffectiveTo());
        course.setSemesterStartDate(courseDTO.getSemesterStartDate());
        course.setSemesterEndDate(courseDTO.getSemesterEndDate());

        return course;
    }
}
