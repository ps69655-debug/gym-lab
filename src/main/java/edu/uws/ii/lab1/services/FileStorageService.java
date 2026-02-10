package edu.uws.ii.lab1.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    /**
     * Zapisuje obraz dla GymPass w strukturze:
     * {files.location}/gympass/{id}/{nazwa_pliku}
     * Zwraca nazwę pliku (do zapisania w bazie).
     */
    String saveGymPassImage(Long gymPassId, MultipartFile file) throws IOException;
}