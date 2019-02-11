package ru.chudakov.repository;

import org.springframework.data.repository.CrudRepository;
import ru.chudakov.domain.Department;

public interface DepartmentRepository extends CrudRepository<Department, Integer> {
    Department findByName(String name);
}
