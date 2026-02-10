package edu.uws.ii.lab1.controllers;

import edu.uws.ii.lab1.EntryLog;
import edu.uws.ii.lab1.EntryType;
import edu.uws.ii.lab1.services.EntryLogService;
import edu.uws.ii.lab1.services.GymPassService;
import edu.uws.ii.lab1.services.MemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/entries")
public class EntryLogController {

    private final EntryLogService entryLogService;
    private final MemberService memberService;
    private final GymPassService gymPassService;

    public EntryLogController(EntryLogService entryLogService,
                              MemberService memberService,
                              GymPassService gymPassService) {
        this.entryLogService = entryLogService;
        this.memberService = memberService;
        this.gymPassService = gymPassService;
    }

    @GetMapping("/mine")
    @PreAuthorize("hasRole('MEMBER')")
    public String myEntries(Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var member = memberService.findByEmail(auth.getName());

        if (member == null) {
            return "redirect:/?error=not_registered";
        }

        model.addAttribute("entries", entryLogService.findByMember(member.getId()));
        return "entries/mine";
    }

    @PostMapping("/checkin")
    @PreAuthorize("hasRole('MEMBER')")
    public String checkIn(@RequestParam Long gymPassId) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var member = memberService.findByEmail(auth.getName());

        EntryLog log = new EntryLog();
        log.setMember(member);
        log.setGymPass(gymPassService.findById(gymPassId));
        log.setEntryTime(LocalDateTime.now());
        log.setEntryType(EntryType.CHECK_IN);

        entryLogService.save(log);
        return "redirect:/entries/mine";
    }
}