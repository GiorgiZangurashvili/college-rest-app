package dev.omedia.boot.service;

import dev.omedia.boot.domain.Course;
import dev.omedia.boot.dto.CourseDTO;
import dev.omedia.boot.mapper.CourseMapper;
import dev.omedia.boot.repository.CourseRegistrationAndGradeRepository;
import dev.omedia.boot.repository.CourseRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.bind.ValidationException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CourseService {
    private final CourseRepository courseRepo;
    private final CourseRegistrationAndGradeRepository courseRegistrationAndGradeRepo;

    public CourseService(CourseRepository courseRepo, CourseRegistrationAndGradeRepository courseRegistrationAndGradeRepo) {
        this.courseRepo = courseRepo;
        this.courseRegistrationAndGradeRepo = courseRegistrationAndGradeRepo;
    }

    public Optional<Collection<CourseDTO>> findAll() {
        return Optional.of(
                CourseMapper.entitiesToCourseDTOs(StreamSupport.stream(courseRepo.findAll().spliterator(), false)
                        .collect(Collectors.toList()))
        );
    }

    public Optional<CourseDTO> findById(long id) {
        CourseDTO courseDTO = CourseMapper.entityToDTO(courseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Course with id=%d does not exist", id))));
        return Optional.of(courseDTO);
    }

    public CourseDTO save(CourseDTO courseDTO) {
        try {
            validateCourseDTO(courseDTO);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            Collection<String> errorMessages = new HashSet<>();
            if (e.getMessage().contains("u_course_course_name")) {
                errorMessages.add("Course name should be unique!");
            }
            if (e.getMessage().contains("u_course_course_code")) {
                errorMessages.add("Course code should be unique!");
            }
            if (!errorMessages.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, Strings.join(errorMessages.iterator(), ','));
            }
            throw e;
        }
        Course course = CourseMapper.dtoToEntity(courseDTO);
        course.setCourseRegistrationAndGrades(courseRegistrationAndGradeRepo.findAllByCourseId(course.getId()));
        Course save = courseRepo.save(course);
        return CourseMapper.entityToDTO(save);
    }

    public CourseDTO update(long id, CourseDTO courseDTO) {
        try {
            validateCourseDTO(courseDTO);
        } catch (ValidationException e) {
            System.out.println("EXCEPTION");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            Collection<String> errorMessages = new HashSet<>();

            if (e.getMessage().contains("u_course_course_code")){
                errorMessages.add("Course code should be unique!");
            }

            if (e.getMessage().contains("u_course_course_name")) {
                errorMessages.add("Course name should be unique!");
            }
            if (!errorMessages.isEmpty()) {
                String message = Strings.join(errorMessages.iterator(), ',');
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
            }
            throw e;
        }
        courseDTO.setId(id);
        Course course = CourseMapper.dtoToEntity(courseDTO);
        course.setCourseRegistrationAndGrades(courseRegistrationAndGradeRepo.findAllByCourseId(course.getId()));
        Course save = courseRepo.save(course);
        return CourseMapper.entityToDTO(save);
    }

    public void delete(long id) {
        courseRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course with id=" + id + " was not found"));
        courseRepo.deleteById(id);
    }

    private void validateCourseDTO(CourseDTO courseDTO) throws ValidationException {
        Collection<String> errorMessages = new HashSet<>();

        if (!Objects.isNull(courseDTO.getName()) && StringUtils.isBlank(courseDTO.getName())) {
            errorMessages.add("Course's name can not be blank!");
        } else if (Objects.isNull(courseDTO.getName())) {
            errorMessages.add("Course's name can not be null!");
        }

        if (Objects.isNull(courseDTO.getCode()) || StringUtils.isBlank(courseDTO.getCode())) {
            errorMessages.add("Course's code can not be null or blank!");
        }

        if (Objects.isNull(courseDTO.getEffectiveFrom())) {
            errorMessages.add("Course's effectiveFrom date can not be null!");
        }

        if (Objects.isNull(courseDTO.getEffectiveTo())) {
            errorMessages.add("Course's effectiveTo date can not be null!");
        }

        if (!Objects.isNull(courseDTO.getEffectiveFrom()) && !Objects.isNull(courseDTO.getEffectiveTo())) {
            if (courseDTO.getEffectiveFrom().isAfter(courseDTO.getEffectiveTo())) {
                errorMessages.add("Course's effectiveFrom date should not be after effectiveTo!");
            }

            if (Objects.isNull(courseDTO.getSemesterStartDate()) || Objects.isNull(courseDTO.getSemesterEndDate())) {
                errorMessages.add("Semester startDate or endDate can not be null");
            } else if (courseDTO.getSemesterStartDate().isAfter(courseDTO.getSemesterEndDate())){
                errorMessages.add("Semester startDate can not be after endDate");
            }else {
                if (courseDTO.getSemesterStartDate().isBefore(courseDTO.getEffectiveFrom().toLocalDate())) {
                    errorMessages.add("Semester startDate can not be before course's effectiveFrom date");
                }
                if (courseDTO.getSemesterStartDate().isAfter(courseDTO.getEffectiveTo().toLocalDate())) {
                    errorMessages.add("Semester startDate can not be after course's effectiveTo date");
                }
                if (courseDTO.getSemesterEndDate().isBefore(courseDTO.getEffectiveFrom().toLocalDate())) {
                    errorMessages.add("Semester end date can not be before course's effectiveFrom date");
                }
                if (courseDTO.getSemesterEndDate().isAfter(courseDTO.getEffectiveTo().toLocalDate())) {
                    errorMessages.add("Semester end date can not be after course's effectiveTo date");
                }
            }
        }

        if (!Objects.isNull(courseDTO.getCourseRegistrationAndGrades()) && !courseDTO.getCourseRegistrationAndGrades().isEmpty()) {
            errorMessages.add("CourseRegistrationAndGrades should be null or empty!");
        }

        if (errorMessages.isEmpty()) {
            return;
        }

        throw new ValidationException(Strings.join(errorMessages.iterator(), ','));
    }
}
