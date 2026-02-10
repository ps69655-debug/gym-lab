package edu.uws.ii.lab1.repositories;

import edu.uws.ii.lab1.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    List<Trainer> findByFullNameContainingIgnoreCase(String fullName);
}