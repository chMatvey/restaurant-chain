package ru.chudakov.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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
    @JsonIgnore
    private Integer durationWorkDay;

    @NonNull
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "chef_department",
            joinColumns = @JoinColumn(name = "chef_id"),
            inverseJoinColumns = @JoinColumn(name = "department_id"))
    private Set<Department> departments;

    @NonNull
    @Enumerated(EnumType.STRING)
    private WorkShift workShift;

    @NonNull
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "operatingMode_id")
    private OperatingMode operatingMode;
}
