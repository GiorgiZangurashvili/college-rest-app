package dev.omedia.boot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
public class CourseRegistrationAndGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "STUDENT_ID", insertable = false, updatable = false)
    private long studentId;

    @ManyToOne
    @JoinColumn(name = "STUDENT_ID", foreignKey = @ForeignKey(name = "FK_COURSE_REGISTRATION_STUDENT_ID"))
    private Student student;


    @Column(name = "COURSE_ID", insertable = false, updatable = false)
    private long courseId;

    @ManyToOne
    @JoinColumn(name = "COURSE_ID", foreignKey = @ForeignKey(name = "FK_COURSE_REGISTRATION_COURSE_ID"))
    private Course course;

    @Column(name = "REGISTER_DATE_TIME")
    private LocalDateTime registeredAt;

    @Column(name = "COURSE_GRADE")
    private int grade;
}
