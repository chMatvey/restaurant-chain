package ru.chudakov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.chudakov.ChefPair;
import ru.chudakov.service.TimetableService;

@RestController
public class TimetableController {
    private final TimetableService timetableService;

    @Autowired
    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping("/timetable")
    public ChefPair[][] getTimetable() {
        return timetableService.generateTimetable(30, 20);
    }
}
