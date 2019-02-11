package ru.chudakov.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"countWorkingDay", "countDayOff"}))
public class OperatingMode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private int countWorkingDay;

    @NonNull
    private int countDayOff;

    @JsonIgnore
    public int getCountWorkAndOffDay() {
        return countWorkingDay + countDayOff;
    }
}
