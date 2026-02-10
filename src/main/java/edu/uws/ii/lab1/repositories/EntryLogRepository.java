package edu.uws.ii.lab1.repositories;

import edu.uws.ii.lab1.EntryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntryLogRepository extends JpaRepository<EntryLog, Long> {
    List<EntryLog> findByMemberIdOrderByEntryTimeDesc(Long memberId);
}