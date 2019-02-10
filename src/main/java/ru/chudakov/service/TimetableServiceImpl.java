package ru.chudakov.service;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.chudakov.ChefPair;
import ru.chudakov.domain.Chef;
import ru.chudakov.domain.Department;
import ru.chudakov.domain.WorkShift;
import ru.chudakov.repository.ChefRepository;
import ru.chudakov.repository.DepartmentRepository;

import java.util.*;

@Service
public class TimetableServiceImpl implements TimetableService {
    private final ChefRepository chefRepository;
    private final DepartmentRepository departmentRepository;

    private final static int durationWorkRestaurant = 14;

    @Autowired
    public TimetableServiceImpl(ChefRepository chefRepository, DepartmentRepository departmentRepository) {
        this.chefRepository = chefRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public ChefPair[][] generateTimetable(int countDays, int countRestaurant) {
        List<Chef> chefs = Lists.newArrayList(chefRepository.findAll());
        List<Department> departments = Lists.newArrayList(departmentRepository.findAll());
        ChefPair[][] result = new ChefPair[countRestaurant][countDays * departments.size()];
        Comparator<Chef> comparatorByDurationWorkDays = Comparator.comparing(Chef::getDurationWorkDay);
        chefs.sort(comparatorByDurationWorkDays);
        Map<Chef, Integer> busyChefs = new HashMap<>();
//        List<Integer> countChefsWithCountryQualification = new ArrayList<>(departments.size());
//        for (int i = 0; i < departments.size(); i++) {
//            countChefsWithCountryQualification.add(0);
//        }
//
//        for (Chef chef : chefs) {
//            int indexDepartment = 0;
//            while (indexDepartment != departments.size()) {
//                if (chef.getDepartments().contains(departments.get(indexDepartment))) {
//                    countChefsWithCountryQualification.set(indexDepartment,
//                            countChefsWithCountryQualification.get(indexDepartment) + 1);
//                }
//                indexDepartment++;
//            }
//        }
        for (int i = 0; i < countRestaurant; i++) {
            int indexDepartment = 0;
            while (containEmptyDay(result[i])) {
                ChefPair chefPair;
                if (result[i][indexDepartment] == null) {
                    chefPair = getPairForOneDayAndOneDepartment(chefs, departments.get(indexDepartment % 3));
                } else if (result[i][indexDepartment].isNotFullPair()) {
                    chefPair = getPairForOneDayAndOneDepartment(chefs, departments.get(indexDepartment % 3),
                            result[i][indexDepartment].getNotNullChef());
                } else {
                    indexDepartment++;
                    continue;
                }
                if (chefPair == null) {
                    return null;
                }
                int countWorkDaysFirstChef = chefPair.getFirstChef().getOperatingMode().getCountWorkingDay();
                int countWorkDaysSecondChef = chefPair.getSecondChef().getOperatingMode().getCountWorkingDay();
                int min = Math.min(countWorkDaysFirstChef, countWorkDaysSecondChef);
                int max = Math.max(countWorkDaysFirstChef, countWorkDaysSecondChef);
//                for (int j = 0; j < max; j++) {
//                    if (j < min && i + j < countDays) {
//                        result[i + j][indexDepartment] = chefPair;
//                    } else if (i + j < countDays) {
//                        result[i + j][indexDepartment] = getNotFullChefPair(chefPair);
//                    }
//                }
                for (int j = indexDepartment; j < max * departments.size() + indexDepartment; j += departments.size()) {
                    if (j < min * departments.size() + indexDepartment && j < countDays * departments.size()) {
                        result[i][j] = chefPair;
                    } else if (j < countDays * departments.size()) {
                        result[i][j] = getNotFullChefPair(chefPair);
                    }
                }
                indexDepartment++;
            }
        }
        return result;
    }

    private boolean containEmptyDay(ChefPair[] timetableForOneDay) {
        for (ChefPair chefPair : timetableForOneDay) {
            if (chefPair == null) {
                return true;
            } else if (chefPair.isNotFullPair()) {
                return true;
            }
        }
        return false;
    }

    private boolean canWorkInOneDay(Chef firstChef, Chef secondChef, Department department) {
        return firstChef.getDurationWorkDay() + secondChef.getDurationWorkDay() == durationWorkRestaurant &&
                firstChef.getWorkShift() != secondChef.getWorkShift() &&
                firstChef.getDepartments().contains(department) &&
                secondChef.getDepartments().contains(department);
    }

    private ChefPair getPairForOneDayAndOneDepartment(List<Chef> chefs, Department department) {
        int indexHead = 0;
        int indexTail = chefs.size() - 1;
        Chef firstChef;
        Chef secondChef;
        for (int j = indexHead; j < chefs.size(); j++) {
            firstChef = chefs.get(j);
            for (int k = indexTail; k > j; k--) {
                secondChef = chefs.get(k);
                if (canWorkInOneDay(firstChef, secondChef, department)) {
                    return new ChefPair(firstChef, secondChef);
                }
            }
        }
        return null;
    }

    private ChefPair getPairForOneDayAndOneDepartment(List<Chef> chefs, Department department, Chef chef) {
        if (chef == null) {
            return null;
        }
        int indexHead = 0;
        int indexTail = chefs.size() - 1;
        for (int j = indexHead; j < chefs.size() / 2 + 1; j++) {
            if (canWorkInOneDay(chef, chefs.get(j), department)) {
                return new ChefPair(chef, chefs.get(j));
            } else if (canWorkInOneDay(chef, chefs.get(indexTail - j), department)) {
                return new ChefPair(chef, chefs.get(indexTail - j));
            }
        }
        return null;
    }

    private ChefPair getNotFullChefPair(ChefPair pair) {
        int countWorkDaysForFirstChef = pair.getFirstChef().getOperatingMode().getCountWorkingDay();
        int countWorkDaysForSecondChef = pair.getSecondChef().getOperatingMode().getCountWorkingDay();
        if (countWorkDaysForFirstChef < countWorkDaysForSecondChef) {
            return new ChefPair(null, pair.getSecondChef());
        } else if (countWorkDaysForFirstChef == countWorkDaysForSecondChef) {
            return pair;
        } else {
            return new ChefPair(pair.getFirstChef(), null);
        }
    }
}
