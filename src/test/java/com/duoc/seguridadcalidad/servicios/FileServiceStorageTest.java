package com.duoc.seguridadcalidad.servicios;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {

    private FileStorageService fileStorageService;
    private final Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();

    @BeforeEach
    void setUp() {
        // El servicio crea los directorios en el constructor
        fileStorageService = new FileStorageService();
    }

    @AfterEach
    void tearDown() throws IOException {
        // Calidad: Limpiamos la carpeta de uploads después de cada test para no ensuciar el proyecto
        if (Files.exists(uploadPath)) {
            FileSystemUtils.deleteRecursively(uploadPath);
        }
    }

    @Test
    @DisplayName("Debería guardar un archivo correctamente y retornar la ruta")
    void testStoreFileSuccess() throws IOException {
        // Arrange: Creamos un archivo simulado (Spring provee MockMultipartFile que no falla con Mockito)
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "receta_test.jpg",
                "image/jpeg",
                "contenido de imagen de prueba".getBytes()
        );

        // Act
        String resultPath = fileStorageService.storeFile(mockFile, "imagenes");

        // Assert
        assertNotNull(resultPath);
        assertTrue(resultPath.startsWith("/media/imagenes/"));
        assertTrue(resultPath.endsWith(".jpg"));

        // Verificación física: ¿El archivo existe en la carpeta uploads/imagenes?
        String fileName = resultPath.replace("/media/imagenes/", "");
        Path physicalPath = uploadPath.resolve("imagenes").resolve(fileName);
        assertTrue(Files.exists(physicalPath), "El archivo debería haberse guardado físicamente");
    }

    @Test
    @DisplayName("Debería lanzar excepción si el archivo tiene un nombre inválido (Path Traversal)")
    void testStoreFileInvalidPath() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "../intento_hack.exe",
                "text/plain",
                "bad data".getBytes()
        );

        // Verificamos la seguridad: No debe permitir secuencias ".."
        assertThrows(IllegalArgumentException.class, () -> {
            fileStorageService.storeFile(mockFile, "imagenes");
        });
    }

    @Test
    @DisplayName("Debería lanzar excepción si el nombre del archivo está vacío")
    void testStoreFileEmptyName() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "",
                "text/plain",
                new byte[0]
        );

        assertThrows(IllegalArgumentException.class, () -> {
            fileStorageService.storeFile(mockFile, "imagenes");
        });
    }
}