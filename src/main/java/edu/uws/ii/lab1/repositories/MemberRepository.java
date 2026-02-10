package edu.uws.ii.lab1.repositories;

import edu.uws.ii.lab1.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByActivationCode(String activationCode);
    Optional<Member> findByFullName(String fullName);
}