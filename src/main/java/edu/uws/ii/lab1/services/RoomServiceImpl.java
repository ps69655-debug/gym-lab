package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.Room;
import edu.uws.ii.lab1.repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repository;

    public RoomServiceImpl(RoomRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Room> findAll() {
        return repository.findAll();
    }

    @Override
    public Room findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono sali"));
    }

    @Override
    public Room save(Room room) {
        return repository.save(room);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}