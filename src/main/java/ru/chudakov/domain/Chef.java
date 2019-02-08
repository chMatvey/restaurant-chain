package ru.chudakov.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Chef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String firstName;

    @NonNull
    private String middleName;

    @NonNull
    private String lastName;

    @NonNull
    private Integer durationWorkDay;

    @NonNull
    @ElementCollection
    Set<String> departments;

    @NonNull
    @Enumerated(EnumType.STRING)
    private WorkShift workShift;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "operatingMode_id")
    private OperatingMode operatingMode;
}
