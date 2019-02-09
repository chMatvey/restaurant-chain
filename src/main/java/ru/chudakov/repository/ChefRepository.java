package ru.chudakov.repository;

import org.springframework.data.repository.CrudRepository;
import ru.chudakov.domain.Chef;

public interface ChefRepository extends CrudRepository<Chef, Integer> {
}
