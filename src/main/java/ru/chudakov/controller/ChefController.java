package ru.chudakov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.chudakov.controller.form.Error;
import ru.chudakov.controller.form.Result;
import ru.chudakov.controller.form.Success;
import ru.chudakov.domain.Chef;
import ru.chudakov.domain.Department;
import ru.chudakov.service.ChefService;
import ru.chudakov.service.DepartmentService;
import ru.chudakov.service.OperatingModeService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@RestController
public class ChefController {
    private final ChefService chefService;
    private final DepartmentService departmentService;
    private final OperatingModeService operatingModeService;

    @Autowired
    public ChefController(ChefService chefService,
                          DepartmentService departmentService,
                          OperatingModeService operatingModeService) {
        this.chefService = chefService;
        this.departmentService = departmentService;
        this.operatingModeService = operatingModeService;
    }

    @GetMapping("/chef/all")
    public List<Chef> getAllChefs() {
        return chefService.getAllChef();
    }

    @PostMapping("/chef/add")
    public Chef addChef(@RequestBody Chef chef) {
        Set<Department> departmentSet = new HashSet<>();
        for (Department department : chef.getDepartments()) {
            departmentSet.add(departmentService.getDepartmentByName(department.getName()));
        }
        return chefService.addChef(new Chef(
                chef.getFirstName(),
                chef.getMiddleName(),
                chef.getLastName(),
                chef.getDurationWorkDay(),
                departmentSet,
                chef.getWorkShift(),
                operatingModeService.getOperatingModeByCountWorkingDayAndCountDayOff(
                        chef.getOperatingMode().getCountWorkingDay(), chef.getOperatingMode().getCountDayOff()
                )
        ));
    }

    @PostMapping("/chef/update")
    public Result updateChef(@RequestBody Chef chef) {
        if (chefService.findChefById(chef.getId()) == null) {
            return new Error("This chef does not exist");
        } else {
            Set<Department> departmentSet = new HashSet<>();
            for (Department department : chef.getDepartments()) {
                departmentSet.add(departmentService.getDepartmentByName(department.getName()));
            }
            chef.setDepartments(departmentSet);
            chef.setOperatingMode(operatingModeService.getOperatingModeByCountWorkingDayAndCountDayOff(
                    chef.getOperatingMode().getCountWorkingDay(), chef.getOperatingMode().getCountDayOff()
            ));
            return new Success<>(chefService.addChef(chef));
        }
    }

    @PostMapping("/chef/delete")
    public Result deleteChef(@RequestBody Chef chef) {
        if (chefService.findChefById(chef.getId()) == null) {
            return new Error("This chef does not exist");
        } else {
            chef.setDepartments(new HashSet<>());
            chefService.addChef(chef);
            chefService.deleteChef(chef);
            return new Success<>("Chef was deleted");
        }
    }
}
