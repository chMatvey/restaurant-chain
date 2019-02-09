package ru.chudakov.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.chudakov.domain.Chef;
import ru.chudakov.repository.ChefRepository;

import java.util.*;

@Service
public class TimetableServiceImpl implements TimetableService {
    private ChefRepository repository;

    private static final int durationWork = 14;
    private static final int month = 30;
    private static final int countRestaurant = 20;
    private static final int countDepartments = 3;

    @Autowired
    public TimetableServiceImpl(ChefRepository repository) {
        this.repository = repository;
    }

    @Override
    public Chef[][] generateTimetable() {
        Chef[][] result = new Chef[month][countDepartments * countRestaurant];
        List<String> departments = new ArrayList<>(3);
        departments.add("Russian");
        departments.add("Italian");
        departments.add("Japanese");
        List<Chef> chefs = Lists.newArrayList(repository.findAll());
        Map<Chef, Integer> busyChefs = new HashMap<>();
        Comparator<Chef> comparatorByDurationWorkDay = Comparator.comparing(Chef::getDurationWorkDay);
        chefs.sort(comparatorByDurationWorkDay);
        int countRussianQualification = 0;
        int countItalianQualification = 0;
        int countJapaneseQualification = 0;
        int countFiveWorkDays = 0;
        int countTwoWorkDays = 0;
        for (Chef chef : chefs) {
            if (chef.getOperatingMode().getCountWorkingDay() == 5) {
                countFiveWorkDays++;
            } else if (chef.getOperatingMode().getCountWorkingDay() == 2) {
                countTwoWorkDays++;
            }
            if (chef.getDepartments().contains("Russian"))
                countRussianQualification++;
            if (chef.getDepartments().contains("Italian"))
                countItalianQualification++;
            if (chef.getDepartments().contains("Japanese"))
                countJapaneseQualification++;
        }
        if (countFiveWorkDays < 30 || countTwoWorkDays < 30) return null;
        int countBusyChefsWithFiveWorkDays = 0;
        int countBusyChefsWithTwoWorkDays = 0;
        int indexForRussianDepartment = 0;
        int indexForItalianDepartment = 1;
        int indexForJapaneseDepartment = 2;
        for (int i = 0; i < month; i++) {
            int indexDepartment = getDepartmentIndex(countRussianQualification,
                    countItalianQualification, countJapaneseQualification);
            int indexHead = 0;
            int indexTail = 0;
            while (containEmptyDay(result[i])) {
                Chef firstChef = chefs.get(indexHead);
                Chef secondChef = chefs.get(chefs.size() - indexTail - 1);
                boolean onlyTwoWorkDays = false;
                boolean onlyFiveWorkDays = false;
                if (countBusyChefsWithTwoWorkDays >= 30) {
                    onlyFiveWorkDays = true;
                } else if (countBusyChefsWithFiveWorkDays >= 30) {
                    onlyTwoWorkDays = true;
                }
                while (!canWorkInOneDay(firstChef, secondChef, departments.get(indexDepartment),
                        onlyTwoWorkDays, onlyFiveWorkDays)) {
                    if (firstChef.getDurationWorkDay().equals(secondChef.getDurationWorkDay())) {
                        for (int j = indexHead; j < chefs.size(); j++) {
                            for (int k = indexTail; k < chefs.size() - 1 - j; k++) {
                                secondChef = chefs.get(chefs.size() - 1 - k);
                                if (!canWorkInOneDay(firstChef, secondChef, departments.get(indexDepartment),
                                        onlyTwoWorkDays, onlyFiveWorkDays)) break;
                            }
                            if (!canWorkInOneDay(firstChef, secondChef, departments.get(indexDepartment),
                                    onlyTwoWorkDays, onlyFiveWorkDays)) break;
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
                busyChefs.put(firstChef, firstChef.getOperatingMode().getCountWorkAndOffDay());
                busyChefs.put(secondChef, secondChef.getOperatingMode().getCountWorkAndOffDay());
                chefs.remove(indexHead);
                chefs.remove(indexTail - 1);
                int indexForResult;
                if (indexDepartment == 0) {
                    indexForResult = indexForRussianDepartment;
                    indexForRussianDepartment += 6;
                } else if (indexDepartment == 1) {
                    indexForResult = indexForItalianDepartment;
                    indexForItalianDepartment += 6;
                } else {
                    indexForResult = indexForJapaneseDepartment;
                    indexForJapaneseDepartment += 6;
                }
                int countWorkingDaysForFirstChef = firstChef.getOperatingMode().getCountWorkAndOffDay();
                int countWorkingDaysForSecondChef = secondChef.getOperatingMode().getCountWorkAndOffDay();
                if (countWorkingDaysForFirstChef == 2) {
                    countBusyChefsWithTwoWorkDays++;
                } else {
                    countBusyChefsWithFiveWorkDays++;
                }
                if (countWorkingDaysForSecondChef == 2) {
                    countBusyChefsWithTwoWorkDays++;
                } else {
                    countBusyChefsWithFiveWorkDays++;
                }
                for (int j = i; j < countWorkingDaysForFirstChef; j++) {
                    result[j][indexForResult] = firstChef;
                }
                for (int j = i; j < countWorkingDaysForSecondChef; j++) {
                    result[j][indexForResult + 1] = secondChef;
                }
                indexDepartment = getDepartmentIndex(countRussianQualification,
                        countItalianQualification, countJapaneseQualification);
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

    private boolean canWorkInOneDay(Chef firstChef, Chef secondChef, String department,
                                    boolean onlyTwoWorkDays, boolean onlyFiveWorkDays) {
        return firstChef.getDurationWorkDay() + secondChef.getDurationWorkDay() == durationWork &&
                firstChef.getWorkShift() != secondChef.getWorkShift() &&
                firstChef.getDepartments().contains(department) &&
                secondChef.getDepartments().contains(department);
    }

    private int getDepartmentIndex(int countRussian, int countItalian, int countJapanese) {
        if (countRussian < countItalian && countRussian < countJapanese) {
            return 0;
        } else if (countItalian < countJapanese) {
            return 1;
        } else {
            return 2;
        }
    }

    private boolean containEmptyDay(Chef[] timetableForOneDay) {
        for (Chef chef : timetableForOneDay) {
            if (chef == null) return true;
        }
        return false;
    }

    private int getMin(int first, int second) {
        if (first < second) {
            return first;
        } else {
            return second;
        }
    }
}
