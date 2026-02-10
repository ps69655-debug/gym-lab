package edu.uws.ii.lab1.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${files.location:uploads}")
    private String filesLocation;

    @Override
    public String saveGymPassImage(Long gymPassId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        String original = file.getOriginalFilename();
        String safeName = (original == null || original.isBlank())
                ? "image.jpg"
                : original.replaceAll("[^a-zA-Z0-9._-]", "_");

        Path targetDir = Path.of(filesLocation, "gympass", String.valueOf(gymPassId))
                .toAbsolutePath().normalize();

        Files.createDirectories(targetDir);

        Path targetFile = targetDir.resolve(safeName);
        Files.write(targetFile, file.getBytes());

        return safeName;
    }
}