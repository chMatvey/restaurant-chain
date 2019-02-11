package ru.chudakov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.chudakov.domain.OperatingMode;
import ru.chudakov.repository.OperatingModeRepository;

@Service
public class OperatingModeServiceImpl implements OperatingModeService {
    private final OperatingModeRepository repository;

    @Autowired
    public OperatingModeServiceImpl(OperatingModeRepository repository) {
        this.repository = repository;
    }

    @Override
    public OperatingMode getOperatingModeByCountWorkingDayAndCountDayOff(int countWorkingDay, int countDayOff) {
        return repository.findAllByCountWorkingDayAndCountDayOff(countWorkingDay, countDayOff);
    }
}
