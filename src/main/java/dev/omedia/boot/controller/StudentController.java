package dev.omedia.boot.controller;

import dev.omedia.boot.domain.Student;
import dev.omedia.boot.dto.StudentDTO;
import dev.omedia.boot.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @Operation(summary = "retrieves all students")
    public ResponseEntity<Set<StudentDTO>> findAll() {
        return ResponseEntity.of(studentService.findALl());
    }

    @GetMapping("/{id}")
    @Operation(summary = "retrieves a student for corresponding id")
    public ResponseEntity<StudentDTO> findById(
            @PathVariable(name = "id") long id
            ) {
        return ResponseEntity.of(studentService.findById(id));
    }

    @PostMapping
    @Operation(summary = "saves a new student to database")
    public ResponseEntity<StudentDTO> save(@Valid @RequestBody StudentDTO studentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.save(studentDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "updates student information")
    public ResponseEntity<StudentDTO> update(
            @PathVariable(name = "id") long id,
            @RequestBody StudentDTO studentDTO
            ) {
        return ResponseEntity.of(
                Optional.of(studentService.update(id, studentDTO))
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "deletes student")
    public void delete(
            @PathVariable(name = "id") long id
    ) {
        studentService.delete(id);
    }
}
