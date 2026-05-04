package com.duoc.seguridadcalidad.modelos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BannerPublicitario Model Tests")
public class BannerPublicitarioTest {

    @Test
    @DisplayName("Debería crear un BannerPublicitario con el constructor vacío")
    void testConstructorVacio() {
        BannerPublicitario banner = new BannerPublicitario();
        assertNull(banner.getIdBanner());
        assertNull(banner.getMarca());
        assertNull(banner.getUrlImagen());
        assertNull(banner.getUrlDestino());
        assertNull(banner.getUbicacion());
    }

    @Test
    @DisplayName("Debería crear un BannerPublicitario con parámetros")
    void testConstructorConParametros() {
        BannerPublicitario banner = new BannerPublicitario(
                1, "CocaCola", "https://example.com/image.jpg",
                "https://example.com", "header"
        );
        
        assertEquals(1, banner.getIdBanner());
        assertEquals("CocaCola", banner.getMarca());
        assertEquals("https://example.com/image.jpg", banner.getUrlImagen());
        assertEquals("https://example.com", banner.getUrlDestino());
        assertEquals("header", banner.getUbicacion());
    }

    @Test
    @DisplayName("Debería obtener y establecer el ID del banner")
    void testIdBannerGettersSetters() {
        BannerPublicitario banner = new BannerPublicitario();
        banner.setIdBanner(5);
        assertEquals(5, banner.getIdBanner());
    }

    @Test
    @DisplayName("Debería obtener y establecer la marca")
    void testMarcaGettersSetters() {
        BannerPublicitario banner = new BannerPublicitario();
        banner.setMarca("Pepsi");
        assertEquals("Pepsi", banner.getMarca());
    }

    @Test
    @DisplayName("Debería obtener y establecer la URL de imagen")
    void testUrlImagenGettersSetters() {
        BannerPublicitario banner = new BannerPublicitario();
        String url = "https://ads.example.com/banner.png";
        banner.setUrlImagen(url);
        assertEquals(url, banner.getUrlImagen());
    }

    @Test
    @DisplayName("Debería obtener y establecer la URL de destino")
    void testUrlDestinoGettersSetters() {
        BannerPublicitario banner = new BannerPublicitario();
        String url = "https://promotional.example.com";
        banner.setUrlDestino(url);
        assertEquals(url, banner.getUrlDestino());
    }

    @Test
    @DisplayName("Debería obtener y establecer la ubicación")
    void testUbicacionGettersSetters() {
        BannerPublicitario banner = new BannerPublicitario();
        banner.setUbicacion("sidebar");
        assertEquals("sidebar", banner.getUbicacion());
    }

    @Test
    @DisplayName("Debería permitir diferentes ubicaciones")
    void testDiferentesUbicaciones() {
        BannerPublicitario banner1 = new BannerPublicitario();
        banner1.setUbicacion("header");
        assertEquals("header", banner1.getUbicacion());
        
        banner1.setUbicacion("footer");
        assertEquals("footer", banner1.getUbicacion());
        
        banner1.setUbicacion("sidebar");
        assertEquals("sidebar", banner1.getUbicacion());
    }

    @Test
    @DisplayName("Debería actualizar todos los datos del banner")
    void testActualizarTodosDatos() {
        BannerPublicitario banner = new BannerPublicitario();
        banner.setIdBanner(1);
        banner.setMarca("NuevaMarca");
        banner.setUrlImagen("https://new.com/img.jpg");
        banner.setUrlDestino("https://new.com");
        banner.setUbicacion("footer");
        
        assertEquals(1, banner.getIdBanner());
        assertEquals("NuevaMarca", banner.getMarca());
        assertEquals("https://new.com/img.jpg", banner.getUrlImagen());
        assertEquals("https://new.com", banner.getUrlDestino());
        assertEquals("footer", banner.getUbicacion());
    }

    @Test
    @DisplayName("Debería mantener integridad de datos en múltiples banners")
    void testMultiplesBanners() {
        BannerPublicitario banner1 = new BannerPublicitario(1, "Marca1", "url1", "dest1", "header");
        BannerPublicitario banner2 = new BannerPublicitario(2, "Marca2", "url2", "dest2", "footer");
        
        assertEquals("Marca1", banner1.getMarca());
        assertEquals("Marca2", banner2.getMarca());
        assertEquals("header", banner1.getUbicacion());
        assertEquals("footer", banner2.getUbicacion());
    }
}

