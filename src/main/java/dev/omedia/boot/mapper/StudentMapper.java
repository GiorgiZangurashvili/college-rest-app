package dev.omedia.boot.mapper;

import dev.omedia.boot.domain.Student;
import dev.omedia.boot.dto.StudentDTO;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentMapper {
    public static StudentDTO entityToDTO(Student student) {
        return new StudentDTO(student.getId(), student.getName(), student.getMail(), student.getBirthDate(),
                CourseRegistrationAndGradeMapper.entitiesToCourseRegistrationAndGradeDTOs(student.getCourseRegistrationAndGrades()));
    }

    public static Set<StudentDTO> entitiesToStudentDTOs(Set<Student> students) {
        return Optional.ofNullable(students).orElse(Collections.emptySet())
                .stream()
                .map(StudentMapper::entityToDTO)
                .collect(Collectors.toSet());
    }

    public static Student dtoToEntity(StudentDTO studentDTO) {
        Student student = new Student();

        student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setMail(studentDTO.getMail());
        student.setBirthDate(studentDTO.getBirthDate());

        return student;
    }
}
