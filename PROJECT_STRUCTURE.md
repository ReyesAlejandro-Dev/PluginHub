# 🏗️ Estructura del Proyecto - PluginHub

Este documento describe la arquitectura y organización del código de PluginHub.

---

## 📁 Estructura de Directorios

```
PluginHub/
├── .gradle/                    # Archivos de Gradle (generados)
├── .idea/                      # Configuración de IntelliJ IDEA
├── build/                      # Archivos compilados (generados)
├── gradle/                     # Wrapper de Gradle
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── pluginhub/
│       │           ├── commands/          # Comandos del plugin
│       │           │   ├── PluginHubCommand.java
│       │           │   ├── PluginSearchCommand.java
│       │           │   ├── PluginInstallCommand.java
│       │           │   └── PluginUpdateCommand.java
│       │           ├── managers/          # Lógica de negocio
│       │           │   └── PluginDownloader.java
│       │           ├── models/            # Modelos de datos
│       │           │   └── PluginInfo.java
│       │           ├── utils/             # Utilidades
│       │           │   ├── ConfigManager.java
│       │           │   └── ColorLogger.java
│       │           └── PluginHub.java     # Clase principal
│       └── resources/
│           ├── config.yml                 # Configuración por defecto
│           └── plugin.yml                 # Metadata del plugin
├── .gitignore                  # Archivos ignorados por Git
├── build.gradle                # Configuración de Gradle
├── CHANGELOG.md                # Historial de cambios
├── CONTRIBUTING.md             # Guía de contribución
├── EXAMPLES.md                 # Ejemplos de uso
├── gradle.properties           # Propiedades de Gradle
├── gradlew                     # Script de Gradle (Unix)
├── gradlew.bat                 # Script de Gradle (Windows)
├── PROJECT_STRUCTURE.md        # Este archivo
├── README.md                   # Documentación principal
└── settings.gradle             # Configuración del proyecto
```

---

## 🎯 Arquitectura del Código

### Patrón de Diseño

PluginHub sigue una arquitectura en capas:

```
┌─────────────────────────────────────┐
│         Capa de Presentación        │
│         (Commands)                  │
├─────────────────────────────────────┤
│         Capa de Lógica              │
│         (Managers)                  │
├─────────────────────────────────────┤
│         Capa de Datos               │
│         (Models)                    │
├─────────────────────────────────────┤
│         Capa de Utilidades          │
│         (Utils)                     │
└─────────────────────────────────────┘
```

---

## 📦 Descripción de Paquetes

### `com.pluginhub`

**Clase Principal**: `PluginHub.java`

Responsabilidades:
- Inicialización del plugin
- Registro de comandos
- Gestión del ciclo de vida
- Coordinación entre componentes

```java
public final class PluginHub extends JavaPlugin {
    private PluginDownloader pluginDownloader;
    private ConfigManager configManager;
    private ColorLogger colorLogger;
    
    @Override
    public void onEnable() { /* ... */ }
    
    @Override
    public void onDisable() { /* ... */ }
}
```

---

### `com.pluginhub.commands`

**Propósito**: Manejo de comandos del jugador/consola

#### `PluginHubCommand.java`
- Comando principal `/pluginhub`
- Subcomandos: help, version, reload, info
- Tab completion

#### `PluginSearchCommand.java`
- Comando `/phsearch <nombre>`
- Búsqueda de plugins disponibles
- Muestra resultados formateados

#### `PluginInstallCommand.java`
- Comando `/phinstall <nombre>`
- Instalación de plugins
- Validación de permisos
- Feedback detallado

#### `PluginUpdateCommand.java`
- Comando `/phupdate [nombre]`
- Lista plugins instalados
- Actualización de plugins
- Gestión de versiones

**Características Comunes**:
- Implementan `CommandExecutor`
- Implementan `TabCompleter`
- Operaciones asíncronas
- Manejo de errores robusto

---

### `com.pluginhub.managers`

**Propósito**: Lógica de negocio y operaciones complejas

#### `PluginDownloader.java`

Responsabilidades:
- Gestión de base de datos de plugins
- Búsqueda y filtrado
- Descarga de archivos
- Instalación de plugins
- Sistema de reintentos
- Gestión de caché

**Características Clave**:
```java
public final class PluginDownloader {
    // Base de datos de plugins
    private final Map<String, PluginInfo> pluginCache;
    
    // Thread pool para operaciones asíncronas
    private final ExecutorService executorService;
    
    // Caché de descargas
    private final Map<String, Long> downloadCache;
    
    // Métodos principales
    public List<PluginInfo> searchPlugins(String query);
    public CompletableFuture<Boolean> downloadPluginAsync(...);
    public CompletableFuture<Boolean> installPluginAsync(...);
}
```

---

### `com.pluginhub.models`

**Propósito**: Modelos de datos inmutables

#### `PluginInfo.java`

Representa información de un plugin:

```java
public final class PluginInfo {
    private final String name;
    private final String version;
    private final String downloadUrl;
    private final String sourceUrl;
    private final String description;
    
    // Constructor con validación
    // Getters
    // equals() y hashCode()
    // toString()
}
```

