package ru.chudakov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.chudakov.Pair;
import ru.chudakov.domain.Chef;
import ru.chudakov.service.TimetableService;

import java.util.List;

@RestController
public class TimetableController {
    private TimetableService timetableService;

    @Autowired
    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping("/timetable")
    public List<List<Pair>> getTimetable() {
        return timetableService.generateTimetable();
    }
}
