# Configuración de Desarrollo

## ⚙️ Para desarrollar localmente:

### Opción 1: Usar profile de desarrollo
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Opción 2: Variables de entorno
```bash
export H2_CONSOLE_ENABLED=true
export SHOW_SQL=true
export DDL_AUTO=create-drop
export LOG_LEVEL=DEBUG
export SECURITY_LOG_LEVEL=DEBUG
mvn spring-boot:run
```

### Opción 3: Usar application-dev.properties
El archivo `application-dev.properties` ya está configurado con todas las opciones de desarrollo.

## 🔒 Configuración segura para producción:

El archivo `application.properties` usa valores seguros por defecto:
- H2 Console: Deshabilitada
- Logging: Nivel INFO/WARN
- DDL: update (no create-drop)
- Show SQL: false

## 📝 Notas importantes:
- `application-dev.properties` NO está en git (está en .gitignore)
- Usa variables de entorno en producción
- El archivo base `application.properties` es seguro para commit