package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.Reservation;
import edu.uws.ii.lab1.ReservationStatus;

import java.util.List;

public interface ReservationService {

    List<Reservation> findAll();

    List<Reservation> findByMember(Long memberId);

    List<Reservation> findByClassSchedule(Long classScheduleId);

    Reservation findById(Long id);

    Reservation save(Reservation reservation);

    Reservation updateStatus(Long reservationId, ReservationStatus status);

    Reservation createReservation(Long memberId, Long classScheduleId);

    void deleteById(Long id);
}