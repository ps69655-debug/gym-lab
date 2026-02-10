package edu.uws.ii.lab1.controllers;

import edu.uws.ii.lab1.services.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ActivationController {

    private final MemberService memberService;

    public ActivationController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/activate")
    public String activate(String code, Model model) {
        boolean success = memberService.activateAccount(code);
        model.addAttribute("success", success);
        return "activation";
    }
}