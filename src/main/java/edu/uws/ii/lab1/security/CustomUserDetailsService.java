package edu.uws.ii.lab1.security;

import edu.uws.ii.lab1.Member;
import edu.uws.ii.lab1.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // default: MEMBER (a nie ROLE_USER)
        String role = (member.getRole() != null && !member.getRole().isBlank())
                ? member.getRole()
                : "ROLE_MEMBER";

        return new org.springframework.security.core.userdetails.User(
                member.getEmail(),
                member.getPassword(),
                member.isEnabled(),
                true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}