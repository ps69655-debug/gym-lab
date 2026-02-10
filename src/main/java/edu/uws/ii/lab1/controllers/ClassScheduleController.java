package edu.uws.ii.lab1.controllers;

import edu.uws.ii.lab1.ClassSchedule;
import edu.uws.ii.lab1.services.ClassScheduleService;
import edu.uws.ii.lab1.services.RoomService;
import edu.uws.ii.lab1.services.TrainerService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/classes")
public class ClassScheduleController {

    private final ClassScheduleService classScheduleService;
    private final TrainerService trainerService;
    private final RoomService roomService;

    public ClassScheduleController(ClassScheduleService classScheduleService,
                                   TrainerService trainerService,
                                   RoomService roomService) {
        this.classScheduleService = classScheduleService;
        this.trainerService = trainerService;
        this.roomService = roomService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MEMBER','STAFF','ADMIN')")
    public String list(Model model) {
        model.addAttribute("classes", classScheduleService.findAll());
        return "classes/list";
    }

    @GetMapping("/form")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public String showForm(Model model, @RequestParam Optional<Long> id) {
        model.addAttribute("classSchedule", id.map(classScheduleService::findById).orElse(new ClassSchedule()));
        model.addAttribute("trainers", trainerService.findAll());
        model.addAttribute("rooms", roomService.findAll());
        return "classes/form";
    }

    @PostMapping("/form")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public String save(@Valid @ModelAttribute("classSchedule") ClassSchedule classSchedule,
                       BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("trainers", trainerService.findAll());
            model.addAttribute("rooms", roomService.findAll());
            return "classes/form";
        }
        classScheduleService.save(classSchedule);
        return "redirect:/classes";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public String delete(@RequestParam Long id) {
        classScheduleService.deleteById(id);
        return "redirect:/classes";
    }
}