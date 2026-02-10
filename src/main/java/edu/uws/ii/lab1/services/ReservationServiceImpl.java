package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.ClassSchedule;
import edu.uws.ii.lab1.Member;
import edu.uws.ii.lab1.Reservation;
import edu.uws.ii.lab1.ReservationStatus;
import edu.uws.ii.lab1.repositories.ClassScheduleRepository;
import edu.uws.ii.lab1.repositories.MemberRepository;
import edu.uws.ii.lab1.repositories.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ClassScheduleRepository classScheduleRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  MemberRepository memberRepository,
                                  ClassScheduleRepository classScheduleRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.classScheduleRepository = classScheduleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<Reservation> findByMember(Long memberId) {
        return reservationRepository.findByMemberId(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<Reservation> findByClassSchedule(Long classScheduleId) {
        return reservationRepository.findByClassScheduleId(classScheduleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono rezerwacji"));
    }

    @Override
    @Transactional
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public Reservation updateStatus(Long reservationId, ReservationStatus status) {
        Reservation r = findById(reservationId);
        r.setStatus(status);
        return reservationRepository.save(r);
    }

    @Override
    @Transactional
    public Reservation createReservation(Long memberId, Long classScheduleId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono członka"));

        ClassSchedule session = classScheduleRepository.findById(classScheduleId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono zajęć"));

        // 1) blokada po starcie (PO, nie "w tej samej sekundzie")
        LocalDateTime now = LocalDateTime.now();
        if (session.getStartTime() != null && now.isAfter(session.getStartTime())) {
            throw new IllegalStateException("CLASS_ALREADY_STARTED");
        }

        // 2) brak duplikatu
        if (reservationRepository.existsByMemberIdAndClassScheduleId(memberId, classScheduleId)) {
            throw new IllegalStateException("ALREADY_RESERVED");
        }

        // 3) limit miejsc (CONFIRMED + PENDING jako zajęte)
        Set<ReservationStatus> taken = EnumSet.of(ReservationStatus.CONFIRMED, ReservationStatus.PENDING);
        long used = reservationRepository.countByClassScheduleIdAndStatusIn(classScheduleId, taken);

        Integer max = session.getMaxParticipants();
        if (max != null && used >= max) {
            throw new IllegalStateException("CLASS_FULL");
        }

        Reservation r = new Reservation();
        r.setMember(member);
        r.setClassSchedule(session);
        r.setCreatedAt(LocalDateTime.now().withSecond(0).withNano(0));
        r.setStatus(ReservationStatus.CONFIRMED);

        return reservationRepository.save(r);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}