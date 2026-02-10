package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.Trainer;
import edu.uws.ii.lab1.repositories.TrainerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository repository;

    public TrainerServiceImpl(TrainerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Trainer> findAll() {
        return repository.findAll();
    }

    @Override
    public Trainer findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono trenera"));
    }

    @Override
    public Trainer save(Trainer trainer) {
        return repository.save(trainer);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}