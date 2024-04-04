package dev.omedia.boot.controller;

import dev.omedia.boot.dto.CourseDTO;
import dev.omedia.boot.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;


    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @Operation(summary = "retrieves all courses in database")
    public ResponseEntity<Collection<CourseDTO>> findAll() {
        return ResponseEntity.of(courseService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "retrieves a course with corresponding id")
    public ResponseEntity<CourseDTO> findById(
            @PathVariable(name = "id") long id
    ) {
        return ResponseEntity.of(courseService.findById(id));
    }

    @PostMapping
    @Operation(summary = "saves a new course in database")
    public ResponseEntity<CourseDTO> save(@Valid @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.save(courseDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "updates course information")
    public ResponseEntity<CourseDTO> update(
            @PathVariable(name = "id") long id,
            @RequestBody CourseDTO courseDTO
    ) {
        return ResponseEntity.of(
                Optional.of(courseService.update(id, courseDTO))
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "deletes course from database")
    public void delete(
            @PathVariable(name = "id") long id
    ) {
        courseService.delete(id);
    }
}
