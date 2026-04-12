package com.duoc.seguridadcalidad.data;

import com.duoc.seguridadcalidad.modelos.Ingrediente;
import com.duoc.seguridadcalidad.modelos.Receta;
import com.duoc.seguridadcalidad.modelos.Dificultad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataSeed {
    public static List<Receta> generarRecetas() {
        List<Receta> lista = new ArrayList<>();

        // Creamos la lista de imágenes para la primera receta
        List<String> imagenes = new ArrayList<>();
        imagenes.add("url.imagen1.net");
        imagenes.add("url.imagen2.net");

        // 1. Tacos al Pastor (Original)
        lista.add(new Receta(
                1, "Tacos al Pastor", "Urbana", "México", Dificultad.MEDIA, 50,
                "Marinar la carne y asar...", 4.5, LocalDate.of(2025, 12, 17),
                imagenes, new ArrayList<>(), "admin", true,
                new Ingrediente("Cerdo", 1.0, "kg"),
                new Ingrediente("Piña", 0.5, "pza")));

        // 2. Pasta Carbonara
        lista.add(new Receta(
                2, "Pasta Carbonara", "Italiana", "Italia", Dificultad.MEDIA, 30,
                "1. Hervir la pasta. 2. Freír el guanciale. 3. Mezclar queso, huevo y pimienta. 4. Unir todo fuera del fuego.", 
                4.8, LocalDate.of(2026, 1, 10),
                new ArrayList<>(), new ArrayList<>(), "admin", true,
                new Ingrediente("Espaguetis", 400.0, "gramos"),
                new Ingrediente("Huevos", 3.0, "unidades"),
                new Ingrediente("Queso Pecorino", 100.0, "gramos"),
                new Ingrediente("Guanciale", 150.0, "gramos")));

        // 3. Ensalada Griega
        lista.add(new Receta(
                3, "Ensalada Griega", "Mediterránea", "Grecia", Dificultad.BAJA, 15,
                "1. Picar tomates, pepinos, cebolla y pimiento. 2. Añadir aceitunas y queso feta. 3. Aliñar con aceite de oliva, orégano y sal.", 
                4.2, LocalDate.of(2026, 2, 5),
                new ArrayList<>(), new ArrayList<>(), "admin", true,
                new Ingrediente("Tomates", 3.0, "unidades"),
                new Ingrediente("Pepino", 1.0, "unidad"),
                new Ingrediente("Queso Feta", 200.0, "gramos"),
                new Ingrediente("Aceitunas Kalamata", 50.0, "gramos")));

        // 4. Risotto de Champiñones
        lista.add(new Receta(
                4, "Risotto de Champiñones", "Italiana", "Italia", Dificultad.MEDIA, 40,
                "1. Sellar los champiñones y reservar. 2. Sofreír cebolla y tostar el arroz. 3. Añadir el vino y luego el caldo poco a poco sin dejar de remover. 4. Integrar mantequilla, queso y los champiñones al final.", 
                4.6, LocalDate.of(2026, 3, 12),
                new ArrayList<>(), new ArrayList<>(), "admin", true,
                new Ingrediente("Arroz Arborio", 300.0, "gramos"),
                new Ingrediente("Champiñones", 250.0, "gramos"),
                new Ingrediente("Caldo de Verduras", 1.0, "litro"),
                new Ingrediente("Queso Parmesano", 80.0, "gramos")));

        // 5. Gazpacho Andaluz
        lista.add(new Receta(
                5, "Gazpacho Andaluz", "Española", "España", Dificultad.BAJA, 20,
                "1. Trocear todos los vegetales. 2. Triturar en la batidora junto con aceite, vinagre y sal. 3. Pasar por un colador fino. 4. Servir muy frío.", 
                4.7, LocalDate.of(2026, 3, 20),
                new ArrayList<>(), new ArrayList<>(), "admin", true,
                new Ingrediente("Tomates maduros", 1.0, "kg"),
                new Ingrediente("Pimiento verde", 1.0, "unidad"),
                new Ingrediente("Pepino", 0.5, "unidad"),
                new Ingrediente("Aceite de oliva extra virgen", 50.0, "ml")));

        // 6. Sushi Maki de Salmón
        lista.add(new Receta(
                6, "Sushi Maki de Salmón", "Asiática", "Japón", Dificultad.ALTA, 60,
                "1. Lavar y cocinar el arroz para sushi. 2. Condimentar el arroz con vinagre de arroz. 3. Extender el arroz sobre el alga nori. 4. Colocar tiras de salmón fresco. 5. Enrollar apretando con la esterilla. 6. Cortar en rodajas.", 
                4.9, LocalDate.of(2026, 4, 1),
                new ArrayList<>(), new ArrayList<>(), "admin", true,
                new Ingrediente("Arroz para sushi", 500.0, "gramos"),
                new Ingrediente("Salmón crudo grado sushi", 200.0, "gramos"),
                new Ingrediente("Alga Nori", 5.0, "hojas"),
                new Ingrediente("Vinagre de arroz", 2.0, "cucharadas")));

        return lista;
    }
}
