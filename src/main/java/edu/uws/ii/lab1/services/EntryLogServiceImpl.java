package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.EntryLog;
import edu.uws.ii.lab1.repositories.EntryLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntryLogServiceImpl implements EntryLogService {

    private final EntryLogRepository repository;

    public EntryLogServiceImpl(EntryLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EntryLog> findAll() {
        return repository.findAll();
    }

    @Override
    public List<EntryLog> findByMember(Long memberId) {
        return repository.findByMemberIdOrderByEntryTimeDesc(memberId);
    }

    @Override
    public EntryLog save(EntryLog log) {
        return repository.save(log);
    }
}