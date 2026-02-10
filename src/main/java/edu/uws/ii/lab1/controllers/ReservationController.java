package edu.uws.ii.lab1.controllers;

import edu.uws.ii.lab1.services.ClassScheduleService;
import edu.uws.ii.lab1.services.MemberService;
import edu.uws.ii.lab1.services.ReservationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ClassScheduleService classScheduleService;
    private final MemberService memberService;

    public ReservationController(ReservationService reservationService,
                                 ClassScheduleService classScheduleService,
                                 MemberService memberService) {
        this.reservationService = reservationService;
        this.classScheduleService = classScheduleService;
        this.memberService = memberService;
    }

    // MEMBER: formularz rezerwacji
    @GetMapping("/form")
    @PreAuthorize("hasRole('MEMBER')")
    public String showForm(Model model) {
        model.addAttribute("classes", classScheduleService.findAll());
        return "reservations/form";
    }

    // MEMBER: tworzenie rezerwacji z regułami biznesowymi
    @PostMapping("/form")
    @PreAuthorize("hasRole('MEMBER')")
    public String save(@RequestParam Long classScheduleId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var member = memberService.findByEmail(auth.getName());
        if (member == null) {
            return "redirect:/?error=not_registered";
        }

        try {
            reservationService.createReservation(member.getId(), classScheduleId);
            return "redirect:/reservations/mine?success=reserved";
        } catch (IllegalStateException ex) {
            return switch (ex.getMessage()) {
                case "CLASS_ALREADY_STARTED" -> "redirect:/reservations/form?error=class_started";
                case "ALREADY_RESERVED" -> "redirect:/reservations/form?error=already_reserved";
                case "CLASS_FULL" -> "redirect:/reservations/form?error=class_full";
                default -> "redirect:/reservations/form?error=unknown";
            };
        }
    }

    // MEMBER: moje rezerwacje
    @GetMapping("/mine")
    @PreAuthorize("hasRole('MEMBER')")
    public String myReservations(Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var member = memberService.findByEmail(auth.getName());
        if (member == null) {
            return "redirect:/?error=not_registered";
        }

        model.addAttribute("reservations", reservationService.findByMember(member.getId()));
        return "reservations/mine";
    }
}