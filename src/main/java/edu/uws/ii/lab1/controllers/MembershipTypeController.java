package edu.uws.ii.lab1.controllers;

import edu.uws.ii.lab1.MembershipType;
import edu.uws.ii.lab1.services.MembershipTypeService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/membership-types")
@PreAuthorize("hasRole('ADMIN')")
public class MembershipTypeController {

    private final MembershipTypeService membershipTypeService;

    public MembershipTypeController(MembershipTypeService membershipTypeService) {
        this.membershipTypeService = membershipTypeService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("types", membershipTypeService.findAll());
        return "membership-types/list";
    }

    @GetMapping("/form")
    public String showForm(Model model, @RequestParam Optional<Long> id) {
        MembershipType type = id.map(membershipTypeService::findById).orElse(new MembershipType());
        model.addAttribute("membershipType", type);
        return "membership-types/form";
    }

    @PostMapping("/form")
    public String save(@Valid @ModelAttribute("membershipType") MembershipType membershipType,
                       BindingResult result) {
        if (result.hasErrors()) {
            return "membership-types/form";
        }
        membershipTypeService.save(membershipType);
        return "redirect:/membership-types";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        membershipTypeService.deleteById(id);
        return "redirect:/membership-types";
    }
}