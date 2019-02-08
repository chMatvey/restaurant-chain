package ru.chudakov;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.chudakov.domain.Chef;

@Data
@RequiredArgsConstructor
public class Pair {
    @NonNull
    private Chef chefForMorning;
    @NonNull
    private Chef chefForEvening;
}
