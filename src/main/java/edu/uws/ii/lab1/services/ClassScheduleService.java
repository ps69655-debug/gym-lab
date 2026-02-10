package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.ClassSchedule;

import java.util.List;

public interface ClassScheduleService {
    List<ClassSchedule> findAll();
    ClassSchedule findById(Long id);
    ClassSchedule save(ClassSchedule schedule);
    void deleteById(Long id);
}