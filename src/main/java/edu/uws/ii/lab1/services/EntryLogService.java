package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.EntryLog;
import java.util.List;

public interface EntryLogService {
    List<EntryLog> findAll();
    List<EntryLog> findByMember(Long memberId);
    EntryLog save(EntryLog log);
}