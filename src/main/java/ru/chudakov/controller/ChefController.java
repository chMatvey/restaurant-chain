package ru.chudakov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.chudakov.domain.Chef;
import ru.chudakov.service.ChefService;

import java.util.List;

@RestController
public class ChefController {
    private ChefService service;

    @Autowired
    public ChefController(ChefService service) {
        this.service = service;
    }

    @GetMapping("/chefs")
    public List<Chef> getAllChefs() {
        return service.getAllChef();
    }
}
