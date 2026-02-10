package edu.uws.ii.lab1;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "member_files")
@Getter
@Setter
@NoArgsConstructor
public class MemberFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String contentType;

    private long size;

    private LocalDateTime uploadedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private FileStorageType storageType;

    // DISK
    private String diskPath; // np. uploads/abc.png

    // DB
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}