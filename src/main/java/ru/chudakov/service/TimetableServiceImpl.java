package ru.chudakov.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.chudakov.Pair;
import ru.chudakov.domain.Chef;
import ru.chudakov.domain.WorkShift;
import ru.chudakov.repository.ChefRepository;

import java.util.*;

@Service
public class TimetableServiceImpl implements TimetableService {
    private ChefRepository repository;

    private static final int durationWork = 14;
    private static final int month = 30;
    private static final int countRestaurant = 20;

    @Autowired
    public TimetableServiceImpl(ChefRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<List<Pair>> generateTimetable() {
        List<List<Pair>> result = new ArrayList<>(month);
        for (int i = 0; i < month; i++) {
            result.add(new ArrayList<>(60));
        }
        List<String> departments = new ArrayList<>();
        departments.add("Russian");
        departments.add("Italian");
        departments.add("Japanese");
        List<Chef> chefs = Lists.newArrayList(repository.findAll());
        Map<Chef, Integer> busyChefs = new HashMap<>();
        Comparator<Chef> comparatorByDurationWorkDay = Comparator.comparing(Chef::getDurationWorkDay);
        chefs.sort(comparatorByDurationWorkDay);
//        int countRussianCuisine = 0;
//        int countItalianCuisine = 0;
//        int countJapaneseCuisine = 0;
//        for (Chef chef : chefs) {
//            if (chef.getDepartments().contains("Russian")) {
//                countRussianCuisine++;
//            }
//            if (chef.getDepartments().contains("Italian")) {
//                countItalianCuisine++;
//            }
//            if (chef.getDepartments().contains("Japanese")) {
//                countJapaneseCuisine++;
//            }
//        }
        for (int i = 0; i < 30; i++) {
            int indexDepartment = 0;
            int indexHead = 0;
            int indexTail = 0;
            int indexRestaurant = 0;
            while (result.get(i).size() != countRestaurant * departments.size()) {
                Chef firstChef = chefs.get(indexHead);
                Chef secondChef = chefs.get(chefs.size() - indexTail - 1);
                while (canWorkTogether(firstChef, secondChef, departments.get(indexDepartment))) {
                    if (firstChef.getDurationWorkDay().equals(secondChef.getDurationWorkDay())) {
                        for (int j = indexHead; j < chefs.size(); j++) {
                            for (int k = indexTail; k < chefs.size() - 1 - j; k++) {
                                secondChef = chefs.get(chefs.size() - 1 - k);
                                if (canWorkTogether(firstChef, secondChef, departments.get(indexDepartment))) break;
                            }
                            if (canWorkTogether(firstChef, secondChef, departments.get(indexDepartment))) break;
                            firstChef = chefs.get(j);
                        }
                        if (indexHead < indexTail) indexHead++;
                        else indexTail++;
                    } else if (durationWork - firstChef.getDurationWorkDay() > secondChef.getDurationWorkDay()) {
                        indexHead++;
                    } else {
                        indexTail++;
                    }
                    firstChef = chefs.get(indexHead);
                    secondChef = chefs.get(chefs.size() - indexTail - 1);
                    if (isMiddle(indexHead, indexTail, chefs.size())) break;
                }
                busyChefs.put(firstChef, firstChef.getOperatingMode().getCountWorkAndOffDay() + i);
                busyChefs.put(secondChef, secondChef.getOperatingMode().getCountWorkAndOffDay() + i);
                result.get(i).add(new Pair(firstChef, secondChef));
                chefs.remove(indexHead);
                chefs.remove(indexTail - 1);
                indexDepartment++;
                indexHead = 0;
                indexTail = 0;
            }
        }
        return result;
    }

    private boolean isMiddle(int indexHead, int indexTail, int sizeArray) {
        int middle = sizeArray / 2;
        return indexHead > middle && indexTail < middle;
    }

    private boolean canWorkTogether(Chef firstChef, Chef secondChef, String department) {
        return firstChef.getDurationWorkDay() + secondChef.getDurationWorkDay() != durationWork &&
                firstChef.getWorkShift() == secondChef.getWorkShift() &&
                !firstChef.getDepartments().contains(department) &&
                !secondChef.getDepartments().contains(department);
    }
}
