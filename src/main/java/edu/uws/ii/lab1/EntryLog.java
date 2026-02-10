package edu.uws.ii.lab1;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "entry_logs")
@Getter
@Setter
@NoArgsConstructor
public class EntryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private GymPass gymPass;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime entryTime;

    @Enumerated(EnumType.STRING)
    private EntryType entryType;
}