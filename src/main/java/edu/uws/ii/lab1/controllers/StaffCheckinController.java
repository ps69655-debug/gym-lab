package edu.uws.ii.lab1.controllers;

import edu.uws.ii.lab1.EntryLog;
import edu.uws.ii.lab1.EntryType;
import edu.uws.ii.lab1.GymPass;
import edu.uws.ii.lab1.services.EntryLogService;
import edu.uws.ii.lab1.services.GymPassService;
import edu.uws.ii.lab1.services.MemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/staff")
@PreAuthorize("hasAnyRole('STAFF','ADMIN')")
public class StaffCheckinController {

    private final MemberService memberService;
    private final GymPassService gymPassService;
    private final EntryLogService entryLogService;

    public StaffCheckinController(MemberService memberService,
                                  GymPassService gymPassService,
                                  EntryLogService entryLogService) {
        this.memberService = memberService;
        this.gymPassService = gymPassService;
        this.entryLogService = entryLogService;
    }

    @GetMapping("/checkin")
    public String checkinPage(@RequestParam(required = false) String email, Model model) {
        model.addAttribute("email", email == null ? "" : email);

        if (email != null && !email.isBlank()) {
            var member = memberService.findByEmail(email.trim());
            model.addAttribute("memberFound", member != null);
            model.addAttribute("member", member);

            if (member != null) {
                List<GymPass> passes = gymPassService.findByMemberId(member.getId());
                model.addAttribute("passes", passes);
            }
        } else {
            model.addAttribute("memberFound", false);
        }

        return "staff/checkin";
    }

    @PostMapping("/checkin")
    public String doCheckin(@RequestParam String email,
                            @RequestParam Long gymPassId) {

        var member = memberService.findByEmail(email.trim());
        if (member == null) {
            return "redirect:/staff/checkin?error=member_not_found";
        }

        GymPass gymPass = gymPassService.findById(gymPassId);

        // integralność: karnet musi należeć do tego membera
        if (gymPass.getMember() == null || !gymPass.getMember().getId().equals(member.getId())) {
            return "redirect:/staff/checkin?email=" + member.getEmail() + "&error=pass_not_owned";
        }

        LocalDate today = LocalDate.now();

        if (gymPass.getStartDate() != null && today.isBefore(gymPass.getStartDate())) {
            return "redirect:/staff/checkin?email=" + member.getEmail() + "&error=pass_not_started";
        }
        if (gymPass.getEndDate() != null && today.isAfter(gymPass.getEndDate())) {
            gymPass.recomputeStatus();
            gymPassService.save(gymPass);
            return "redirect:/staff/checkin?email=" + member.getEmail() + "&error=pass_expired";
        }
        if (gymPass.getEntriesLeft() == null || gymPass.getEntriesLeft() <= 0) {
            gymPass.recomputeStatus();
            gymPassService.save(gymPass);
            return "redirect:/staff/checkin?email=" + member.getEmail() + "&error=no_entries_left";
        }

        // consume entry
        gymPass.setEntriesLeft(gymPass.getEntriesLeft() - 1);
        gymPass.recomputeStatus();
        gymPassService.save(gymPass);

        EntryLog log = new EntryLog();
        log.setMember(member);
        log.setGymPass(gymPass);
        log.setEntryTime(LocalDateTime.now());
        log.setEntryType(EntryType.CHECK_IN);
        entryLogService.save(log);

        return "redirect:/staff/checkin?email=" + member.getEmail() + "&success=checked_in";
    }
}
