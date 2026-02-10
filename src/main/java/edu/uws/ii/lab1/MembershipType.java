package edu.uws.ii.lab1;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "membership_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    private String description;

    @Min(1)
    private Integer durationDays;

    @Min(0)
    private Double price;

    @OneToMany(mappedBy = "membershipType")
    private List<GymPass> gymPasses;
}