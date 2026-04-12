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
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio donde se subirán los archivos.", ex);
        }
    }

    public String storeFile(MultipartFile file, String type) {
        // type should be "imagenes" or "videos"
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        try {
            if(originalFileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }

            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            Path targetLocation = this.fileStorageLocation.resolve(type).resolve(uniqueFileName);
            
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/media/" + type + "/" + uniqueFileName;
        } catch (IOException ex) {
            throw new RuntimeException("No se pudo guardar el archivo " + originalFileName, ex);
        }
    }
}
