package ru.chudakov.service;

import ru.chudakov.Pair;
import ru.chudakov.domain.Chef;

import java.util.List;

public interface TimetableService {
    public List<List<Pair>> generateTimetable();
}
