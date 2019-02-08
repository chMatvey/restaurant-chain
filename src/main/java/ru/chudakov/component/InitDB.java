package ru.chudakov.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.chudakov.domain.Chef;
import ru.chudakov.domain.OperatingMode;
import ru.chudakov.domain.WorkShift;
import ru.chudakov.repository.ChefRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class InitDB {
    private ChefRepository chefRepository;

    @Autowired
    public InitDB(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }

    @PostConstruct
    public void init() {
        String[] firstOrMiddleNames = {"Alex", "John", "Benjamin", "Michael", "Thomas", "Morgan"};
        String[] lastName = {"Graham", "Smith", "Williams", "Taylor", "Cooper", "Baker"};
        List<String> departments = new ArrayList<>();
        departments.add("Russian");
        departments.add("Italian");
        departments.add("Japanese");
        List<OperatingMode> operatingModes = new ArrayList<>();
        operatingModes.add(new OperatingMode(5, 2));
        operatingModes.add(new OperatingMode(2, 2));
        List<WorkShift> workShifts = new ArrayList<>();
        workShifts.add(WorkShift.Morning);
        workShifts.add(WorkShift.Evening);
        List<Chef> chefs = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Set<String> departmentForChef = new HashSet<>();
            int countDepartment = random.nextInt(3) + 1;
            for (int j = 0; j < countDepartment; j++) {
                departmentForChef.add(departments.get(j));
            }
            chefs.add(new Chef(
                    firstOrMiddleNames[random.nextInt(6)],
                    firstOrMiddleNames[random.nextInt(6)],
                    lastName[random.nextInt(6)],
                    random.nextInt(7) + 4,
                    departmentForChef,
                    workShifts.get(random.nextInt(2)),
                    operatingModes.get(random.nextInt(2))
            ));
        }
        chefRepository.saveAll(chefs);
    }
}
