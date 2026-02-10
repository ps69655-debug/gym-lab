package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.MembershipType;
import edu.uws.ii.lab1.repositories.MembershipTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembershipTypeServiceImpl implements MembershipTypeService {

    private final MembershipTypeRepository repository;

    public MembershipTypeServiceImpl(MembershipTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MembershipType> findAll() {
        return repository.findAll();
    }

    @Override
    public MembershipType findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono typu karnetu"));
    }

    @Override
    public MembershipType save(MembershipType type) {
        return repository.save(type);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}