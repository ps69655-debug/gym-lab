package edu.uws.ii.lab1;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "trainers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fullName;

    private String specialization;

    @Email
    private String email;

    private String phone;

    @OneToMany(mappedBy = "trainer")
    private List<ClassSchedule> classSchedules;
}