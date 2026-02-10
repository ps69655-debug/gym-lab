package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.MemberFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemberFileService {

    List<MemberFile> findByMember(Long memberId);

    MemberFile storeToDatabase(Long memberId, MultipartFile file);

    MemberFile storeToDisk(Long memberId, MultipartFile file);

    MemberFile findById(Long id);

    void delete(Long id);
}