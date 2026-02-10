package edu.uws.ii.lab1.controllers;

import edu.uws.ii.lab1.Member;
import edu.uws.ii.lab1.services.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final MemberService memberService;

    public RegistrationController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("member", new Member());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute Member member, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        
        try {
            memberService.register(member);
            model.addAttribute("success", "Rejestracja udana! Sprawdź email.");
        } catch (Exception e) {
            model.addAttribute("error", "Błąd: " + e.getMessage());
        }
        model.addAttribute("member", new Member());
        return "register";
    }
}