**Características**:
- Inmutable (final fields)
- Validación en constructor
- Thread-safe
- Implementa equals/hashCode

---

### `com.pluginhub.utils`

**Propósito**: Utilidades y helpers reutilizables

#### `ConfigManager.java`

Gestión de configuración:

```java
public final class ConfigManager {
    // Carga y recarga de config.yml
    public void loadConfig();
    public void reloadConfiguration();
    
    // Getters de configuración
    public int getDownloadTimeout();
    public int getDownloadRetries();
    public boolean isAutoUpdateEnabled();
    public boolean isCacheEnabled();
}
```

#### `ColorLogger.java`

Sistema de logging con colores:

```java
public final class ColorLogger {
    // Códigos ANSI
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    // ...
    
    // Métodos de logging
    public void logSuccess(String message);
    public void logError(String message);
    public void logWarning(String message);
    public void printBanner(String version);
}
```

---

## 🔄 Flujo de Datos

### Instalación de Plugin

```
Usuario ejecuta comando
        ↓
PluginInstallCommand
        ↓
Validación de permisos
        ↓
PluginDownloader.getPluginInfo()
        ↓
Verificar si existe
        ↓
PluginDownloader.installPluginAsync()
        ↓
CompletableFuture (async)
        ↓
Descarga con reintentos
        ↓
Guardar en carpeta plugins/
        ↓
Callback al thread principal
        ↓
Mensaje de éxito/error al usuario
```

### Búsqueda de Plugin

```
Usuario ejecuta comando
        ↓
PluginSearchCommand
        ↓
Validación de entrada
        ↓
Operación asíncrona
        ↓
PluginDownloader.searchPlugins()
        ↓
Filtrado en caché
        ↓
Retorno al thread principal
        ↓
Formateo de resultados
        ↓
Mostrar al usuario
```

---

## 🧵 Gestión de Threads

### Thread Principal (Bukkit)
- Comandos de usuario
- Mensajes al jugador
- Interacción con API de Bukkit

### Thread Pool (ExecutorService)
- Descargas de archivos
- Búsquedas en caché
- Operaciones I/O

### Sincronización

```java
// Operación asíncrona
plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
    // Trabajo pesado aquí
    List<PluginInfo> results = downloader.searchPlugins(query);
    
    // Volver al thread principal
    plugin.getServer().getScheduler().runTask(plugin, () -> {
        // Interactuar con Bukkit API
        sender.sendMessage("Resultados: " + results.size());
    });
});
```

---

## 🔒 Thread Safety

### Estructuras Thread-Safe

```java
// ConcurrentHashMap para caché
private final Map<String, PluginInfo> pluginCache = new ConcurrentHashMap<>();

// ExecutorService para operaciones asíncronas
private final ExecutorService executorService = Executors.newFixedThreadPool(3);

// CompletableFuture para resultados asíncronos
public CompletableFuture<Boolean> installPluginAsync(String pluginName) {
    return CompletableFuture.supplyAsync(() -> {
        // Operación segura
    }, executorService);
}
```

---

## 📊 Dependencias

### Compilación

```gradle
dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
}
```

### Runtime

- Java 21+
- Paper/Spigot 1.21+

---

## 🧪 Testing

### Estructura de Tests (Futuro)

```
src/
└── test/
    └── java/
        └── com/
            └── pluginhub/
                ├── commands/
                │   └── PluginSearchCommandTest.java
                ├── managers/
                │   └── PluginDownloaderTest.java
                └── utils/
                    └── ConfigManagerTest.java
```

---

## 📝 Convenciones de Código

### Nomenclatura

- **Clases**: `PascalCase` (ej: `PluginDownloader`)
- **Métodos**: `camelCase` (ej: `searchPlugins`)
- **Constantes**: `UPPER_SNAKE_CASE` (ej: `BUFFER_SIZE`)
- **Paquetes**: `lowercase` (ej: `com.pluginhub.commands`)

### Modificadores

```java
// Clases finales cuando no se heredan
public final class PluginDownloader { }

// Campos finales para inmutabilidad
private final JavaPlugin plugin;

// Métodos públicos documentados
/**
 * Busca plugins por nombre o descripción
 * @param query Término de búsqueda
 * @return Lista de plugins encontrados
 */
public List<PluginInfo> searchPlugins(String query) { }
```

### Organización de Métodos

1. Constructores
2. Métodos públicos
3. Métodos privados
4. Getters/Setters
5. Clases internas

---

## 🔧 Configuración de Build

### build.gradle

```gradle
plugins {
    id 'java'
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = 'com.pluginhub'
version = '1.0'

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
```

---

## 📚 Recursos Adicionales

- [README.md](README.md) - Documentación principal
- [CONTRIBUTING.md](CONTRIBUTING.md) - Guía de contribución
- [EXAMPLES.md](EXAMPLES.md) - Ejemplos de uso
- [CHANGELOG.md](CHANGELOG.md) - Historial de cambios

---

<div align="center">

**¿Preguntas sobre la arquitectura?**

[Crear Issue](https://github.com/tuusuario/PluginHub/issues/new) | [Discussions](https://github.com/tuusuario/PluginHub/discussions)

</div>
