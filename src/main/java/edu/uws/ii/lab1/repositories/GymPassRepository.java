package edu.uws.ii.lab1.repositories;

import edu.uws.ii.lab1.GymPass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GymPassRepository extends JpaRepository<GymPass, Long> {
    List<GymPass> findByMemberId(Long memberId);
}
