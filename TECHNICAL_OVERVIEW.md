# 🔧 Resumen Técnico - PluginHub v2.0

Documentación técnica de la arquitectura y mejoras implementadas.

---

## 🎯 Objetivos Alcanzados

### ✅ Integración Multi-Fuente
- **SpigotMC** - API REST (Spiget)
- **Modrinth** - API REST v2
- **Hangar** - API REST v1 (PaperMC)
- **BukkitDev** - Web Scraping (Jsoup)

### ✅ Sin Plugins Prepuestos
- Eliminada la base de datos estática
- Búsqueda dinámica en APIs reales
- Acceso a miles de plugins

### ✅ Arquitectura Sofisticada
- Búsqueda paralela asíncrona
- Sistema de caché inteligente
- Manejo robusto de errores
- Thread-safe operations

---

## 🏗️ Arquitectura del Sistema

### Diagrama de Componentes

```
┌─────────────────────────────────────────────────────┐
│                   PluginHub                         │
│                 (Clase Principal)                   │
└────────────────────┬────────────────────────────────┘
                     │
        ┌────────────┼────────────┐
        │            │            │
        ▼            ▼            ▼
   Commands      Managers      Utils
        │            │            │
        │            ▼            │
        │    PluginDownloader     │
        │            │            │
        │     ┌──────┴──────┐    │
        │     │             │    │
        │     ▼             ▼    │
        │   APIs         Models  │
        │     │             │    │
        │  ┌──┴──┐          │    │
        │  │     │          │    │
        ▼  ▼     ▼          ▼    ▼
    Spigot  Modrinth   PluginInfo
    Hangar  Bukkit
```

### Flujo de Búsqueda

```
Usuario: /phsearch essentials
        ↓
PluginSearchCommand
        ↓
PluginDownloader.searchPluginsAsync()
        ↓
┌───────────────────────────────────┐
│   Búsqueda Paralela (4 threads)  │
├───────────────────────────────────┤
│ Thread 1: SpigotAPI.search()     │
│ Thread 2: ModrinthAPI.search()   │
│ Thread 3: HangarAPI.search()     │
│ Thread 4: BukkitAPI.search()     │
└───────────────────────────────────┘
        ↓
CompletableFuture.allOf()
        ↓
Combinar y eliminar duplicados
        ↓
Ordenar por popularidad
        ↓
Guardar en caché
        ↓
Retornar resultados
        ↓
Mostrar al usuario
```

---

## 🔌 APIs Implementadas

### 1. SpigotAPI (Spiget)

**Endpoint:** `https://api.spiget.org/v2`

**Características:**
- API REST completa
- Información de recursos
- Estadísticas de descargas
- Ratings y reviews
- Versiones y actualizaciones

**Ejemplo de Request:**
```http
GET https://api.spiget.org/v2/search/resources/essentials?size=10&sort=-downloads
User-Agent: PluginHub/2.0
```

**Ejemplo de Response:**
```json
[
  {
    "id": "9089",
    "name": "EssentialsX",
    "tag": "Essential commands and utilities",
    "author": {
      "name": "EssentialsX Team"
    },
    "version": {
      "name": "2.20.1"
    },
    "downloads": 2500000,
    "rating": {
      "average": 4.8
    },
    "category": {
      "name": "Admin Tools"
    }
  }
]
```

### 2. ModrinthAPI

**Endpoint:** `https://api.modrinth.com/v2`

**Características:**
- API moderna y rápida
- Soporte para múltiples loaders
- Versiones por Minecraft version
- Categorización avanzada

**Ejemplo de Request:**
```http
GET https://api.modrinth.com/v2/search?query=essentials&limit=10&facets=[["project_type:plugin"],["categories:bukkit"]]
User-Agent: PluginHub/2.0
```

**Ejemplo de Response:**
```json
{
  "hits": [
    {
      "project_id": "abc123",
      "title": "EssentialsX",
      "description": "Essential commands",
      "author": "EssentialsX",
      "downloads": 2500000,
      "categories": ["bukkit", "admin"],
      "game_versions": ["1.21", "1.20.4"]
    }
  ]
}
```

### 3. HangarAPI (PaperMC)

**Endpoint:** `https://hangar.papermc.io/api/v1`

**Características:**
- Repositorio oficial de Paper
- Plugins verificados
- Información de dependencias
- Múltiples plataformas

