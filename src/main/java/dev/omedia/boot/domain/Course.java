package dev.omedia.boot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "U_COURSE_COURSE_NAME", columnNames = "COURSE_NAME"),
                @UniqueConstraint(name = "U_COURSE_COURSE_CODE", columnNames = "COURSE_CODE")
        }
)
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "COURSE_NAME", nullable = false)
    private String name;

    @Column(name = "COURSE_CODE", nullable = false)
    private String code;

    @Column(name = "EFF_FROM", nullable = false)
    private LocalDateTime effectiveFrom;

    @Column(name = "EFF_TO", nullable = false)
    private LocalDateTime effectiveTo;

    @Column(name = "SEMESTER_START_DATE")
    private LocalDate semesterStartDate;

    @Column(name = "SEMESTER_END_DATE")
    private LocalDate semesterEndDate;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    private Set<CourseRegistrationAndGrade> courseRegistrationAndGrades;
}
