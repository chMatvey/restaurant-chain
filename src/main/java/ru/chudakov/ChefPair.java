package ru.chudakov;

import lombok.*;
import ru.chudakov.domain.Chef;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChefPair {
    private Chef firstChef;
    private Chef secondChef;

    public Chef getNotNullChef() {
        if (firstChef != null) {
            return firstChef;
        } else {
            return secondChef;
        }
    }
}
