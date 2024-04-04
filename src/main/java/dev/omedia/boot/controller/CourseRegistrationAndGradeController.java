package dev.omedia.boot.controller;

import dev.omedia.boot.dto.CourseRegistrationAndGradeDTO;
import dev.omedia.boot.service.CourseRegistrationAndGradeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/registration grade")
public class CourseRegistrationAndGradeController {
    private final CourseRegistrationAndGradeService service;

    public CourseRegistrationAndGradeController(CourseRegistrationAndGradeService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "retrieves all course registrations and grades in database")
    public ResponseEntity<Set<CourseRegistrationAndGradeDTO>> findAll() {
        return ResponseEntity.of(
                service.findAll()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "retrieves course registration and grade for corresponding id")
    public ResponseEntity<CourseRegistrationAndGradeDTO> findById(
            @PathVariable(name = "id") long id
    ) {
        return ResponseEntity.of(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "saves a new course registration and grade")
    public ResponseEntity<CourseRegistrationAndGradeDTO> save(@RequestBody CourseRegistrationAndGradeDTO courseRegistrationAndGradeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.save(courseRegistrationAndGradeDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "updates course registration and grade")
    public ResponseEntity<CourseRegistrationAndGradeDTO> update(
            @PathVariable(name = "id") long id,
            @RequestBody CourseRegistrationAndGradeDTO courseRegistrationAndGradeDTO
    ) {
        return ResponseEntity.of(
                Optional.of(service.update(id, courseRegistrationAndGradeDTO))
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "deletes course registration and grade")
    public void delete(
            @PathVariable(name = "id") long id
    ) {
        service.delete(id);
    }
}
