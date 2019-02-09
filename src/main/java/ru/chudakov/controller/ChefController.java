package ru.chudakov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.chudakov.controller.form.Error;
import ru.chudakov.controller.form.Result;
import ru.chudakov.controller.form.Success;
import ru.chudakov.domain.Chef;
import ru.chudakov.service.ChefService;

import java.util.List;

@RestController
public class ChefController {
    private final ChefService service;

    @Autowired
    public ChefController(ChefService service) {
        this.service = service;
    }

    @GetMapping("/chef/all")
    public List<Chef> getAllChefs() {
        return service.getAllChef();
    }

    @PostMapping("/chef/add")
    public Chef addChef(@RequestBody Chef chef) {
        return service.addChef(new Chef(
                chef.getFirstName(),
                chef.getMiddleName(),
                chef.getLastName(),
                chef.getDurationWorkDay(),
                chef.getDepartments(),
                chef.getWorkShift(),
                chef.getOperatingMode()
        ));
    }

    @PostMapping("/chef/delete")
    public Result deleteChef(@RequestBody Chef chef) {
        if (service.findChefById(chef.getId()) == null) {
            return new Error("This chef does not exist");
        } else {
            service.deleteChef(chef);
            return new Success<>("Chef was deleted");
        }
    }

    public Result updateChef(@RequestBody Chef chef) {
        if (service.findChefById(chef.getId()) == null) {
            return new Error("This chef does not exist");
        } else {
            return new Success<>(service.updateChef(chef));
        }
    }
}
