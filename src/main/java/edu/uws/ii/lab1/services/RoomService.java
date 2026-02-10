package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.Room;

import java.util.List;

public interface RoomService {
    List<Room> findAll();
    Room findById(Long id);
    Room save(Room room);
    void deleteById(Long id);
}