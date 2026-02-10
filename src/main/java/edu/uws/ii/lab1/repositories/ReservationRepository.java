package edu.uws.ii.lab1.repositories;

import edu.uws.ii.lab1.Reservation;
import edu.uws.ii.lab1.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByMemberId(Long memberId);

    List<Reservation> findByClassScheduleId(Long classScheduleId);

    boolean existsByMemberIdAndClassScheduleId(Long memberId, Long classScheduleId);

    long countByClassScheduleIdAndStatusIn(Long classScheduleId, Collection<ReservationStatus> statuses);

    @Query("select r from Reservation r where lower(r.classSchedule.title) like lower(concat('%', :title, '%'))")
    List<Reservation> searchByClassTitle(@Param("title") String title);
}