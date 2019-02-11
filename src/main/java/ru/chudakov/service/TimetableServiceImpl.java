package ru.chudakov.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.chudakov.ChefPair;
import ru.chudakov.domain.Chef;
import ru.chudakov.domain.Department;
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
        for (Chef chef : chefs) {
            busyChefs.put(chef, 0);
        }
        for (int i = 0; i < countRestaurant; i++) {
            int indexDepartment = 0;
            while (containEmptyDay(result[i])) {
                ChefPair chefPair;
                if (result[i][indexDepartment] == null) {
                    chefPair = getPairForOneDayAndOneDepartment(chefs, departments.get(indexDepartment % 3), busyChefs);
                } else if (result[i][indexDepartment].isNotFullPair()) {
                    chefPair = getPairForOneDayAndOneDepartment(chefs, departments.get(indexDepartment % 3), busyChefs,
                            result[i][indexDepartment].getNotNullChef());
                } else {
                    if (indexDepartment % 3 == 0) {
                        for (Map.Entry<Chef, Integer> entry : busyChefs.entrySet()) {
                            if (entry.getValue() != 0) {
                                busyChefs.put(entry.getKey(), entry.getValue() - 1);
                            }
                        }
                    }
                    indexDepartment++;
                    continue;
                }
                if (chefPair == null) {
                    return null;
                }
                int countWorkDaysFirstChef = chefPair.getFirstChef().getOperatingMode().getCountWorkingDay();
                if (busyChefs.get(chefPair.getFirstChef()) != 0) {
                    countWorkDaysFirstChef = countWorkDaysFirstChef -
                            (chefPair.getFirstChef().getOperatingMode().getCountWorkAndOffDay() -
                                    busyChefs.get(chefPair.getFirstChef()));
                }
                int countWorkDaysSecondChef = chefPair.getSecondChef().getOperatingMode().getCountWorkingDay();
                if (busyChefs.get(chefPair.getSecondChef()) != 0) {
                    countWorkDaysSecondChef = countWorkDaysSecondChef -
                            (chefPair.getSecondChef().getOperatingMode().getCountWorkAndOffDay() -
                                    busyChefs.get(chefPair.getSecondChef()));
                }
                int min = Math.min(countWorkDaysFirstChef, countWorkDaysSecondChef);
                int max = Math.max(countWorkDaysFirstChef, countWorkDaysSecondChef);
                for (int j = indexDepartment; j < max * departments.size() + indexDepartment; j += departments.size()) {
                    if (j < min * departments.size() + indexDepartment && j < countDays * departments.size()) {
                        result[i][j] = chefPair;
                    } else if (j < countDays * departments.size()) {
                        if (result[i][j] == null) {
                            result[i][j] = getNotFullChefPair(chefPair, countWorkDaysFirstChef, countWorkDaysSecondChef);
                        } else {
                            result[i][j] = new ChefPair(result[i][j].getNotNullChef(),
                                    getNotFullChefPair(chefPair, countWorkDaysFirstChef, countWorkDaysSecondChef)
                                            .getNotNullChef());
                        }
                    }
                }
                updateBusyChef(busyChefs, chefPair.getFirstChef());
                updateBusyChef(busyChefs, chefPair.getSecondChef());
                if (indexDepartment % 3 == 0) {
                    for (Map.Entry<Chef, Integer> entry : busyChefs.entrySet()) {
                        if (entry.getValue() != 0) {
                            busyChefs.put(entry.getKey(), entry.getValue() - 1);
                        }
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

    private ChefPair getPairForOneDayAndOneDepartment(List<Chef> chefs, Department department,
                                                      Map<Chef, Integer> busyChefs) {
        int indexHead = 0;
        int indexTail = chefs.size() - 1;
        Chef firstChef;
        Chef secondChef;
        for (int j = indexHead; j < chefs.size(); j++) {
            firstChef = chefs.get(j);
            for (int k = indexTail; k > j; k--) {
                secondChef = chefs.get(k);
                if (busyChefs.get(firstChef) == 0 && busyChefs.get(secondChef) == 0 &&
                        canWorkInOneDay(firstChef, secondChef, department)) {
                    return new ChefPair(firstChef, secondChef);
                }
            }
        }
        return null;
    }

    private ChefPair getPairForOneDayAndOneDepartment(List<Chef> chefs, Department department,
                                                      Map<Chef, Integer> busyChefs, Chef chef) {
        if (chef == null) {
            return null;
        }
        int indexHead = 0;
        int indexTail = chefs.size() - 1;
        for (int j = indexHead; j < chefs.size() / 2 + 1; j++) {
            if (busyChefs.get(chefs.get(j)) == 0 && canWorkInOneDay(chef, chefs.get(j), department)) {
                return new ChefPair(chef, chefs.get(j));
            } else if (busyChefs.get(chefs.get(indexTail - j)) == 0 &&
                    canWorkInOneDay(chef, chefs.get(indexTail - j), department)) {
                return new ChefPair(chef, chefs.get(indexTail - j));
            }
        }
        return getPairForOneDayAndOneDepartment(chefs, department, busyChefs);
    }

    private ChefPair getNotFullChefPair(ChefPair pair, int countWorkDaysForFirstChef, int countWorkDaysForSecondChef) {
        if (countWorkDaysForFirstChef < countWorkDaysForSecondChef) {
            return new ChefPair(null, pair.getSecondChef());
        } else if (countWorkDaysForFirstChef == countWorkDaysForSecondChef) {
            return pair;
        } else {
            return new ChefPair(pair.getFirstChef(), null);
        }
    }

    private void updateBusyChef(Map<Chef, Integer> busyChef, Chef chef) {
        if (busyChef.get(chef) == 0) {
            busyChef.remove(chef);
            busyChef.put(chef, chef.getOperatingMode().getCountWorkAndOffDay());
        }
    }
}
