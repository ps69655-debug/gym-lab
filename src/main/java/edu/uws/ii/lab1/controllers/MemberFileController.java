package edu.uws.ii.lab1.controllers;

import edu.uws.ii.lab1.FileStorageType;
import edu.uws.ii.lab1.services.MemberFileService;
import edu.uws.ii.lab1.services.MemberService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/files")
@PreAuthorize("hasRole('MEMBER')")
public class MemberFileController {

    private final MemberFileService memberFileService;
    private final MemberService memberService;

    public MemberFileController(MemberFileService memberFileService,
                                MemberService memberService) {
        this.memberFileService = memberFileService;
        this.memberService = memberService;
    }

    @GetMapping
    public String list(Model model) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var member = memberService.findByEmail(auth.getName());
        model.addAttribute("files", memberFileService.findByMember(member.getId()));
        model.addAttribute("storageTypes", FileStorageType.values());
        return "files/list";
    }

    @GetMapping("/upload")
    public String uploadForm() {
        return "files/upload";
    }

    @PostMapping("/upload-db")
    public String uploadDb(@RequestParam("file") MultipartFile file) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var member = memberService.findByEmail(auth.getName());

        try {
            memberFileService.storeToDatabase(member.getId(), file);
            return "redirect:/files?success=db";
        } catch (Exception e) {
            return "redirect:/files/upload?error=db";
        }
    }

    @PostMapping("/upload-disk")
    public String uploadDisk(@RequestParam("file") MultipartFile file) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var member = memberService.findByEmail(auth.getName());

        try {
            memberFileService.storeToDisk(member.getId(), file);
            return "redirect:/files?success=disk";
        } catch (Exception e) {
            return "redirect:/files/upload?error=disk";
        }
    }

    // Pobranie pliku, jeśli był zapisany w DB (BLOB)
    @GetMapping("/db/{id}")
    public ResponseEntity<byte[]> downloadDb(@PathVariable Long id) {
        var mf = memberFileService.findById(id);

        if (mf.getStorageType() != FileStorageType.DB || mf.getData() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mf.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + mf.getOriginalFilename() + "\"")
                .body(mf.getData());
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        memberFileService.delete(id);
        return "redirect:/files?success=deleted";
    }
}