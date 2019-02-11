package ru.chudakov.repository;

import org.springframework.data.repository.CrudRepository;
import ru.chudakov.domain.OperatingMode;

public interface OperatingModeRepository extends CrudRepository<OperatingMode, Integer> {
    OperatingMode findAllByCountWorkingDayAndCountDayOff(int countWorkingDay, int countDayOff);
}
