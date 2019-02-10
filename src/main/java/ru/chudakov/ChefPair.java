package ru.chudakov;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.chudakov.domain.Chef;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChefPair {
    private Chef firstChef;
    private Chef secondChef;

    @JsonIgnore
    public Chef getNotNullChef() {
        if (firstChef != null) {
            return firstChef;
        } else {
            return secondChef;
        }
    }

    @JsonIgnore
    public boolean isNotFullPair() {
        if (firstChef == null || secondChef == null) {
            return true;
        } else {
            return false;
        }
    }
}
