package edu.uws.ii.lab1.repositories;

import edu.uws.ii.lab1.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
    List<ClassSchedule> findByTrainerId(Long trainerId);
    List<ClassSchedule> findByRoomId(Long roomId);
}