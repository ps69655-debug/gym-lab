package edu.uws.ii.lab1.controllers;

import edu.uws.ii.lab1.GymPass;
import edu.uws.ii.lab1.services.FileStorageService;
import edu.uws.ii.lab1.services.GymPassService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping
public class GymPassController {

    private final GymPassService gymPassService;
    private final FileStorageService fileStorageService;

    public GymPassController(GymPassService gymPassService,
                             FileStorageService fileStorageService) {
        this.gymPassService = gymPassService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/GymPass")
    @PreAuthorize("hasAnyRole('MEMBER','STAFF','ADMIN')")
    public String listGymPasses(Model model) {
        model.addAttribute("gympasses", gymPassService.findAll());
        return "GymPass";
    }

    @GetMapping("/form")
    @PreAuthorize("hasRole('ADMIN')")
    public String showForm(Model model, @RequestParam Optional<Long> id) {
        GymPass gymPass = id.isPresent()
                ? gymPassService.findById(id.get())
                : new GymPass();

        model.addAttribute("gymPass", gymPass);
        return "form";
    }

    @PostMapping("/form")
    @PreAuthorize("hasRole('ADMIN')")
    public String processForm(@Valid @ModelAttribute("gymPass") GymPass gymPass,
                              BindingResult result,
                              @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
                              Model model) {
        if (result.hasErrors()) {
            return "form";
        }

        // 1) zapis encji, żeby mieć ID (potrzebne do folderu /gympass/{id}/)
        GymPass saved = gymPassService.saveWithMember(gymPass);

        // 2) zapis pliku na dysk + zapamiętanie nazwy pliku w bazie
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = fileStorageService.saveGymPassImage(saved.getId(), imageFile);
                saved.setImageFileName(fileName);
                gymPassService.saveWithMember(saved);
            } catch (IOException e) {
                model.addAttribute("uploadError", "Nie udało się zapisać pliku: " + e.getMessage());
                model.addAttribute("gymPass", saved);
                return "form";
            }
        }

        return "redirect:/GymPass";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteGymPass(@RequestParam Long id) {
        gymPassService.deleteById(id);
        return "redirect:/GymPass";
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("hasAnyRole('MEMBER','STAFF','ADMIN')")
    public String details(@PathVariable Long id, Model model) {
        GymPass gymPass = gymPassService.findById(id);
        model.addAttribute("gymPass", gymPass);
        return "details";
    }
}