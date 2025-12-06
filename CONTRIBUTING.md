# 🤝 Guía de Contribución

¡Gracias por tu interés en contribuir a PluginHub! Este documento proporciona pautas para contribuir al proyecto.

---

## 📋 Tabla de Contenidos

- [Código de Conducta](#código-de-conducta)
- [¿Cómo puedo contribuir?](#cómo-puedo-contribuir)
- [Configuración del Entorno](#configuración-del-entorno)
- [Proceso de Desarrollo](#proceso-de-desarrollo)
- [Estándares de Código](#estándares-de-código)
- [Commits](#commits)
- [Pull Requests](#pull-requests)

---

## 📜 Código de Conducta

Este proyecto adhiere a un código de conducta. Al participar, se espera que mantengas este código. Por favor reporta comportamiento inaceptable.

---

## 🎯 ¿Cómo puedo contribuir?

### Reportar Bugs

Si encuentras un bug:

1. **Verifica** que no haya sido reportado antes en [Issues](https://github.com/tuusuario/PluginHub/issues)
2. **Crea un issue** con:
   - Título descriptivo
   - Pasos para reproducir
   - Comportamiento esperado vs actual
   - Versión de PluginHub, Paper/Spigot y Java
   - Logs relevantes

### Sugerir Mejoras

Para sugerir nuevas características:

1. **Verifica** que no exista una sugerencia similar
2. **Crea un issue** explicando:
   - El problema que resuelve
   - Cómo debería funcionar
   - Alternativas consideradas

### Contribuir Código

1. **Fork** el repositorio
2. **Crea una rama** desde `main`
3. **Implementa** tu cambio
4. **Prueba** exhaustivamente
5. **Envía** un Pull Request

---

## 🛠️ Configuración del Entorno

### Requisitos

- Java 21 o superior
- Gradle 8.0+
- Git
- IDE recomendado: IntelliJ IDEA

### Configuración

```bash
# Clonar el repositorio
git clone https://github.com/tuusuario/PluginHub.git
cd PluginHub

# Compilar el proyecto
./gradlew build

# Ejecutar tests
./gradlew test

# Ejecutar servidor de prueba
./gradlew runServer
```

---

## 🔄 Proceso de Desarrollo

### 1. Crear una Rama

```bash
git checkout -b feature/nombre-descriptivo
# o
git checkout -b fix/descripcion-del-bug
```

### 2. Desarrollar

- Escribe código limpio y documentado
- Sigue los estándares del proyecto
- Añade tests si es necesario
- Actualiza documentación

### 3. Probar

```bash
# Compilar
./gradlew build

# Ejecutar tests
./gradlew test

# Probar en servidor
./gradlew runServer
```

### 4. Commit

```bash
git add .
git commit -m "tipo: descripción breve"
```

### 5. Push

```bash
git push origin feature/nombre-descriptivo
```

### 6. Pull Request

Crea un PR con:
- Título descriptivo
- Descripción detallada de cambios
- Referencias a issues relacionados
- Screenshots si aplica

---

## 📝 Estándares de Código

### Java

- **Estilo**: Seguir convenciones de Java estándar
- **Indentación**: 4 espacios
- **Líneas**: Máximo 120 caracteres
- **Nombres**: 
  - Clases: `PascalCase`
  - Métodos: `camelCase`
  - Constantes: `UPPER_SNAKE_CASE`

### Documentación

```java
/**
 * Descripción breve del método
 * 
 * @param parametro Descripción del parámetro
 * @return Descripción del retorno
 * @throws Exception Descripción de la excepción
 */
public ReturnType metodoEjemplo(String parametro) throws Exception {
    // Implementación
}
```

### Estructura de Paquetes

```
com.pluginhub/
├── commands/      # Comandos del plugin
├── managers/      # Lógica de negocio
├── models/        # Modelos de datos
├── utils/         # Utilidades
└── exceptions/    # Excepciones personalizadas
```

---

## 💬 Commits

### Formato

```
tipo(alcance): descripción breve

Descripción detallada opcional

Refs: #123
```

### Tipos

- `feat`: Nueva característica
- `fix`: Corrección de bug
- `docs`: Cambios en documentación
- `style`: Formato, punto y coma, etc
- `refactor`: Refactorización de código
- `test`: Añadir o modificar tests
- `chore`: Mantenimiento, dependencias

### Ejemplos

```bash
feat(search): añadir filtro por categoría

Implementa un sistema de filtrado que permite
buscar plugins por categoría específica.

Refs: #42

---

fix(download): corregir timeout en descargas grandes

El timeout anterior era muy corto para plugins grandes.
Aumentado a 60 segundos y añadido progreso.

Fixes: #38
```

---

## 🔀 Pull Requests

### Checklist

Antes de enviar un PR, verifica:

- [ ] El código compila sin errores
- [ ] Los tests pasan
- [ ] Añadiste tests para nuevo código
- [ ] Actualizaste la documentación
- [ ] Seguiste los estándares de código
- [ ] Los commits son descriptivos
- [ ] No hay conflictos con `main`

### Plantilla de PR

```markdown
## Descripción
Breve descripción de los cambios

## Tipo de cambio
- [ ] Bug fix
- [ ] Nueva característica
- [ ] Breaking change
- [ ] Documentación

## ¿Cómo se ha probado?
Describe las pruebas realizadas

## Checklist
- [ ] Mi código sigue los estándares del proyecto
- [ ] He realizado una auto-revisión
- [ ] He comentado código complejo
- [ ] He actualizado la documentación
- [ ] Mis cambios no generan nuevas advertencias
- [ ] He añadido tests
- [ ] Los tests nuevos y existentes pasan

## Screenshots (si aplica)
```

---

## 🧪 Testing

### Escribir Tests

```java
@Test
public void testSearchPlugins() {
    PluginDownloader downloader = new PluginDownloader(plugin, config);
    List<PluginInfo> results = downloader.searchPlugins("essentials");
    
    assertNotNull(results);
    assertFalse(results.isEmpty());
    assertTrue(results.get(0).getName().contains("Essential"));
}
```

### Ejecutar Tests

```bash
# Todos los tests
./gradlew test

# Tests específicos
./gradlew test --tests "PluginDownloaderTest"

# Con reporte
./gradlew test jacocoTestReport
```

---

## 📚 Recursos

- [Documentación de Paper](https://docs.papermc.io/)
- [Spigot Plugin Development](https://www.spigotmc.org/wiki/spigot-plugin-development/)
- [Java Coding Conventions](https://www.oracle.com/java/technologies/javase/codeconventions-contents.html)

---

## ❓ ¿Preguntas?

Si tienes preguntas:

- 💬 Abre un [Discussion](https://github.com/tuusuario/PluginHub/discussions)
- 📧 Envía un email a soporte@pluginhub.com
- 🐛 Crea un [Issue](https://github.com/tuusuario/PluginHub/issues)

---

## 🙏 Agradecimientos

¡Gracias por contribuir a PluginHub! Tu ayuda hace que este proyecto sea mejor para todos.

---

<div align="center">

**⭐ No olvides dar una estrella al proyecto si te gusta! ⭐**

</div>
