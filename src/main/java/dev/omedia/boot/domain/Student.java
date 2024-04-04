package dev.omedia.boot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "U_STUDENT_STUDENT_MAIL", columnNames = "STUDENT_MAIL")
})
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "STUDENT_NAME", nullable = false)
    private String name;

    @Column(name = "STUDENT_MAIL", nullable = false)
    private String mail;

    @Column(name = "BIRTH_DATE", nullable = false)
    private LocalDateTime birthDate;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    private Set<CourseRegistrationAndGrade> courseRegistrationAndGrades;
}
