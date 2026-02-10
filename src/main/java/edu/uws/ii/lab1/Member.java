package edu.uws.ii.lab1;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "members")
@Getter
@Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String fullName;

    @OneToMany(mappedBy = "member")
    private List<GymPass> gymPasses;

    @OneToMany(mappedBy = "member")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "member")
    private List<EntryLog> entryLogs;

    @OneToMany(mappedBy = "member")
    private List<Payment> payments;

    // LAB05: pliki użytkownika
    @OneToMany(mappedBy = "member")
    private List<MemberFile> files;

    @Email
    @Column(unique = true)
    private String email;

    @Column(length = 30)
    private String role = "ROLE_MEMBER";

    private String activationCode;

    @NotBlank
    @Column(length = 100, nullable = false)
    private String password;

    private boolean enabled;
}