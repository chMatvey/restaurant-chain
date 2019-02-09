package ru.chudakov.component;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.chudakov.domain.Chef;
import ru.chudakov.domain.Department;
import ru.chudakov.domain.OperatingMode;
import ru.chudakov.domain.WorkShift;
import ru.chudakov.repository.ChefRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class InitDB {
    private ChefRepository chefRepository;
    private static final int countChefs = 200;

    @Autowired
    public InitDB(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }

    @PostConstruct
    public void init() {
        String[] firstOrMiddleNames = {"Alex", "John", "Benjamin", "Michael", "Thomas", "Morgan"};
        String[] lastName = {"Graham", "Smith", "Williams", "Taylor", "Cooper", "Baker"};
        List<Department> departments = new ArrayList<>();
        departments.add(new Department("Russian"));
        departments.add(new Department("Italian"));
        departments.add(new Department("Japanese"));
        List<OperatingMode> operatingModes = new ArrayList<>();
        operatingModes.add(new OperatingMode(5, 2));
        operatingModes.add(new OperatingMode(2, 2));
        List<WorkShift> workShifts = new ArrayList<>();
        workShifts.add(WorkShift.Morning);
        workShifts.add(WorkShift.Evening);
        List<Chef> chefs = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < countChefs; i++) {
            int countDepartmentForChef = random.nextInt(3) + 1;
            Set<Integer> indexesDepartmentForChefs = new HashSet<>();
            while (countDepartmentForChef != 0) {
                int index = random.nextInt(3);
                if (!indexesDepartmentForChefs.contains(index)) {
                    indexesDepartmentForChefs.add(index);
                    countDepartmentForChef--;
                }
            }
            Set<Department> departmentsForChef = new HashSet<>();
            for (Integer index : indexesDepartmentForChefs) {
                departmentsForChef.add(departments.get(index));
            }
            chefs.add(new Chef(
                    firstOrMiddleNames[random.nextInt(6)],
                    firstOrMiddleNames[random.nextInt(6)],
                    lastName[random.nextInt(6)],
                    random.nextInt(7) + 4,
                    departmentsForChef,
                    workShifts.get(random.nextInt(2)),
                    operatingModes.get(random.nextInt(2))
            ));
        }
        chefRepository.saveAll(chefs);
    }
}
