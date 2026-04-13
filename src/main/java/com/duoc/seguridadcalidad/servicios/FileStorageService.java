package com.duoc.seguridadcalidad.servicios;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation.resolve("imagenes"));
            Files.createDirectories(this.fileStorageLocation.resolve("videos"));
        } catch (IOException ex) { // S112: specific exception instead of generic Exception
            throw new IllegalStateException("No se pudo crear el directorio donde se subirán los archivos.", ex);
        }
    }

    public String storeFile(MultipartFile file, String type) throws IOException {
        // S4449 / S2589: cleanPath can return null when getOriginalFilename() is null;
        // guard with a safe fallback before any further use.
        String rawName = file.getOriginalFilename();
        if (rawName == null || rawName.isBlank()) {
            throw new IllegalArgumentException("El archivo no tiene nombre.");
        }
        String originalFileName = StringUtils.cleanPath(rawName);

        // S112: specific exception instead of RuntimeException
        if (originalFileName.contains("..")) {
            throw new IllegalArgumentException("Sorry! Filename contains invalid path sequence: " + originalFileName);
        }

        // S2589 fix: originalFileName is guaranteed non-null here (checked above)
        String fileExtension = "";
        if (originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        Path targetLocation = this.fileStorageLocation.resolve(type).resolve(uniqueFileName);

        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return "/media/" + type + "/" + uniqueFileName;
    }
}
