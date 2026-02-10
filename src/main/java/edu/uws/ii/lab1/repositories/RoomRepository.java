package edu.uws.ii.lab1.repositories;

import edu.uws.ii.lab1.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByNameContainingIgnoreCase(String name);
}