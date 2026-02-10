package edu.uws.ii.lab1;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "gym_passes")
@Getter
@Setter
@NoArgsConstructor
public class GymPass extends AuditedEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String ownerName;

    @NotBlank
    @Size(min = 2, max = 30)
    private String passType;

    private boolean active;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    @Min(0)
    private Integer entriesLeft;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("10000.0")
    @NumberFormat(pattern = "#0.00")
    private Double price;

    private String notes;
    private String imageFileName;

    @Enumerated(EnumType.STRING)
    private GymPassStatus status;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToMany
    @JoinTable(
            name = "gym_pass_addons",
            joinColumns = @JoinColumn(name = "gym_pass_id"),
            inverseJoinColumns = @JoinColumn(name = "addon_id")
    )
    private Set<Addon> addons;

    @ManyToOne
    @JoinColumn(name = "membership_type_id")
    private MembershipType membershipType;

    @OneToMany(mappedBy = "gymPass")
    private List<Payment> payments;

    @OneToMany(mappedBy = "gymPass")
    private List<EntryLog> entryLogs;

    public void recomputeStatus() {
        LocalDate today = LocalDate.now();

        if (today.isAfter(endDate)) {
            status = GymPassStatus.EXPIRED;
            active = false;
        } else if (entriesLeft == 0) {
            status = GymPassStatus.SUSPENDED;
            active = false;
        } else {
            status = GymPassStatus.ACTIVE;
            active = true;
        }
    }
}