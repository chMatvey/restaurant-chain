package ru.chudakov.service;

import ru.chudakov.ChefPair;
import ru.chudakov.domain.Chef;

public interface TimetableService {
    public ChefPair[][] generateTimetable(int countDays, int countRestaurant);
}
