package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.ClassSchedule;
import edu.uws.ii.lab1.repositories.ClassScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassScheduleServiceImpl implements ClassScheduleService {

    private final ClassScheduleRepository repository;

    public ClassScheduleServiceImpl(ClassScheduleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ClassSchedule> findAll() {
        return repository.findAll();
    }

    @Override
    public ClassSchedule findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zajęć"));
    }

    @Override
    public ClassSchedule save(ClassSchedule schedule) {
        return repository.save(schedule);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}