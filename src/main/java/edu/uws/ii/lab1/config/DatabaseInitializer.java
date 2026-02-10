package edu.uws.ii.lab1.config;

import edu.uws.ii.lab1.*;
import edu.uws.ii.lab1.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DatabaseInitializer {

    /**
     * Etap 2 / Lab03:
     * - seed: MembershipType (plans)
     * - seed: Members (DB) z rolami ROLE_MEMBER/ROLE_STAFF/ROLE_ADMIN i poprawnym hasłem BCrypt
     * - seed: Rooms, Trainers, ClassSchedules
     * - seed: Reservations (żeby STAFF od razu widział uczestników)
     */
    @Bean(name = "init")
    public CommandLineRunner init(MemberRepository memberRepository,
                                  MembershipTypeRepository membershipTypeRepository,
                                  RoomRepository roomRepository,
                                  TrainerRepository trainerRepository,
                                  ClassScheduleRepository classScheduleRepository,
                                  ReservationRepository reservationRepository,
                                  PasswordEncoder passwordEncoder) {
        return args -> {

            // --------- PLANS ---------
            if (membershipTypeRepository.count() == 0) {
                MembershipType standard = new MembershipType();
                standard.setName("Standard 30 dni");
                standard.setDurationDays(30);
                standard.setPrice(149.99);
                standard.setDescription("Dostęp na 30 dni, standardowy plan.");
                membershipTypeRepository.save(standard);

                MembershipType premium = new MembershipType();
                premium.setName("Premium 90 dni");
                premium.setDurationDays(90);
                premium.setPrice(399.99);
                premium.setDescription("Dostęp na 90 dni, pełny plan premium.");
                membershipTypeRepository.save(premium);
            }

            // --------- MEMBERS (DB) ---------
            Member member = createOrFixMember(
                    memberRepository, passwordEncoder,
                    "member@local", "Member Demo", "ROLE_MEMBER", "member12345"
            );

            createOrFixMember(
                    memberRepository, passwordEncoder,
                    "staff@local", "Staff Demo", "ROLE_STAFF", "staff12345"
            );

            createOrFixMember(
                    memberRepository, passwordEncoder,
                    "admin@local", "Admin Demo", "ROLE_ADMIN", "admin12345"
            );

            // --------- ROOMS ---------
            if (roomRepository.count() == 0) {
                Room r1 = new Room();
                r1.setName("Sala A");
                r1.setCapacity(20);
                r1.setLocation("Parter");
                roomRepository.save(r1);

                Room r2 = new Room();
                r2.setName("Sala B");
                r2.setCapacity(12);
                r2.setLocation("1 piętro");
                roomRepository.save(r2);
            }

            // --------- TRAINERS ---------
            if (trainerRepository.count() == 0) {
                Trainer t1 = new Trainer();
                t1.setFullName("Anna Nowak");
                t1.setSpecialization("Yoga / Mobility");
                t1.setEmail("anna.nowak@local");
                t1.setPhone("500-100-200");
                trainerRepository.save(t1);

                Trainer t2 = new Trainer();
                t2.setFullName("Kamil Kowalski");
                t2.setSpecialization("Strength / Functional");
                t2.setEmail("kamil.kowalski@local");
                t2.setPhone("500-300-400");
                trainerRepository.save(t2);
            }

            // --------- CLASS SCHEDULES ---------
            if (classScheduleRepository.count() == 0) {
                List<Room> rooms = roomRepository.findAll();
                List<Trainer> trainers = trainerRepository.findAll();

                Room salaA = rooms.stream().filter(r -> "Sala A".equals(r.getName())).findFirst().orElse(rooms.get(0));
                Room salaB = rooms.stream().filter(r -> "Sala B".equals(r.getName())).findFirst().orElse(rooms.get(0));

                Trainer anna = trainers.stream().filter(t -> t.getFullName().contains("Anna")).findFirst().orElse(trainers.get(0));
                Trainer kamil = trainers.stream().filter(t -> t.getFullName().contains("Kamil")).findFirst().orElse(trainers.get(0));

                LocalDateTime base = LocalDateTime.now().withSecond(0).withNano(0).plusDays(1);

                ClassSchedule s1 = new ClassSchedule();
                s1.setTitle("Yoga Beginner");
                s1.setDescription("Rozciąganie + oddech + podstawy pozycji.");
                s1.setStartTime(base.withHour(18).withMinute(0));
                s1.setEndTime(base.withHour(19).withMinute(0));
                s1.setMaxParticipants(15);
                s1.setTrainer(anna);
                s1.setRoom(salaA);
                classScheduleRepository.save(s1);

                ClassSchedule s2 = new ClassSchedule();
                s2.setTitle("Functional Strength");
                s2.setDescription("Trening ogólnorozwojowy z naciskiem na siłę.");
                s2.setStartTime(base.plusDays(1).withHour(19).withMinute(0));
                s2.setEndTime(base.plusDays(1).withHour(20).withMinute(0));
                s2.setMaxParticipants(10);
                s2.setTrainer(kamil);
                s2.setRoom(salaB);
                classScheduleRepository.save(s2);
            }

            // --------- RESERVATIONS (żeby STAFF widział uczestników od razu) ---------
            // Tworzymy 1 rezerwację member@local na pierwsze zajęcia, jeśli jeszcze jej nie ma.
            if (reservationRepository.count() == 0) {
                List<ClassSchedule> sessions = classScheduleRepository.findAll();
                if (!sessions.isEmpty() && member != null) {
                    Reservation r = new Reservation();
                    r.setMember(member);
                    r.setClassSchedule(sessions.get(0));
                    r.setStatus(ReservationStatus.CONFIRMED);
                    r.setCreatedAt(LocalDateTime.now().withSecond(0).withNano(0));
                    reservationRepository.save(r);
                }
            }
        };
    }

    private Member createOrFixMember(MemberRepository repo,
                                     PasswordEncoder encoder,
                                     String email,
                                     String fullName,
                                     String role,
                                     String rawPassword) {

        var existingOpt = repo.findByEmail(email);
        if (existingOpt.isPresent()) {
            Member m = existingOpt.get();

            // naprawa ról po wcześniejszym USER/SUPERUSER
            if (m.getRole() == null || m.getRole().isBlank() || m.getRole().equals("ROLE_USER") || m.getRole().equals("ROLE_SUPERUSER")) {
                m.setRole(role);
            }

            // naprawa pustego hasła (żeby walidacja @NotBlank nie wywalała)
            if (m.getPassword() == null || m.getPassword().isBlank()) {
                m.setPassword(encoder.encode(rawPassword));
            }

            // naprawa enabled
            if (!m.isEnabled()) {
                m.setEnabled(true);
            }

            repo.save(m);
            return m;
        }

        Member m = new Member();
        m.setEmail(email);
        m.setFullName(fullName);
        m.setEnabled(true);
        m.setRole(role);
        m.setPassword(encoder.encode(rawPassword));
        return repo.save(m);
    }
}