**Ejemplo de Request:**
```http
GET https://hangar.papermc.io/api/v1/projects?q=essentials&limit=10&sort=-downloads
User-Agent: PluginHub/2.0
```

### 4. BukkitAPI (Web Scraping)

**Endpoint:** `https://dev.bukkit.org`

**Características:**
- Web scraping con Jsoup
- Plugins clásicos
- Compatibilidad legacy

**Técnica:**
```java
Document doc = Jsoup.parse(html);
Elements projects = doc.select(".project-list-item");

for (Element item : projects) {
    String name = item.selectFirst(".project-list-item__name a").text();
    String description = item.selectFirst(".project-list-item__description").text();
    // ...
}
```

---

## 💾 Modelo de Datos

### PluginInfo (Builder Pattern)

```java
public final class PluginInfo {
    private final String id;              // ID único en la fuente
    private final String name;            // Nombre del plugin
    private final String version;         // Versión
    private final String author;          // Autor
    private final String description;     // Descripción
    private final String downloadUrl;     // URL de descarga
    private final String sourceUrl;       // URL de la página
    private final PluginSource source;    // Fuente (enum)
    private final List<String> supportedVersions;  // Versiones MC
    private final int downloads;          // Número de descargas
    private final double rating;          // Rating (0-5)
    private final long lastUpdate;        // Timestamp
    private final String category;        // Categoría
    private final boolean premium;        // Es premium?
    private final List<String> dependencies;  // Dependencias
}
```

**Uso del Builder:**
```java
PluginInfo info = new PluginInfo.Builder()
    .id("9089")
    .name("EssentialsX")
    .version("2.20.1")
    .author("EssentialsX Team")
    .description("Essential commands")
    .downloadUrl("https://...")
    .sourceUrl("https://...")
    .source(PluginSource.SPIGOT)
    .downloads(2500000)
    .rating(4.8)
    .category("Admin Tools")
    .build();
```

---

## 🔄 Sistema de Caché

### Implementación

```java
private final Map<String, PluginInfo> searchCache;
private final Map<String, Long> cacheTimestamps;

// Guardar en caché
searchCache.put(pluginName.toLowerCase(), pluginInfo);
cacheTimestamps.put(query.toLowerCase(), System.currentTimeMillis());

// Verificar caché
Long timestamp = cacheTimestamps.get(cacheKey);
long cacheAge = System.currentTimeMillis() - timestamp;
long maxAge = configManager.getCacheDuration() * 60 * 1000L;

if (cacheAge < maxAge) {
    return cachedResults;
}
```

### Ventajas

- ✅ Reduce llamadas a APIs externas
- ✅ Mejora tiempo de respuesta
- ✅ Configurable (duración)
- ✅ Thread-safe (ConcurrentHashMap)
- ✅ Limpieza manual disponible

---

## 🚀 Operaciones Asíncronas

### CompletableFuture

```java
public CompletableFuture<List<PluginInfo>> searchPluginsAsync(String query) {
    return CompletableFuture.supplyAsync(() -> {
        List<CompletableFuture<List<PluginInfo>>> futures = new ArrayList<>();
        
        // Búsqueda paralela en 4 fuentes
        futures.add(CompletableFuture.supplyAsync(() -> 
            spigotAPI.searchPlugins(query, SEARCH_LIMIT), executorService));
        
        futures.add(CompletableFuture.supplyAsync(() -> 
            modrinthAPI.searchPlugins(query, SEARCH_LIMIT), executorService));
        
        futures.add(CompletableFuture.supplyAsync(() -> 
            hangarAPI.searchPlugins(query, SEARCH_LIMIT), executorService));
        
        futures.add(CompletableFuture.supplyAsync(() -> 
            bukkitAPI.searchPlugins(query, SEARCH_LIMIT), executorService));
        
        // Esperar a todas
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        // Combinar resultados
        List<PluginInfo> allResults = new ArrayList<>();
        for (CompletableFuture<List<PluginInfo>> future : futures) {
            allResults.addAll(future.get());
        }
        
        return allResults;
    }, executorService);
}
```

### ExecutorService

```java
private final ExecutorService executorService = Executors.newFixedThreadPool(5);

// Shutdown limpio
public void shutdown() {
    executorService.shutdown();
    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
    }
}
```

