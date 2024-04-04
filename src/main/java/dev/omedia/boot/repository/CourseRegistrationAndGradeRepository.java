package dev.omedia.boot.repository;

import dev.omedia.boot.domain.CourseRegistrationAndGrade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CourseRegistrationAndGradeRepository extends CrudRepository<CourseRegistrationAndGrade, Long> {
    Set<CourseRegistrationAndGrade> findAllByStudentId(long id);
    Set<CourseRegistrationAndGrade> findAllByCourseId(long id);
    Set<CourseRegistrationAndGrade> findAllByCourseIdAndStudentId(long courseId, long studentId);
}
