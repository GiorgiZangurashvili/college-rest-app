package dev.omedia.boot.repository;

import dev.omedia.boot.domain.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
}
