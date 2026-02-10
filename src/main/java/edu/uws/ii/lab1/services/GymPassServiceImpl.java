package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.GymPass;
import edu.uws.ii.lab1.Member;
import edu.uws.ii.lab1.repositories.GymPassRepository;
import edu.uws.ii.lab1.repositories.MemberRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GymPassServiceImpl implements GymPassService {

    private final GymPassRepository gymPassRepository;
    private final MemberRepository memberRepository;

    public GymPassServiceImpl(GymPassRepository gymPassRepository, MemberRepository memberRepository) {
        this.gymPassRepository = gymPassRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymPass> findAll() {
        return gymPassRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymPass> findByMemberId(Long memberId) {
        return gymPassRepository.findByMemberId(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('MEMBER','STAFF','ADMIN')")
    public GymPass findById(Long id) {
        return gymPassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono karnetu"));
    }

    @Override
    @Transactional
    public GymPass save(GymPass gymPass) {
        gymPass.recomputeStatus();
        return gymPassRepository.save(gymPass);
    }

    @Override
    @Transactional
    public GymPass saveWithMember(GymPass gymPass) {
        // Bezpieczna wersja: linkujemy istniejącego użytkownika po fullName,
        // ale NIE tworzymy nowego Member automatycznie (bo Member ma @NotBlank password).
        // Jeśli nie znajdziemy użytkownika -> zapisujemy GymPass bez powiązania.
        String fullName = gymPass.getOwnerName() != null ? gymPass.getOwnerName().trim() : "";
        if (fullName.isBlank()) {
            throw new IllegalArgumentException("ownerName nie może być puste");
        }

        memberRepository.findByFullName(fullName)
                .ifPresent(gymPass::setMember);

        gymPass.recomputeStatus();
        return gymPassRepository.save(gymPass);
    }

    @Override
    @Transactional
    @Secured("ROLE_ADMIN")
    public void deleteById(Long id) {
        gymPassRepository.deleteById(id);
    }
}