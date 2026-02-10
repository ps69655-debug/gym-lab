package edu.uws.ii.lab1.services;

import edu.uws.ii.lab1.FileStorageType;
import edu.uws.ii.lab1.Member;
import edu.uws.ii.lab1.MemberFile;
import edu.uws.ii.lab1.repositories.MemberFileRepository;
import edu.uws.ii.lab1.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MemberFileServiceImpl implements MemberFileService {

    private final MemberFileRepository memberFileRepository;
    private final MemberRepository memberRepository;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    public MemberFileServiceImpl(MemberFileRepository memberFileRepository,
                                 MemberRepository memberRepository) {
        this.memberFileRepository = memberFileRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberFile> findByMember(Long memberId) {
        return memberFileRepository.findByMemberId(memberId);
    }

    @Override
    @Transactional
    public MemberFile storeToDatabase(Long memberId, MultipartFile file) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("EMPTY_FILE");
        }

        try {
            MemberFile mf = new MemberFile();
            mf.setMember(member);
            mf.setOriginalFilename(safeName(file.getOriginalFilename()));
            mf.setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");
            mf.setSize(file.getSize());
            mf.setUploadedAt(LocalDateTime.now().withSecond(0).withNano(0));
            mf.setStorageType(FileStorageType.DB);
            mf.setData(file.getBytes());
            mf.setDiskPath(null);
            return memberFileRepository.save(mf);
        } catch (IOException e) {
            throw new RuntimeException("FILE_READ_ERROR");
        }
    }

    @Override
    @Transactional
    public MemberFile storeToDisk(Long memberId, MultipartFile file) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika"));

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("EMPTY_FILE");
        }

        String original = safeName(file.getOriginalFilename());
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) ext = original.substring(dot);

        String storedName = UUID.randomUUID() + ext;
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(dir);
            Path target = dir.resolve(storedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            MemberFile mf = new MemberFile();
            mf.setMember(member);
            mf.setOriginalFilename(original);
            mf.setContentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream");
            mf.setSize(file.getSize());
            mf.setUploadedAt(LocalDateTime.now().withSecond(0).withNano(0));
            mf.setStorageType(FileStorageType.DISK);
            mf.setDiskPath(Paths.get(uploadDir, storedName).toString().replace("\\", "/"));
            mf.setData(null);

            return memberFileRepository.save(mf);
        } catch (IOException e) {
            throw new RuntimeException("FILE_SAVE_ERROR");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MemberFile findById(Long id) {
        return memberFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono pliku"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MemberFile mf = findById(id);

        if (mf.getStorageType() == FileStorageType.DISK && mf.getDiskPath() != null) {
            try {
                Path p = Paths.get(mf.getDiskPath()).toAbsolutePath().normalize();
                // jeśli diskPath jest względny (uploads/xxx), poprawiamy:
                if (!p.toFile().exists()) {
                    p = Paths.get("").toAbsolutePath().resolve(mf.getDiskPath()).normalize();
                }
                Files.deleteIfExists(p);
            } catch (Exception ignored) {}
        }

        memberFileRepository.deleteById(id);
    }

    private String safeName(String original) {
        if (!StringUtils.hasText(original)) return "file";
        return original.replaceAll("[\\\\/\\r\\n\\t\\0]", "_");
    }
}