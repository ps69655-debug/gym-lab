package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.Member;
import edu.uws.ii.lab1.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElse(null);
    }

    public void register(Member user) {
        var activationKey = UUID.randomUUID().toString();
        user.setActivationCode(activationKey);
        user.setEnabled(false);

        // default rola dla nowych kont
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("ROLE_MEMBER");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        memberRepository.save(user);

        String activationLink = "http://localhost:8080/activate?code=" + activationKey;
        String text = "Witaj!\n\nKliknij w link aby aktywować konto:\n" + activationLink;

        emailService.sendSimpleMessage(user.getEmail(), "Aktywacja konta", text);
    }

    public boolean activateAccount(String code) {
        var member = memberRepository.findByActivationCode(code).orElse(null);
        if (member == null) return false;

        member.setEnabled(true);
        member.setActivationCode(null);
        memberRepository.save(member);
        return true;
    }

    // --- ADMIN: lista użytkowników
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    // --- ADMIN: zmiana roli
    public void updateRole(Long memberId, String role) {
        Member m = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));
        m.setRole(role);
        memberRepository.save(m);
    }
}