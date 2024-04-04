package dev.omedia.boot.service;

import dev.omedia.boot.config.CollegeRestAppConfig;
import dev.omedia.boot.domain.Student;
import dev.omedia.boot.dto.StudentDTO;
import dev.omedia.boot.mapper.StudentMapper;
import dev.omedia.boot.repository.CourseRegistrationAndGradeRepository;
import dev.omedia.boot.repository.StudentRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StudentService {
    private final StudentRepository studentRepo;
    private final CourseRegistrationAndGradeRepository courseRegistrationAndGradeRepo;
    private final CollegeRestAppConfig collegeRestAppConfig;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public StudentService(StudentRepository studentRepo, CourseRegistrationAndGradeRepository courseRegistrationRepo, CollegeRestAppConfig collegeRestAppConfig) {
        this.studentRepo = studentRepo;
        this.courseRegistrationAndGradeRepo = courseRegistrationRepo;
        this.collegeRestAppConfig = collegeRestAppConfig;
    }

    public Optional<StudentDTO> findById(long id) {
        StudentDTO studentDTO = StudentMapper.entityToDTO(studentRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Student with id=%d does not exist", id))));
        return Optional.of(studentDTO);
    }

    public Optional<Set<StudentDTO>> findALl() {
        return Optional.of(
                StudentMapper.entitiesToStudentDTOs(StreamSupport.stream(studentRepo.findAll().spliterator(), false)
                        .collect(Collectors.toSet()))
        );
    }

    public StudentDTO save(StudentDTO studentDTO) {
        try {
            validateStudentDTO(studentDTO);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("u_student_student_mail")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student's mail should be unique", e);
            }
            throw e;
        }
        Student student = StudentMapper.dtoToEntity(studentDTO);
        student.setCourseRegistrationAndGrades(courseRegistrationAndGradeRepo.findAllByCourseId(student.getId()));
        Student save = studentRepo.save(student);
        return StudentMapper.entityToDTO(save);
    }

    public StudentDTO update(long id, StudentDTO studentDTO) {
        try {
            validateStudentDTO(studentDTO);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("u_student_student_mail")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student's mail should be unique", e);
            }
            throw e;
        }
        studentDTO.setId(id);
        Student student = StudentMapper.dtoToEntity(studentDTO);
        student.setCourseRegistrationAndGrades(courseRegistrationAndGradeRepo.findAllByCourseId(student.getId()));
        Student save = studentRepo.save(student);
        return StudentMapper.entityToDTO(save);
    }

    public void delete(long id) {
        studentRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id=" + id + " was not found"));
        studentRepo.deleteById(id);
    }

    private boolean isValidMail(String mail) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(mail);
        return matcher.matches();
    }

    private void validateStudentDTO(StudentDTO studentDTO) throws ValidationException {
        Collection<String> errorMessages = new HashSet<>();

        if (!Objects.isNull(studentDTO.getName()) && StringUtils.isBlank(studentDTO.getName())) {
            errorMessages.add("Student's name can not be blank!");
        } else if (Objects.isNull(studentDTO.getName())){
            errorMessages.add("Student's name can not be null!");
        }

        if (Objects.isNull(studentDTO.getMail())) {
            errorMessages.add("Student's mail can not be null!");
        }else if (!isValidMail(studentDTO.getMail())) {
            errorMessages.add("Student's mail is in invalid format!");
        }

        if (!Objects.isNull(studentDTO.getBirthDate()) && Period.between(studentDTO.getBirthDate().toLocalDate(), LocalDate.now()).toTotalMonths() < collegeRestAppConfig.getStudentMinAge()) {
            errorMessages.add(String.format("Student must be over %d age!", collegeRestAppConfig.getStudentMinAge()));
        } else if (Objects.isNull(studentDTO.getBirthDate())){
            errorMessages.add("Student's birthdate can not be null!");
        }

        if (!Objects.isNull(studentDTO.getCourseRegistrationAndGrades()) && !studentDTO.getCourseRegistrationAndGrades().isEmpty()) {
            errorMessages.add("Student's CourseRegistrationAndGrades should be null or empty!");
        }

        if (errorMessages.isEmpty()) {
            return;
        }

        throw new ValidationException(Strings.join(errorMessages.iterator(), ','));
    }
}
