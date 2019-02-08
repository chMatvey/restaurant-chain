package ru.chudakov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class OperatingMode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private int countWorkingDay;

    @NonNull
    private int countDayOff;

    public int getCountWorkAndOffDay() {
        return countWorkingDay + countDayOff;
    }
}
