package edu.uws.ii.lab1.controllers;

import edu.uws.ii.lab1.services.GymPassService;
import edu.uws.ii.lab1.services.MemberService;
import edu.uws.ii.lab1.services.ReservationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberPanelController {

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final GymPassService gymPassService;

    public MemberPanelController(MemberService memberService,
                                 ReservationService reservationService,
                                 GymPassService gymPassService) {
        this.memberService = memberService;
        this.reservationService = reservationService;
        this.gymPassService = gymPassService;
    }

    @GetMapping("/panel")
    public String panel(Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var member = memberService.findByEmail(auth.getName());

        // Member is null for in-memory users like admin/superuser
        if (member == null) {
            return "redirect:/?error=not_registered";
        }

        model.addAttribute("member", member);
        model.addAttribute("reservations", reservationService.findByMember(member.getId()));
        // IMPORTANT: show only the logged-in member's gym passes (privacy fix)
        model.addAttribute("gympasses", gymPassService.findByMemberId(member.getId()));

        return "panel/index";
    }
}