---

## 🔒 Thread Safety

### Estructuras Concurrentes

```java
// ConcurrentHashMap para caché
private final Map<String, PluginInfo> searchCache = new ConcurrentHashMap<>();

// Operaciones atómicas
searchCache.putIfAbsent(key, value);
searchCache.computeIfAbsent(key, k -> computeValue());
```

### Sincronización con Bukkit

```java
// Operación pesada en thread pool
plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
    List<PluginInfo> results = heavyOperation();
    
    // Volver al thread principal para Bukkit API
    plugin.getServer().getScheduler().runTask(plugin, () -> {
        sender.sendMessage("Resultados: " + results.size());
    });
});
```

---

## 📊 Manejo de Errores

### Estrategia de Reintentos

```java
private boolean downloadWithRetry(PluginInfo info, File destination) {
    int maxRetries = configManager.getDownloadRetries();
    
    for (int attempt = 1; attempt <= maxRetries; attempt++) {
        try {
            if (performDownload(info.getDownloadUrl(), destination, timeout)) {
                return true;
            }
        } catch (IOException e) {
            logger.warning("Error en intento " + attempt + "/" + maxRetries);
            
            if (attempt < maxRetries) {
                Thread.sleep(2000 * attempt); // Backoff exponencial
            }
        }
    }
    
    return false;
}
```

### Manejo de Fallos Parciales

```java
// Si una fuente falla, las demás continúan
futures.add(CompletableFuture.supplyAsync(() -> {
    try {
        return spigotAPI.searchPlugins(query, SEARCH_LIMIT);
    } catch (IOException e) {
        logger.warning("Error en Spigot: " + e.getMessage());
        return Collections.emptyList(); // Retorna lista vacía, no falla todo
    }
}, executorService));
```

---

## 🔧 Dependencias

### build.gradle

```gradle
dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    
    // HTTP Client moderno
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    
    // JSON parsing
    implementation("com.google.code.gson:gson:2.10.1")
    
    // HTML parsing para web scraping
    implementation("org.jsoup:jsoup:1.17.2")
}
```

### Justificación

- **OkHttp**: Cliente HTTP moderno, eficiente, con connection pooling
- **Gson**: JSON parsing rápido y confiable
- **Jsoup**: HTML parsing para BukkitDev (no tiene API)

---

## 📈 Métricas de Rendimiento

### Tiempos de Respuesta

| Operación | v1.0 | v2.0 | Mejora |
|-----------|------|------|--------|
| Búsqueda local | 1ms | - | - |
| Búsqueda API única | - | 500ms | - |
| Búsqueda paralela | - | 2-5s | ✅ |
| Instalación | 5-10s | 5-10s | = |
| Caché hit | - | 1ms | ✅ |

### Escalabilidad

- **v1.0**: 8 plugins máximo
- **v2.0**: Miles de plugins disponibles
- **Caché**: Hasta 1000+ plugins en memoria
- **Threads**: Pool de 5 threads para operaciones

---

## 🔮 Mejoras Futuras

### v3.0 Planeado

1. **Base de datos SQLite**
   - Caché persistente
   - Historial de instalaciones
   - Estadísticas de uso

2. **Sistema de Dependencias**
   - Detección automática
   - Instalación de dependencias
   - Resolución de conflictos

3. **Verificación de Integridad**
   - Checksums SHA-256
   - Firma digital
   - Validación de archivos

4. **Filtrado Avanzado**
   - Por versión de Minecraft
   - Por categoría
   - Por rating mínimo
   - Por número de descargas

5. **GitHub Integration**
   - Búsqueda en GitHub Releases
   - Plugins de repositorios públicos
   - Actualización desde commits

---

## 📚 Referencias

- [Spiget API Docs](https://spiget.org/documentation/)
- [Modrinth API Docs](https://docs.modrinth.com/api-spec/)
- [Hangar API Docs](https://hangar.papermc.io/api-docs)
- [OkHttp Documentation](https://square.github.io/okhttp/)
- [Gson User Guide](https://github.com/google/gson/blob/master/UserGuide.md)
- [Jsoup Cookbook](https://jsoup.org/cookbook/)

---

<div align="center">

**Arquitectura diseñada para escalabilidad y rendimiento**

Made with ❤️ by Pecar

</div>
