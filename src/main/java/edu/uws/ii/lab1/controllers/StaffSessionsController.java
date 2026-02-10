package edu.uws.ii.lab1.controllers;

import edu.uws.ii.lab1.ReservationStatus;
import edu.uws.ii.lab1.services.ClassScheduleService;
import edu.uws.ii.lab1.services.ReservationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staff/sessions")
@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
public class StaffSessionsController {

    private final ClassScheduleService classScheduleService;
    private final ReservationService reservationService;

    public StaffSessionsController(ClassScheduleService classScheduleService,
                                   ReservationService reservationService) {
        this.classScheduleService = classScheduleService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("sessions", classScheduleService.findAll());
        return "staff/sessions";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        model.addAttribute("session", classScheduleService.findById(id));
        model.addAttribute("reservations", reservationService.findByClassSchedule(id));
        model.addAttribute("statuses", ReservationStatus.values());
        return "staff/session-details";
    }

    @PostMapping("/reservation/{reservationId}/status")
    public String updateStatus(@PathVariable Long reservationId,
                               @RequestParam ReservationStatus status,
                               @RequestParam Long sessionId) {
        reservationService.updateStatus(reservationId, status);
        return "redirect:/staff/sessions/" + sessionId + "?success=updated";
    }
}