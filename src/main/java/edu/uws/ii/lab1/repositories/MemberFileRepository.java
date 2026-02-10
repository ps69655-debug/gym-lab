package edu.uws.ii.lab1.repositories;

import edu.uws.ii.lab1.MemberFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberFileRepository extends JpaRepository<MemberFile, Long> {
    List<MemberFile> findByMemberId(Long memberId);
}