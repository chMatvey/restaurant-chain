package ru.chudakov.service;

import ru.chudakov.domain.OperatingMode;

public interface OperatingModeService {
    public OperatingMode getOperatingModeByCountWorkingDayAndCountDayOff(int countWorkingDay, int countDayOff);
}
