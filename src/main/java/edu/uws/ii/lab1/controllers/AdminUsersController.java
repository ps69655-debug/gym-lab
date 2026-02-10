package edu.uws.ii.lab1.controllers;

import edu.uws.ii.lab1.services.MemberService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsersController {

    private final MemberService memberService;

    public AdminUsersController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", memberService.findAll());
        model.addAttribute("roles", new String[]{"ROLE_MEMBER", "ROLE_STAFF", "ROLE_ADMIN"});
        return "admin/users";
    }

    @PostMapping("/{id}/role")
    public String updateRole(@PathVariable Long id, @RequestParam String role) {
        memberService.updateRole(id, role);
        return "redirect:/admin/users?success=role_updated";
    }
}