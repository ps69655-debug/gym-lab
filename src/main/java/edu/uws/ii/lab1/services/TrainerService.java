package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.Trainer;

import java.util.List;

public interface TrainerService {
    List<Trainer> findAll();
    Trainer findById(Long id);
    Trainer save(Trainer trainer);
    void deleteById(Long id);
}