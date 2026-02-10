package edu.uws.ii.lab1.repositories;

import edu.uws.ii.lab1.Addon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddonRepository extends JpaRepository<Addon, Long> {
}