package com.duoc.seguridadcalidad.modelos;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ingrediente Model Tests")
public class IngredienteTest {

    @Test
    @DisplayName("Debería crear un Ingrediente con el constructor vacío")
    void testConstructorVacio() {
        Ingrediente ingrediente = new Ingrediente();
        assertNull(ingrediente.getNombre());
        assertNull(ingrediente.getCantidad());
        assertNull(ingrediente.getUnidadMedida());
    }

    @Test
    @DisplayName("Debería crear un Ingrediente con parámetros")
    void testConstructorConParametros() {
        Ingrediente ingrediente = new Ingrediente("Harina", 200.0, "gramos");
        
        assertEquals("Harina", ingrediente.getNombre());
        assertEquals(200.0, ingrediente.getCantidad());
        assertEquals("gramos", ingrediente.getUnidadMedida());
    }

    @Test
    @DisplayName("Debería obtener y establecer el nombre")
    void testNombreGettersSetters() {
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNombre("Azúcar");
        assertEquals("Azúcar", ingrediente.getNombre());
    }

    @Test
    @DisplayName("Debería obtener y establecer la cantidad")
    void testCantidadGettersSetters() {
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setCantidad(150.5);
        assertEquals(150.5, ingrediente.getCantidad());
    }

    @Test
    @DisplayName("Debería obtener y establecer la unidad de medida")
    void testUnidadMedidaGettersSetters() {
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setUnidadMedida("mililitros");
        assertEquals("mililitros", ingrediente.getUnidadMedida());
    }

    @Test
    @DisplayName("Debería generar el toString correctamente")
    void testToString() {
        Ingrediente ingrediente = new Ingrediente("Sal", 5.0, "gramos");
        String str = ingrediente.toString();
        
        assertTrue(str.contains("Ingrediente{"));
        assertTrue(str.contains("nombre='Sal'"));
        assertTrue(str.contains("cantidad=5.0"));
        assertTrue(str.contains("unidadMedida='gramos'"));
    }

    @Test
    @DisplayName("Debería permitir diferentes unidades de medida")
    void testDiferentesUnidadesMedida() {
        Ingrediente ing1 = new Ingrediente("Agua", 1.0, "litro");
        Ingrediente ing2 = new Ingrediente("Sal", 10.0, "gramos");
        Ingrediente ing3 = new Ingrediente("Leche", 250.0, "mililitros");
        
        assertEquals("litro", ing1.getUnidadMedida());
        assertEquals("gramos", ing2.getUnidadMedida());
        assertEquals("mililitros", ing3.getUnidadMedida());
    }

    @Test
    @DisplayName("Debería actualizar los datos de un ingrediente")
    void testActualizarDatos() {
        Ingrediente ingrediente = new Ingrediente("Mantequilla", 100.0, "gramos");
        
        ingrediente.setNombre("Aceite de oliva");
        ingrediente.setCantidad(50.0);
        ingrediente.setUnidadMedida("mililitros");
        
        assertEquals("Aceite de oliva", ingrediente.getNombre());
        assertEquals(50.0, ingrediente.getCantidad());
        assertEquals("mililitros", ingrediente.getUnidadMedida());
    }

    @Test
    @DisplayName("Debería manejar cantidades decimales")
    void testCantidadesDecimales() {
        Ingrediente ingrediente = new Ingrediente("Levadura", 7.5, "gramos");
        assertEquals(7.5, ingrediente.getCantidad());
        
        ingrediente.setCantidad(0.5);
        assertEquals(0.5, ingrediente.getCantidad());
    }
}

