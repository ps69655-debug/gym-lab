package edu.uws.ii.lab1.repositories;

import edu.uws.ii.lab1.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MembershipTypeRepository extends JpaRepository<MembershipType, Long> {
    Optional<MembershipType> findByName(String name);
}