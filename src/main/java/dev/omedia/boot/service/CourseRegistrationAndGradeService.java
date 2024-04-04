package dev.omedia.boot.service;

import dev.omedia.boot.config.CollegeRestAppConfig;
import dev.omedia.boot.domain.Course;
import dev.omedia.boot.domain.CourseRegistrationAndGrade;
import dev.omedia.boot.domain.Student;
import dev.omedia.boot.dto.CourseRegistrationAndGradeDTO;
import dev.omedia.boot.mapper.CourseRegistrationAndGradeMapper;
import dev.omedia.boot.repository.CourseRegistrationAndGradeRepository;
import dev.omedia.boot.repository.CourseRepository;
import dev.omedia.boot.repository.StudentRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.bind.ValidationException;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CourseRegistrationAndGradeService {
    private final CourseRegistrationAndGradeRepository courseRegistrationAndGradeRepo;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final CollegeRestAppConfig collegeRestAppConfig;

    public CourseRegistrationAndGradeService(CourseRegistrationAndGradeRepository courseRegistrationAndGradeRepo, CourseRepository courseRepository, StudentRepository studentRepository, CollegeRestAppConfig collegeRestAppConfig) {
        this.courseRegistrationAndGradeRepo = courseRegistrationAndGradeRepo;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.collegeRestAppConfig = collegeRestAppConfig;
    }

    public Optional<Set<CourseRegistrationAndGradeDTO>> findAll() {
        return Optional.of(
                CourseRegistrationAndGradeMapper.entitiesToCourseRegistrationAndGradeDTOs(
                        StreamSupport.stream(courseRegistrationAndGradeRepo
                                .findAll()
                                .spliterator(), false)
                                .collect(Collectors.toSet())
                )
        );
    }

    public Optional<CourseRegistrationAndGradeDTO> findById(long id) {
        var dto = CourseRegistrationAndGradeMapper.entityToDTO(courseRegistrationAndGradeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("CourseRegistrationAndGrade with id=%d does not exist", id))));
        return Optional.of(dto);
    }

    public CourseRegistrationAndGradeDTO save(CourseRegistrationAndGradeDTO courseRegistrationAndGradeDTO) {
        try {
            validateOnSave(courseRegistrationAndGradeDTO);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return getCourseRegistrationAndGradeDTO(courseRegistrationAndGradeDTO);
    }

    public CourseRegistrationAndGradeDTO update(long id, CourseRegistrationAndGradeDTO courseRegistrationAndGradeDTO) {
        try {
            validateOnUpdate(courseRegistrationAndGradeDTO);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        courseRegistrationAndGradeDTO.setId(id);
        return getCourseRegistrationAndGradeDTO(courseRegistrationAndGradeDTO);
    }

    private CourseRegistrationAndGradeDTO getCourseRegistrationAndGradeDTO(CourseRegistrationAndGradeDTO courseRegistrationAndGradeDTO) {
        var courseRegistrationAndGrade = CourseRegistrationAndGradeMapper.dtoToEntity(courseRegistrationAndGradeDTO);
        Optional<Course> course = courseRepository.findById(courseRegistrationAndGradeDTO.getCourseId());
        if (course.isPresent()) {
            courseRegistrationAndGrade.setCourse(course.get());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Course with id=%d does not exist!", courseRegistrationAndGradeDTO.getCourseId()));
        }
        Optional<Student> student = studentRepository.findById(courseRegistrationAndGradeDTO.getStudentId());
        if (student.isPresent()) {
            courseRegistrationAndGrade.setStudent(student.get());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Student with id=%d does not exist!", courseRegistrationAndGradeDTO.getCourseId()));
        }
        var save = courseRegistrationAndGradeRepo.save(courseRegistrationAndGrade);
        return CourseRegistrationAndGradeMapper.entityToDTO(save);
    }

    public void delete(long id) {
        courseRegistrationAndGradeRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CourseRegistrationAndGrade with id=" + id + " was not found"));
        courseRegistrationAndGradeRepo.deleteById(id);
    }

    private void validateOnSave(CourseRegistrationAndGradeDTO courseRegistrationAndGradeDTO) throws ValidationException {
        Collection<String> errorMessages = new HashSet<>();

        validate(courseRegistrationAndGradeDTO, errorMessages);

        if (courseRegistrationAndGradeDTO.getGrade() != collegeRestAppConfig.getMinimumGradeToGet()) {
            errorMessages.add("While saving new RegistrationAndGrade, grade should equal minimum=" + collegeRestAppConfig.getMinimumGradeToGet());
        }

        if (errorMessages.isEmpty()) {
            return;
        }

        throw new ValidationException(Strings.join(errorMessages.iterator(), ','));
    }

    private void validateOnUpdate(CourseRegistrationAndGradeDTO courseRegistrationAndGradeDTO) throws ValidationException {
        Set<CourseRegistrationAndGrade> everyCourseRegistration = courseRegistrationAndGradeRepo.findAllByCourseIdAndStudentId(courseRegistrationAndGradeDTO.getCourseId(), courseRegistrationAndGradeDTO.getStudentId());
        if (everyCourseRegistration.isEmpty()) {
            throw new ValidationException("Such CourseRegistrationAndGrade does not exist");
        }

        Collection<String> errorMessages = new HashSet<>();

        if (everyCourseRegistration.size() == collegeRestAppConfig.getMaximumAllowedRegistration()) {
            errorMessages.add(String.format("Student has taken this already %d times, which is maximum times that is allowed", collegeRestAppConfig.getMaximumAllowedRegistration()));
        }

        for (var courseRegistration : everyCourseRegistration) {
            if (courseRegistration.getGrade() >= collegeRestAppConfig.getMinimumGradeToPass()) {
                errorMessages.add("Student has already passed this course, so can not register again");
                break;
            }
        }

        if (courseRegistrationAndGradeDTO.getGrade() < collegeRestAppConfig.getMinimumGradeToGet()) {
            errorMessages.add(String.format("Student can not get less than %d points in course", collegeRestAppConfig.getMinimumGradeToGet()));
        }

        if (courseRegistrationAndGradeDTO.getGrade() > collegeRestAppConfig.getMaximumGradeToGet()) {
            errorMessages.add(String.format("Student can not get more than %d points in course", collegeRestAppConfig.getMaximumGradeToGet()));
        }

        if (errorMessages.isEmpty()) {
            return;
        }

        throw new ValidationException(Strings.join(errorMessages.iterator(), ','));
    }

    private void validate(CourseRegistrationAndGradeDTO courseRegistrationAndGradeDTO, Collection<String> errorMessages) {
        var course = courseRepository.findById(courseRegistrationAndGradeDTO.getCourseId());
        var student = studentRepository.findById(courseRegistrationAndGradeDTO.getStudentId());

        if (course.isEmpty()) {
            errorMessages.add("Course with id=" + courseRegistrationAndGradeDTO.getCourseId() + " does not exist");
        }
        if (student.isEmpty()){
            errorMessages.add("Student with id=" + courseRegistrationAndGradeDTO.getStudentId() + " does not exist");
        }
        if (course.isPresent() && student.isPresent()){
            var registeredAt = courseRegistrationAndGradeDTO.getRegisteredAt();
            Period period = Period.between(registeredAt.toLocalDate(), course.get().getEffectiveFrom().toLocalDate());
            if (period.isNegative() && Math.abs(period.getDays()) > collegeRestAppConfig.getAllowedDaysToRegisterAfterStart()) {
                errorMessages.add(String.format("Student can not register on course after %d days have passed after effectiveFrom date", collegeRestAppConfig.getAllowedDaysToRegisterAfterStart()));
            } else if (period.getDays() > collegeRestAppConfig.getAllowedDaysToRegisterBeforeStart()) {
                errorMessages.add(String.format("Student can not register on course %d days before effectiveFrom date", collegeRestAppConfig.getAllowedDaysToRegisterBeforeStart()));
            }
        }
    }
}
